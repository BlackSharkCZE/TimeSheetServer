package cz.blackshark.modules.main.dto

import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.File
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.ws.rs.FormParam
import javax.ws.rs.core.MediaType

class InvoiceRequest {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: File? = null

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    var fileName: String? = null

    @FormParam("issuerID")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    var issuerId: Long = -1

    @FormParam("recipientID")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    var recipientId: Long = -1

    @FormParam("invoiceNumber")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    @NotEmpty
    lateinit var invoiceNumber: String

    @FormParam("issueDate")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    @NotEmpty
    lateinit var issueDate: String

    @FormParam("paymentDate")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    @NotEmpty
    lateinit var paymentDate: String

    @FormParam("vatPaymentDate")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull
    @NotEmpty
    lateinit var vatPaymentDate: String

}
