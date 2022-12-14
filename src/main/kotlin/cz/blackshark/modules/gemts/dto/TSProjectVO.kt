package cz.blackshark.modules.gemts.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TSProjectVO(
    val key: Long,
    val text: String,
    val parentId: Int?,
    val projects: List<TSProjectVO>,
    val tags: List<TSProjectVO>
)
