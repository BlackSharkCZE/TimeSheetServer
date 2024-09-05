package cz.blackshark.modules.ares.http.client

import cz.blackshark.modules.ares.model.EkonomickySubjekt
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RegisterRestClient(configKey = "ares-api-2")
@Path("/ekonomicke-subjekty-v-be/rest")
interface AresClientV2 {

    @Produces(MediaType.APPLICATION_JSON)
    @Path("ekonomicke-subjekty/{ico}")
    @GET
    fun vratEkonomickySubjekt(@PathParam("ico") ico: String): EkonomickySubjekt
}
