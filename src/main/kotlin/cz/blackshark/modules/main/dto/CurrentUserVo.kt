package cz.blackshark.modules.main.dto

data class CurrentUserVo(
    val login: String,
    val roles: List<String>,
    val companyId: Long?,
)
