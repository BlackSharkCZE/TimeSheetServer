package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import cz.blackshark.timesheet.commons.domain.CompanyVo

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubjectDetailVo(
    val subject: String,
    val company: CompanyVo? = null
)
