package cz.blackshark.modules.main.beans.remotewriters

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import org.jboss.logging.Logger
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Interface to process remote timeline writer. (Write to JIRA or Timesheet)
 */
abstract class AbstractTimelineRemoteWriter : TimelineRemoteWriter {

    @Inject
    lateinit var logger: Logger

    /**
     * Write to remote system
     * @param timeline data to write
     * @return true when success or false
     */
    override fun write(timeline: TimelineEntity, settings: RemoteWriteSettingsEntity): RestResponse<RemoteWriteTimestampEntity?> {

        val record = getRemoteWriteRepository().findByTimelineAndWriter(timeline.id!!, getName())
        if (record == null || record.success == false) {
            return try {
                val resp = getWriterFunc()(timeline, settings)
                RestResponse(resp.success, resp.message, persistTimestamp(record, resp, timeline))
            } catch (e: Exception) {
                logger.errorf(e, "Remote write of timeline %s failed on writer %s", timeline, getName())
                RestResponse(false, "Remote write failed!", null, null)
            }
        } else {
            logger.infof(
                "Record for %s already wrote by %s. Timestamp: %s",
                timeline,
                getName(),
                record.writeTimestamp!!.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
            )
            return RestResponse(true, "Already wrote.", null, null)
        }
    }

    private fun persistTimestamp(current:  RemoteWriteTimestampEntity?, response: RestResponse<Unit>?, currentTimeline: TimelineEntity):  RemoteWriteTimestampEntity {
        val toStore = current
            ?: RemoteWriteTimestampEntity().apply {
                this.timeline = currentTimeline
                this.writerName = getName()
                this.success = response?.success ?: false
            }
        val x = toStore.apply { writeTimestamp = ZonedDateTime.now() }
        getRemoteWriteRepository().persist(x)
        return x
    }

}
