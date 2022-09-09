package cz.blackshark.modules.main.dto

data class DashboardVo(
    val workTimeDashboard: WorkTimeDashboardVo,
    val projectReportList: List<ProjectReportVo>
)
