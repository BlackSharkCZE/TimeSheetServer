package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.dto.CurrentUserVo
import cz.blackshark.security.TimesheetPrincipal
import javax.annotation.security.PermitAll
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("user")
@PermitAll
class UserController {

    @GET
    @Path("/current")
    fun getCurrentUser(@Context securityContext: SecurityContext): CurrentUserVo {

        return (securityContext.userPrincipal as? TimesheetPrincipal)?.let { tp ->
            CurrentUserVo(
                login = tp.name,
                roles = tp.roles,
                companyId = tp.companyId
            )
        } ?: throw IllegalStateException("No user in context")
    }
}
