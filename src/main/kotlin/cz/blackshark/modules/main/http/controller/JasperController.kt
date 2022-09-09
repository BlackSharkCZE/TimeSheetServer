package cz.blackshark.http.controller

import cz.blackshark.modules.main.beans.InvoiceGenerateBean
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/jasper")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class JasperController @Inject constructor(val invoiceGenerateBean: InvoiceGenerateBean) {

	companion object {
		private val logger = LoggerFactory.getLogger(JasperController::class.java)
	}


	@GET
	@Path("/generate/{invoiceID}")
	@Throws(WebApplicationException::class)
	fun generate(@PathParam("invoiceID") invoiceID: Long): Response {
		logger.debug("Request to generate PDF for invoice $invoiceID")
		val data = invoiceGenerateBean.generatePDFInvoice(invoiceID)
		return Response.ok(data, MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.header("Content-Disposition", "attachment; filename=$invoiceID.pdf")
				.build()
	}

}
