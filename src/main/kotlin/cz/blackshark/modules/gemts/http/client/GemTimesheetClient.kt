package cz.blackshark.modules.gemts.http.client

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.gemts.dto.TSListItemVO
import cz.blackshark.modules.gemts.dto.TSProjectVO
import cz.blackshark.modules.gemts.dto.WriteWorkLogRequestVo
import cz.blackshark.modules.gemts.service.GemTimesheetLogin
import cz.blackshark.modules.jira.customizations.JiraClientObjectMapper
import cz.blackshark.modules.main.http.provider.ResteasyWebApplicationExceptionMapper
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.CookieParam
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response

@RegisterRestClient(configKey = "gem-timesheet")
@Path("/rest")
@RegisterProviders(
    RegisterProvider(JiraClientObjectMapper::class),
    RegisterProvider(ResteasyWebApplicationExceptionMapper::class),
)

@Logged
interface GemTimesheetClient {

    @GET
    @Path("timesheets")
    fun workLog(
        @QueryParam("monthCode") monthCode: String,
        @CookieParam(GemTimesheetLogin.QUAKRUS_COOKIE_LOGIN_NAME) cookie: String,
    ): List<TSListItemVO>

    @GET
    @Path("projects/fullProjectsStructure")
    fun getRootProjects(@CookieParam(GemTimesheetLogin.QUAKRUS_COOKIE_LOGIN_NAME) cookie: String): List<TSProjectVO>

    @POST
    @Path("/timesheets")
    fun saveWorkLog(
        data: WriteWorkLogRequestVo,
        @CookieParam(GemTimesheetLogin.QUAKRUS_COOKIE_LOGIN_NAME) cookie: String,
    ): List<WriteWorkLogRequestVo>
}
