package cz.blackshark.modules.gemts.http.client

import cz.blackshark.annotations.Logged
import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.dto.TSListItemVO
import cz.blackshark.modules.gemts.dto.TSProjectVO
import cz.blackshark.modules.gemts.dto.WriteWorkLogRequestVo
import cz.blackshark.modules.jira.customizations.JiraClientObjectMapper
import cz.blackshark.modules.main.http.provider.ResteasyWebApplicationExceptionMapper
import io.smallrye.config.SmallRyeConfig
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

@ClientHeaderParam(
    name = "Authorization",
    value = ["{cz.blackshark.modules.gemts.http.client.GemTimesheetClient.auth}"]
)
@RegisterRestClient(configKey = "gem-timesheet")
@Path("/rest")
@RegisterProviders(
    RegisterProvider(JiraClientObjectMapper::class),
    RegisterProvider(ResteasyWebApplicationExceptionMapper::class),
)

@Logged
interface GemTimesheetClient {

    fun auth(): String {
        val config = ConfigProvider.getConfig().unwrap(SmallRyeConfig::class.java)
        val timesheet = config.getConfigMapping(GemTimesheetConfig::class.java)
        val username = timesheet.username()
        val password = timesheet.password()
        return "Basic " + Base64.getEncoder().encodeToString("$username:$password".toByteArray())
    }

    @GET
    @Path("timesheets")
    fun workLog(@QueryParam("monthCode") monthCode: String): List<TSListItemVO>

    @GET
    @Path("projects/fullProjectsStructure")
    fun getRootProjects(): List<TSProjectVO>

    @POST
    @Path("/timesheets")
    fun saveWorkLog(data: WriteWorkLogRequestVo): List<WriteWorkLogRequestVo>
}
