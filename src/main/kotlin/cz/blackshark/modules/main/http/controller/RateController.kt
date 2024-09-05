package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.beans.RateBean
import cz.blackshark.modules.main.dto.RateVo
import cz.blackshark.modules.main.exceptions.TsException
import io.quarkus.security.Authenticated
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("rate")
@Authenticated
@RequestScoped
class RateController(
    private val rateBean: RateBean,
    private val principalService: PrincipalService,
) {

    @POST
    @Path("/create")
    @Throws(TsException::class)
    @Transactional
    @RolesAllowed("admin", "superadmin")
    fun creteRate(@Context context: SecurityContext, rateVo: RateVo): RateVo {

        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subject ->
            rateBean.createRate(rateVo, subject)
        }
    }

    @GET
    @Path("/list")
    @Throws(TsException::class)
    @RolesAllowed("admin", "superadmin")
    fun getAllUserRates(@Context securityContext: SecurityContext): List<RateVo> {
        return principalService.withTimesheetPrincipalAndSubjectEntity(securityContext) { tp, subject ->
            rateBean.findForSubject(subject)
        }
    }
}
