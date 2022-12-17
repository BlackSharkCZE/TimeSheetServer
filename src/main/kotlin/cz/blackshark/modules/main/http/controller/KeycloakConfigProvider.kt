package cz.blackshark.modules.main.http.controller

import cz.blackshark.config.ApplicationConfig
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("keycloak-config")
class KeycloakConfigProvider {

    @Inject
    private lateinit var applicationConfig: ApplicationConfig

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getConfigFile(): String {
        return Files.readString(Paths.get(applicationConfig.keycloakConfig()))
    }


}
