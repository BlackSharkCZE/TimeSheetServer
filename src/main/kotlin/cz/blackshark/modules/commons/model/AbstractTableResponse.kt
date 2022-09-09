package cz.blackshark.modules.commons.model

data class AbstractTableResponse<T> (
		val success:Boolean,
		val message:String?,
		val fields:List<String>,
		val items:List<T>
)
