package cz.blackshark.modules.main.factories

import cz.blackshark.modules.main.dto.TimelineVo
import cz.blackshark.modules.main.persistence.entity.ProjectEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity

object TimelineEntityFactory {

    fun create(timelineVo: TimelineVo, project: ProjectEntity, subject: SubjectEntity): TimelineEntity {
        val entity = TimelineEntity()
        entity.projectEntity = project
        entity.fromTime = timelineVo.fromTime
        entity.toTime = timelineVo.toTime
        entity.pause = timelineVo.pause
        entity.note = timelineVo.note
        entity.subject = subject
        return entity
    }

}
