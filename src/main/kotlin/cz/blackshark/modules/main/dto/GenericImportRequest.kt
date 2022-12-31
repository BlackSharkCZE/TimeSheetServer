package cz.blackshark.modules.main.dto

import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.File
import javax.ws.rs.FormParam
import javax.ws.rs.core.MediaType

class GenericImportRequest {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: File? = null

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    var fileName: String? = null
}
