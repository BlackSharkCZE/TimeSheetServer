package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.InvoiceBean
import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@ApplicationScoped
class InvoiceGenerateController(
    private val invoiceBean: InvoiceBean,
    private val principalService: PrincipalService,
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate")
    @Throws(NotAuthorizedException::class)
    @Transactional
    fun generateInvoice(
        @QueryParam("issueDate") issueDate: LocalDate?,
        @QueryParam("companyID") companyID: Long,
        @Context context: SecurityContext,
    ): RestResponse<InvoiceEntity> {

        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subject ->
            val iDate = issueDate ?: LocalDate.now()
            val entity = invoiceBean.generateInvoice(subject, companyID, iDate, tp)
            RestResponse(true, null, entity, null)
        }
    }
}
