package cz.blackshark.timesheet.commons.domain

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.http.views.Views

open class CompanyBaseVo {
    @JsonView(Views.Simple::class)
    var id: Long? = null
    @JsonView(Views.Simple::class)
    var ic: String? = null
    @JsonView(Views.Simple::class)
    var companyName: String? = null
    @JsonView(Views.Simple::class)
    var dic: String? = null
    @JsonView(Views.Simple::class)
    var platceDph: Boolean = true
    var email: String? = null
    var primary: Boolean = false


}
