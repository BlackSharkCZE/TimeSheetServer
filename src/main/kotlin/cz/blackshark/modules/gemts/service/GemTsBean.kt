package cz.blackshark.modules.gemts.service

import cz.blackshark.annotations.Logged
import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.gemts.converter.GemTimesheetConverter
import cz.blackshark.modules.gemts.dto.TSProjectVO
import cz.blackshark.modules.gemts.http.client.GemTimesheetClient
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import kotlin.math.roundToLong

@ApplicationScoped
@Logged
class GemTsBean @Inject constructor(
    @RestClient val client: GemTimesheetClient,
    private val config: GemTimesheetConfig,
    private val converter: GemTimesheetConverter
) {

    fun getAllProjects(): List<TSProjectVO> = client.getRootProjects()

    fun findDrMaxProject(): TSProjectVO? =
        getAllProjects().firstOrNull { it.text == config.drMaxProjectName() }

    fun workTimeOnProject(projectName: String, month: LocalDate): Duration {
        val workLog = client.workLog(month.format(DateTimeFormatter.ofPattern("YYYY/MM")))
        return Duration.of(
            (workLog
                .filter { it.rootProjectName == projectName }
                .sumOf { it.hours.toDouble() } * 60.0).roundToLong(),
            ChronoUnit.MINUTES
        )
    }

    fun logIntoTimesheet(timeline: TimelineEntity,settings: RemoteWriteSettingsEntity) : RestResponse<Unit> {
        val data = converter.convertTimeline(timeline, settings)
        data?.let { client.saveWorkLog(it) }
        return RestResponse(true, null, null, null)

    }

}
