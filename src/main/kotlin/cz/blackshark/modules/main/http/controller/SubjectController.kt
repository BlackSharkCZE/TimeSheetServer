package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.dto.SubjectDetailVo
import cz.blackshark.modules.main.http.views.Views
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
class SubjectController {

    @Inject
    private lateinit var jwt: JsonWebToken

    @GET
    @JsonView(Views.Simple::class)
    fun findSubjectDetail(@Context context: SecurityContext ): SubjectDetailVo {
        return SubjectDetailVo(jwt.subject, context.userPrincipal.name, CompanyVo().apply {
            this.ic = "776543654"
            this.platceDph = false
            this.phoneNumber = "420778503001"
        })
    }

}
