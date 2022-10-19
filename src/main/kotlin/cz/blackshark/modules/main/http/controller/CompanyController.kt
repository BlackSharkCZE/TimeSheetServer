package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.InvoiceNumberGenerator
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.timesheet.commons.domain.CompanyVo
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class CompanyController {

	@Inject
	lateinit var jwtToken: JsonWebToken

	@Inject
	lateinit var companyRepositoryBase: CompanyRepository

	@Inject
	lateinit var invoiceNumberGenerator: InvoiceNumberGenerator

	@POST
	@Path("/search")
	fun search(searchMap: Map<String, Any>): List<CompanyEntity> {
		val query = searchMap.map { (k, _) -> "$k = :$k" }.joinToString("and")
		return companyRepositoryBase.list(query, searchMap)
	}

	@GET
	@Path("/all")
	fun getAllCompany(@QueryParam("primary") @DefaultValue("false") primary: Boolean): List<CompanyEntity> = companyRepositoryBase.findWithPrimary(primary)


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
