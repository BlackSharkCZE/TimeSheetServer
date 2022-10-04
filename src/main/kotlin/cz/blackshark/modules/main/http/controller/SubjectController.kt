package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.dto.SubjectDetailVo
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
    fun findSubjectDetail(@Context context: SecurityContext ): SubjectDetailVo {
        return SubjectDetailVo(jwt.subject)
    }

}
