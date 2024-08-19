package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.beans.DphBean
import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.dto.DphInvoiceSumPreviewVo
import cz.blackshark.modules.main.dto.VatReport
import cz.blackshark.modules.main.http.views.Views
import io.quarkus.logging.Log
import io.quarkus.security.Authenticated
import java.time.LocalDate
import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("dph")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class DphController(
    private val dphBean: DphBean,
    private val principalService: PrincipalService,
) {

    @GET
    @Path("issued-invoice")
    @RolesAllowed("admin", "superadmin")
    fun getAllInvoiceOverview(@Context securityContext: SecurityContext): List<DphInvoiceSumPreviewVo> {
        return principalService.withTimesheetPrincipalSubject(securityContext) { tp, _ ->
            dphBean.getAllIssuedInvoice(tp.companyId ?: -1)
        }
    }

    @GET
    @Path("received-invoice")
    @RolesAllowed("admin", "superadmin")
    fun getAllReceivedOverview(@Context securityContext: SecurityContext): List<DphInvoiceSumPreviewVo> {
        return principalService.withTimesheetPrincipalSubject(securityContext) { tp, _ ->
            dphBean.getAllReceivedInvoice(tp.companyId ?: -1)
        }
    }

    /**
     * Download all invoice for the DPH for at the given month
     */
    @GET
    @Path("download/{month}/{company}")
    @RolesAllowed("admin", "superadmin")
    fun downloadInvoiceBatch(
        @PathParam("month") month: LocalDate,
        @PathParam("company") companyID: Long,
        @Context context: SecurityContext,
    ): Response {
        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subject ->
            Log.infof("Incoming date: %s", month.toString())
            Response.ok(dphBean.createInvoiceZip(companyID, month, subject), MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .header("Content-Disposition", "attachment; filename=dph-${month}.zip")
                .build()
        }
    }

    @GET
    @Path("download/tax-report/{year}/{company}")
    @RolesAllowed("admin", "superadmin")
    fun downloadTaxBundle(
        @PathParam("year") year: Int,
        @PathParam("company") companyID: Long,
        @Context context: SecurityContext,
    ): Response {
        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subject ->
            Log.infof("Download invoices for tax for year $year and company $companyID")
            Response.ok(
                dphBean.createInvoiceTaxBundle(year, companyID, subject),
                MediaType.APPLICATION_OCTET_STREAM_TYPE
            )
                .header("Content-Disposition", "attachment;filename=tax-bundle-${year}.zip")
                .build()
        }
    }

    /**
     * Generate reports VAT reports for company
     */
    @GET
    @Path("tax-report/{year}/{company}")
    @JsonView(Views.Simple::class)
    @RolesAllowed("admin", "superadmin")
    fun getVatReport(
        @PathParam("year") year: Int,
        @PathParam("company") companyID: Long,
        @Context context: SecurityContext,
    ): VatReport {
        return principalService.withTimesheetPrincipalAndSubjectEntity(context) { tp, subject ->
            Log.infof("Generate invoice sum for tax for year $year and company ${companyID}")
            dphBean.generateTaxReport(year, companyID, subject)
        }
    }
}
