package cz.blackshark.modules.main.domains

import cz.blackshark.modules.main.persistence.entity.InvoiceEntity

data class BeanProcessingResult(val isError: Boolean, val errorCode: String?, val entity: InvoiceEntity? = null)
