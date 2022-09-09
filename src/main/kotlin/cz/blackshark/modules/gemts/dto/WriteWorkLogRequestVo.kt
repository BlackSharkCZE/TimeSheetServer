package cz.blackshark.modules.gemts.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.blackshark.modules.jira.customizations.LocalTimeDeserializer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class WriteWorkLogRequestVo(
   val projectId: Long,
   val rootProjectId: Long,
   val employeeId: String,
   val description: String,

   @JsonFormat(pattern = "yyyy-MM-dd")
   val dateStringFrom: LocalDate,

   @JsonFormat(pattern = "yyyy-MM-dd")
   val dateStringTo: LocalDate,

   @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
   @JsonDeserialize(using = LocalTimeDeserializer::class)
   val hoursFrom: LocalTime,

   @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
   @JsonDeserialize(using = LocalTimeDeserializer::class)
   val hoursTo: LocalTime,

   val pause: Int,
   val hours: BigDecimal,

   val id: Long = 0,
   val onCall: Boolean = false,
   val tagId: Int = 0,
   val blockedEditing: Boolean = false,

)
