package cz.blackshark.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class UserVo (@JsonProperty("first_name") val firstName:String,
									 @JsonProperty("last_name") val lastName:String)
