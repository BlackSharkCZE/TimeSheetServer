package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.tools.DateTimeUtils
import java.time.ZonedDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TimelineVo(
    val id: Long?,
    val projectName: String?,
    val projectId: Long,
    val fromTime: ZonedDateTime,
    val toTime: ZonedDateTime,
    val pause: Int,
    val workTime: String,
    @NotNull @NotEmpty val note: String,
    val remoteWriteTimestamp: List<RemoteWriteTimestampVo>?,
    @JsonProperty(required = false)
    val writers: List<String>?
) {
    constructor(entity: TimelineEntity, writers: List<String>) : this(
        entity.id,
        entity.projectEntity?.name,
        entity.projectEntity!!.id!!,
        entity.fromTime,
        entity.toTime,
        entity.pause,
        DateTimeUtils.formatDateDiff(entity.fromTime, entity.toTime, entity.pause),
        entity.note,
        entity.remoteWriteTimestamp.map { RemoteWriteTimestampVo(it.id!!, it.writerName!!, it.writeTimestamp!!, it.success!!) },
        writers
    )
}
