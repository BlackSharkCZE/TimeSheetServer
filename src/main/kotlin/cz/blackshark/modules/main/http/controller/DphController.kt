package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.beans.DphBean
import cz.blackshark.modules.main.dto.VatReport
import cz.blackshark.modules.main.http.views.Views
import org.jboss.logging.Logger
import java.time.LocalDate
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("dph")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class DphController @Inject constructor(
    private val logger: Logger,
    private val dphBean: DphBean
) {

    /**
     * Download all invoice for the DPH for at the given month
     */
    @GET
    @Path("download/{month}/{company}")
    fun downloadInvoiceBatch(
        @PathParam("month") month: LocalDate,
        @PathParam("company") companyID: Long
    ): Response {
        logger.infof("Incoming date: %s", month.toString())
        return Response.ok(dphBean.createInvoiceZip(companyID, month), MediaType.APPLICATION_OCTET_STREAM_TYPE)
            .header("Content-Disposition", "attachment; filename=dph-${month}.zip")
            .build()
    }

    @GET
    @Path("download/tax-report/{year}/{company}")
    fun downloadTaxBundle(
        @PathParam("year") year: Int,
        @PathParam("company") companyID: Long
    ): Response {
        logger.infof("Download invoices for tax for year $year and company $companyID")
        return Response.ok(dphBean.createInvoiceTaxBundle(year, companyID), MediaType.APPLICATION_OCTET_STREAM_TYPE)
            .header("Content-Disposition", "attachment;filename=tax-bundle-${year}.zip")
            .build()
    }

    /**
     * Generate reports VAT reports for company
     */
    @GET
    @Path("tax-report/{year}/{company}")
    @JsonView(Views.Simple::class)
    fun getVatReport(
        @PathParam("year") year: Int,
        @PathParam("company") companyID: Long) : VatReport {
        logger.infof("Generate invoice sum for tax for year $year and company ${companyID}")
        return dphBean.generateTaxReport(year, companyID)

    }

}
