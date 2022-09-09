package cz.blackshark.timesheet.commons.domain

import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.http.views.Views

@JsonView(Views.Full::class)
class CompanyVo : CompanyBaseVo() {
	var okres: String? = null
	var obec: String? = null
	var castObce: String? = null
	var ulice: String? = null
	var cisloDomu: String? = null
	var psc: String? = null
	var phoneNumber: String? = null
	var primaryAccount: Boolean = false
	var bankAccountNumber: String? = null

	fun getPs() = psc
}
