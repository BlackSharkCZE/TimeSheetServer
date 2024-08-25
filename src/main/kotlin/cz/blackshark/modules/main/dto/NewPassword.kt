package cz.blackshark.modules.main.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewPassword(
    @NotBlank
    val subject: String,
    @NotBlank
    val oldPassword: String,
    @NotBlank
    @Size(min = 8)
    val newPassword: String,
)
