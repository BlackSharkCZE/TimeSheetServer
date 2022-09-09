package cz.blackshark.modules.gemts.converter

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.dto.WriteWorkLogRequestVo
import cz.blackshark.modules.main.exceptions.ConverterException
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.tools.DateTimeUtils
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GemTimesheetConverter @Inject constructor(

    private val gemTsConfig: GemTimesheetConfig
) {

    @Throws(ConverterException::class)
    fun convertTimeline(timelineEntity: TimelineEntity, settings: RemoteWriteSettingsEntity): WriteWorkLogRequestVo? {
        return WriteWorkLogRequestVo(
            settings.projectId!!,
            settings.projectRootId!!,
            gemTsConfig.username(),
            timelineEntity.note,
            timelineEntity.fromTime.toLocalDate(),
            timelineEntity.toTime.toLocalDate(),
            timelineEntity.fromTime.toLocalTime(),
            timelineEntity.toTime.toLocalTime(),
            timelineEntity.pause,
            DateTimeUtils.getTimeDiffInHours(timelineEntity.fromTime, timelineEntity.toTime, timelineEntity.pause),
            tagId = settings.tagId ?: 0
        )
    }


}
