package cz.blackshark.http.controller

import io.quarkus.runtime.configuration.ProfileManager
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String = ProfileManager.getActiveProfile()
}
