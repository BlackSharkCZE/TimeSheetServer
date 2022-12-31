package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.beans.SubjectBean
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.modules.main.dto.SubjectDetailVo
import cz.blackshark.modules.main.http.views.Views
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.timesheet.commons.domain.CompanyVo
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("subject")
@Authenticated
class SubjectController  {

    @Inject
    private lateinit var jwt: JsonWebToken

    @Inject
    private lateinit var companyRepository: CompanyRepository

    @Inject
    private lateinit var subjectBean: SubjectBean

    @GET
    @JsonView(Views.Simple::class)
    fun findSubjectDetail(@Context context: SecurityContext ): SubjectDetailVo {
        val company = companyRepository.findPrimaryCompany()?.let {
            CompanyMapper.convert(it)
        }
        val subjectEntity = subjectBean.findOrCreateSubject(jwt.subject)
        return SubjectDetailVo(jwt.subject, context.userPrincipal.name, subjectEntity.id!!, company)
    }
}
