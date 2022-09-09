package cz.blackshark.modules.gemts.http.resources

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.gemts.dto.TSProjectVO
import cz.blackshark.modules.gemts.service.GemTsBean
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query
import javax.inject.Inject

@GraphQLApi
@Logged
class GemTimesheetResource @Inject constructor(private val service: GemTsBean)  {

    @Query("allProjects")
    @Description("Get all projects in the Timesheet System")
    fun getAllTodos(): List<TSProjectVO> = service.getAllProjects()

}
