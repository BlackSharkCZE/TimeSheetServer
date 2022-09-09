package cz.blackshark.modules.main.beans

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.remotewriters.AbstractTimelineRemoteWriter
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.repository.RemoteWriteSettingsRepository
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Logged
class RemoteWriterBean @Inject constructor(
    private val remoteWriteSettingsRepository: RemoteWriteSettingsRepository,
    private val timelineRepository: TimelineRepository,
    private val remoteWriters: Instance<AbstractTimelineRemoteWriter>,
    private val logger: Logger

) {

    fun findAll(): List<RemoteWriteSettingsEntity> = remoteWriteSettingsRepository.findAll().list()

    fun upsert(item: RemoteWriteSettingsEntity): RemoteWriteSettingsEntity {
        remoteWriteSettingsRepository.persist(item)
        /*if (item.id != null) {
            val oldItem = remoteWriteSettingsRepository.findById(item.id)
            remoteWriteSettingsRepository.persist(
                oldItem.apply {
                    this.remoteWriterList = item.remoteWriterList
                    this.keyPattern = item.keyPattern
                    this.projectId = item.projectId
                    this.projectRootId = item.projectRootId
                })
        } else {
            remoteWriteSettingsRepository.persist(item)
        }*/
        return item
    }

    @Transactional
    fun write(timelineId: Long): Map<String, RestResponse<RemoteWriteTimestampEntity?>?> {
        val timeline = timelineRepository.findById(timelineId)

        val settings = remoteWriteSettingsRepository.findAll()
            .list<RemoteWriteSettingsEntity>()
            .firstOrNull { it.keyPattern!!.toRegex().containsMatchIn(timeline.note) }

        val writerList: List<String> = settings!!.remoteWriterList?.split(";") ?: emptyList<String>()
        val responseMap = writerList.map { writerName ->
            writerName to remoteWriters.firstOrNull { it.getName() == writerName }?.write(timeline, settings)
        }.filter { it.second != null }.toMap()

        logger.infof("Response from remote write: %s", responseMap)
        return responseMap

    }
}
