package cz.blackshark.modules.datatable.domain

/**
 * Common Response object for the DataTable
 */
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class DataTableResponse<RowType>(
		val rows:List<RowType>,
		val paginator: Paginator
)
