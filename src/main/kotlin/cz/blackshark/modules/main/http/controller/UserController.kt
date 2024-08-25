package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.beans.UserBean
import cz.blackshark.modules.main.dto.CurrentUserVo
import cz.blackshark.modules.main.dto.NewPassword
import cz.blackshark.security.TimesheetPrincipal
import io.quarkus.security.Authenticated
import javax.annotation.security.PermitAll
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("user")
@Authenticated
class UserController(
    private val principalService: PrincipalService,
    private val userBean: UserBean,
) {

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


    @POST
    @Path("/password")
    @Transactional
    fun setNewPassword(@Context securityContext: SecurityContext, @Valid password: NewPassword): Response {
       return principalService.withTimesheetPrincipalAndSubjectEntity(securityContext) { tp, subject ->
          userBean.setNewPassword(subject, password)
           Response.ok().build()
       }

    }

}
