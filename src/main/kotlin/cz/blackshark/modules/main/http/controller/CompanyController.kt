package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.InvoiceNumberGenerator
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.exceptions.TsException
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.UserRepository
import cz.blackshark.security.TimesheetPrincipal
import cz.blackshark.timesheet.commons.domain.CompanyVo
import io.quarkus.security.Authenticated
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class CompanyController {

    @Inject
    lateinit var companyRepositoryBase: CompanyRepository

    @Inject
    lateinit var invoiceNumberGenerator: InvoiceNumberGenerator

    @Inject
    lateinit var userRepository: UserRepository

    @PUT
    @Path("/primary/{companyId}")
    @Transactional
    fun setAsPrimary(
        @PathParam("companyId") companyId: Long,
        @Context securityContext: SecurityContext,
    ): CompanyEntity {

        return (securityContext.userPrincipal as? TimesheetPrincipal)?.let { tp ->
           companyRepositoryBase.findById(companyId)?.let { company ->
               userRepository.findById(tp.name)?.let { user ->
                   user.companyId = company.id
                   userRepository.persist(user)
                   company
               } ?: throw TsException("User with name ${tp.name} not found in database!", null)
           } ?: throw CompanyExcetption("Company with ID $companyId not found in database!")
        } ?: throw SecurityException("Invalid principal in security context!")

    }

    @POST
    @Path("/search")
    fun search(searchMap: Map<String, Any>): List<CompanyEntity> {
        val query = searchMap.map { (k, _) -> "$k = :$k" }.joinToString("and")
        return companyRepositoryBase.list(query, searchMap)
    }

    @GET
    @Path("/all")
    fun getAllCompany(@QueryParam("primary") @DefaultValue("false") primary: Boolean): List<CompanyEntity> =
        companyRepositoryBase.findWithPrimary(primary)

    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: Long): CompanyEntity {
        return companyRepositoryBase.findById(id)
    }

    @POST
    @Transactional
    fun saveNewCompany(@Valid companyEntity: CompanyEntity): CompanyEntity {
        companyRepositoryBase.persist(companyEntity)
        companyEntity.id?.let {
            invoiceNumberGenerator.createForCompany(it, 1L)
        }
        return companyEntity
    }

    @Path("/primary")
    @POST
    @Transactional
    fun createPrimaryCompany(@Valid companyEntity: CompanyEntity): CompanyVo {
        companyRepositoryBase.persist(companyEntity.apply { primaryAccount = true })
        companyEntity.id?.let {
            invoiceNumberGenerator.createForCompany(it, 1L)
        }
        return CompanyMapper.convert(companyEntity)
    }
}
