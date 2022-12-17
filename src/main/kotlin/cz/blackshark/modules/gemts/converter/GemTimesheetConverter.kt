package cz.blackshark.modules.gemts.converter

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.dto.WriteWorkLogRequestVo
import cz.blackshark.modules.main.exceptions.ConverterException
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.tools.DateTimeUtils
import java.time.ZoneId
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GemTimesheetConverter @Inject constructor(

    private val gemTsConfig: GemTimesheetConfig
) {

    @Throws(ConverterException::class)
    fun convertTimeline(timelineEntity: TimelineEntity, settings: RemoteWriteSettingsEntity): WriteWorkLogRequestVo? {
        val zone = ZoneId.of("Europe/Prague")
        val fromTimeZ = timelineEntity.fromTime.withZoneSameInstant(zone)
        val toTimeZ = timelineEntity.toTime.withZoneSameInstant(zone)
        return WriteWorkLogRequestVo(
            settings.projectId!!,
            settings.projectRootId!!,
            gemTsConfig.username(),
            timelineEntity.note,
            fromTimeZ.toLocalDate(),
            toTimeZ.toLocalDate(),
            fromTimeZ.toLocalTime(),
            toTimeZ.toLocalTime(),
            timelineEntity.pause,
            DateTimeUtils.getTimeDiffInHours(fromTimeZ, toTimeZ, timelineEntity.pause),
            tagId = settings.tagId ?: 0
        )
    }

}
