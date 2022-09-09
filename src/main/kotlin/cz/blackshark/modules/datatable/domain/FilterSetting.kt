package cz.blackshark.modules.datatable.domain

data class FilterSetting(
		var type: String,
		var value: Any,
		var childKey: String? = null
) {
	constructor() : this("","") {
	}
}

