package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.persistence.entity.InvoiceItemEntity
import cz.blackshark.modules.main.persistence.repository.InvoiceItemRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import cz.blackshark.timesheet.commons.domain.InvoiceItemVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@ApplicationScoped
open class InvoiceDetailController {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(InvoiceDetailController::class.java)
    }

    @Inject
    lateinit var invoiceItemRepository: InvoiceItemRepository

    @Inject
    lateinit var invoiceRepository: InvoiceRepository

    @GET
    @Path("{invoiceID}")
    fun getInvoiceDetail(@PathParam("invoiceID") invoiceID: Long): Response {
        val invoice = invoiceRepository.findById(invoiceID)
//		val items:List<InvoiceItemEntity> = invoiceItemRepository.find("invoiceEntity.id", invoiceNumber).list()

        return Response.ok(invoice).type(MediaType.APPLICATION_JSON).build()
    }

    @POST
    @Path("{invoiceID}/add-item")
    @Transactional
    fun addItem(
        @PathParam("invoiceID") invoiceID: Long,
        @Valid item: InvoiceItemVo
    ): Response {

        val invoice = invoiceRepository.findById(invoiceID)
        val invoiceItemEntity = InvoiceItemEntity()
        invoiceItemEntity.vat = item.vatRate
        invoiceItemEntity.price = item.price
        invoiceItemEntity.totalPrice = item.priceWithVat
        invoiceItemEntity.invoiceEntity = invoice
        invoiceItemEntity.requisition = null
        invoiceItemEntity.note = item.description
        invoiceItemRepository.persist(invoiceItemEntity)
        if (invoiceItemEntity.id != null && invoiceItemEntity.id!! > 0L) {
            return Response.status(Response.Status.CREATED).entity(
                "InvoiceItem added to invoice " +
                        "${invoice.number} with ID: ${invoiceItemEntity.id}"
            ).build()
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DELETE
    @Path("{invoiceNumber}/delete-item/{itemID}")
    @Transactional
    fun deleteItem(
        @PathParam("invoiceNumber") invoiceNumber: String,
        @PathParam("itemID") itemID: Long
    ): Response {
        val invoiceItem = invoiceItemRepository
            .find("id=?1 and invoiceEntity.number=?2", itemID, invoiceNumber)
            .firstResultOptional<InvoiceItemEntity>()
        if (invoiceItem.isPresent) {
            invoiceItemRepository.delete(invoiceItem.get())
            return Response.ok().build()
        } else {
            logger.error("Can not find InvoiceItem for ID {} and InvoiceNumber {}", itemID, invoiceNumber)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
        }
    }


}
