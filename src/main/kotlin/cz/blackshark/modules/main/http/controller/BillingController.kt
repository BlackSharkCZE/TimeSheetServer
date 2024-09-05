package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.BillingBean
import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.dto.BillingLineVo
import io.quarkus.security.Authenticated
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Path("/billing")
@Authenticated
class BillingController(
    private val billingBean: BillingBean,
    private val principalService: PrincipalService,
) {

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Throws(NotAuthorizedException::class)
    fun getInvoice(@QueryParam("to") to: LocalDate?, @Context securityContext: SecurityContext): List<BillingLineVo> {
        return principalService.withTimesheetPrincipalAndSubjectEntity(securityContext) { tp, subject ->
            val toDate = to ?: LocalDate
                .now()
                .minus(1, ChronoUnit.MONTHS)
                .with(TemporalAdjusters.lastDayOfMonth())
            billingBean.getBillingList(subject, toDate)
        }
    }
}
