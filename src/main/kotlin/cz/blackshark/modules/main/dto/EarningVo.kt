package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class EarningVo (
		val earning:BigDecimal,
		val companyID: Long,
		val companyName: String,
		val workTime:BigDecimal
)
