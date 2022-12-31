package cz.blackshark.modules.gemts.http.resources

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.gemts.dto.TSProjectVO
import cz.blackshark.modules.gemts.service.GemTsBean
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("gem-timesheet-resource")
class GemTimesheetResource @Inject constructor(private val service: GemTsBean) {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllTodos(): List<TSProjectVO> = service.getAllProjects()

}
