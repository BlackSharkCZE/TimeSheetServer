package cz.blackshark.modules.main.beans.remotewriters

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.RemoteWriteRepository

interface TimelineRemoteWriter {

    fun getName(): String // return remote system name
    fun getWriterFunc(): (timeline: TimelineEntity,settings: RemoteWriteSettingsEntity) -> RestResponse<Unit>
    fun write(timeline: TimelineEntity, settings: RemoteWriteSettingsEntity): RestResponse<RemoteWriteTimestampEntity?>
    fun getRemoteWriteRepository(): RemoteWriteRepository


}
