package cz.blackshark.modules.main.converter

import cz.blackshark.modules.main.dto.IncomingInvoiceMetadataVo
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceItemEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.timesheet.commons.domain.InvoiceHeaderVo
import cz.blackshark.timesheet.commons.domain.InvoiceItemVo
import cz.blackshark.timesheet.commons.domain.InvoiceVo
import java.math.BigDecimal


object InvoiceMapper {

    fun toValueObject(invoiceEntity: InvoiceEntity, subjectEntity: SubjectEntity): InvoiceVo {
        return InvoiceVo(
            InvoiceHeaderVo(
                invoiceEntity.number!!,
                invoiceEntity.items.first().requisition?.customerNumber,
                invoiceEntity.issueDate!!,
                invoiceEntity.paymentDate!!,
                invoiceEntity.vatPaymentDate,
                CompanyMapper.convert(invoiceEntity.issuerCompany!!),
                CompanyMapper.convert(invoiceEntity.recipientCompany!!),
                invoiceEntity.items.map { it.totalPrice }.fold(BigDecimal.ZERO, BigDecimal::add),
                invoiceEntity.items.sumOf { it.price },
                subjectEntity.firstName + " " + subjectEntity.lastName,
            ),
            invoiceEntity.items.map(this::toValueObject)
        )
    }

    fun toValueObject(invoiceItem: InvoiceItemEntity): InvoiceItemVo {
        return if (invoiceItem.vat > BigDecimal(0.1)) {
            InvoiceItemVo(
                invoiceItem.id!!,
                invoiceItem.requisition?.note ?: invoiceItem.note ?: "Descriptiop missing",
                invoiceItem.price,
                invoiceItem.vat.minus(BigDecimal.ONE),
                invoiceItem.price.times(invoiceItem.vat.minus(BigDecimal.ONE)),
                invoiceItem.totalPrice
            )
        } else {
            InvoiceItemVo(
                invoiceItem.id!!,
                invoiceItem.requisition?.note ?: "",
                invoiceItem.price,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                invoiceItem.totalPrice
            )
        }
    }

    fun convert(
        metadata: IncomingInvoiceMetadataVo,
        systemFileName: String,
        issuer: CompanyEntity,
        recipient: CompanyEntity
    ): InvoiceEntity {
        return InvoiceEntity().apply {
            number = metadata.invoiceNumber
            issuerCompany = issuer
            recipientCompany = recipient
            issueDate = metadata.issueDate
            paymentDate = metadata.paymentDate
            vatPaymentDate = metadata.vatPaymentDate
            storePath = systemFileName
        }
    }
}
