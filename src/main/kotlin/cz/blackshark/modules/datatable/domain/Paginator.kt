package cz.blackshark.modules.datatable.domain

data class Paginator(
		val pageCount: Int,
		val itemsPerPage: Int,
		val currentPage: Int,
		val totalRecords: Long
)
