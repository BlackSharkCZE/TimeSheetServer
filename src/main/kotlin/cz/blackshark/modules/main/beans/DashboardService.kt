package cz.blackshark.modules.main.beans

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.service.GemTsBean
import cz.blackshark.modules.main.dto.DashboardVo
import cz.blackshark.modules.main.dto.ProjectReportVo
import cz.blackshark.modules.main.dto.WorkTimeDashboardVo
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DashboardService @Inject constructor(
    private val gemConfig: GemTimesheetConfig,
    private val gemTsBean: GemTsBean,
    private val timelineRepository: TimelineRepository,
    private val workTimeUtils: WorkTimeUtils) {


    fun getCurrentDashboard(): DashboardVo {
        return getDashboard(LocalDate.now().withDayOfMonth(1))
    }

    fun getDashboard(date: LocalDate): DashboardVo {
        val drMaxWorkTime = gemTsBean.workTimeOnProject(gemConfig.drMaxProjectName(), date.withDayOfMonth(1))
        return DashboardVo(
            WorkTimeDashboardVo(
                date,
                workTimeUtils.getWorkTimeByMonth(date) * 8,
                timelineRepository.currentWorkTimeInMonth(date),
            ),
            listOf(
                ProjectReportVo(gemConfig.drMaxProjectName(), drMaxWorkTime)
            )
        )
    }
}
