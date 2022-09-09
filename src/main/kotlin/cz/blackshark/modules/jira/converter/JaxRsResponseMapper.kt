package cz.blackshark.modules.jira.converter

import cz.blackshark.modules.commons.model.RestResponse
import javax.ws.rs.core.Response

object JaxRsResponseMapper {

    fun convert(response: Response): RestResponse<Unit> =
        RestResponse<Unit>(
            response.status in 200..299,
            if (response.hasEntity()) response.readEntity(String::class.java); else "",
            null,
            response.status
        )


}
