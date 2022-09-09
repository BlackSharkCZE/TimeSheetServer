package cz.blackshark.modules.ares.http.client

import cz.blackshark.modules.ares.domains.generated.AresOdpovedi
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@RegisterRestClient(configKey = "ares-api")
@Path("/cgi-bin/ares")
interface AresClient {

    @Produces(MediaType.APPLICATION_XML)
    @Path("darv_bas.cgi")
    @GET()
    fun basicQuery(@QueryParam("ico") ico: String): AresOdpovedi

}
