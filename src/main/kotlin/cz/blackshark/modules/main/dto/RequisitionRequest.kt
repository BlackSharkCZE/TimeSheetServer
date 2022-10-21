package cz.blackshark.modules.main.dto

import org.jboss.resteasy.annotations.providers.multipart.PartFilename
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.File
import java.io.InputStream
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.ws.rs.FormParam
import javax.ws.rs.core.MediaType

class RequisitionRequest {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    lateinit var file: File

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @NotEmpty
    lateinit var fileName: String

    @FormParam("note")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @NotEmpty
    lateinit var note: String

    @FormParam("orderNumber")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @NotEmpty
    lateinit var orderNumber: String

    @FormParam("startDate")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @NotEmpty
    lateinit var startDate: String

    @FormParam("endDate")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @NotEmpty
    lateinit var endDate: String

    @FormParam("companyId")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull @Min(1)
    var companyId: Long = -1

}
