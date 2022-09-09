package cz.blackshark.modules.gemts.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import cz.blackshark.modules.gemts.converter.HoursToJsonDeserializer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TSListItemVO(
    val id:Long,
    val employeeId: String,
    val employeeName: String,
    val dateString: String,
    val dateStringISO: LocalDate,
    val hoursFrom: LocalTime,
    @JsonDeserialize(using = HoursToJsonDeserializer::class)
    val hoursTo: LocalTime,
    val pause: Int,
    val hours:BigDecimal,
    val status: Int,
    val confirmedByName: String?,
    val confirmationDescription: String?,
    val onCall: Boolean,
    val rootProjectName: String,
    val projectName: String,
    val tagName: String?,
    val description: String
)
