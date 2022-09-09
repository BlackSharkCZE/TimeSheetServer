package cz.blackshark.modules.main.http.resource

import cz.blackshark.modules.main.beans.DashboardService
import cz.blackshark.modules.main.dto.DashboardVo
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Name
import org.eclipse.microprofile.graphql.Query
import java.time.LocalDate
import javax.inject.Inject

@GraphQLApi
class DashboardResource @Inject constructor(private val dashboardService: DashboardService) {

    @Query("currentDashboard")
    @Description("Get Dashboard data for the current month")
    fun getCurrentDashboard(): DashboardVo = dashboardService.getCurrentDashboard()

    @Query
    @Description("Get Dashboard for the given month of the date")
    fun getDashboard(@Name("date") date: LocalDate): DashboardVo = dashboardService.getDashboard(date)

}
