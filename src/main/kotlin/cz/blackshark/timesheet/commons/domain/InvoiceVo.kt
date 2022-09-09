package cz.blackshark.timesheet.commons.domain

data class InvoiceVo(val header:InvoiceHeaderVo, val items:List<InvoiceItemVo>)
