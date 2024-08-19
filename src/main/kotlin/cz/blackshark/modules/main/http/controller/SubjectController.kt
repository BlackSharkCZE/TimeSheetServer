package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.beans.SubjectBean
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.modules.main.dto.SubjectDetailVo
import cz.blackshark.modules.main.exceptions.TsException
import cz.blackshark.modules.main.http.views.Views
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.timesheet.commons.domain.CompanyVo
import io.quarkus.security.Authenticated
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("subject")
@Authenticated
class SubjectController(
    private val companyRepository: CompanyRepository,
    private val subjectBean: SubjectBean,
    private val principalService: PrincipalService,
)  {

    @GET
    @JsonView(Views.Simple::class)
    fun findSubjectDetail(@Context context: SecurityContext ): SubjectDetailVo {

        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subjectEntity ->
            companyRepository.findById(tp.companyId)?.let { company ->
                val companyVo = CompanyMapper.convert(company)
                val subjectEntity = subjectBean.findOrCreateSubject(tp.subjectValue)
                SubjectDetailVo("TODO", context.userPrincipal.name, subjectEntity.id!!, companyVo, subjectEntity.firstName, subjectEntity.lastName)
            } ?: throw TsException("Company with id [${tp.companyId}] not found", null)

        }
    }
}
