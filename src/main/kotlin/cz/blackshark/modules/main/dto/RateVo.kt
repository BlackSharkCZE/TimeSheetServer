package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.validation.constraints.Min

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class RateVo(
    var id: Long?,
    @Min(0)
    val rate: BigDecimal,
    val validSince: ZonedDateTime,
    val companyId: Long,
    val companyName: String? = null
)
