package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.domains.InvoiceItem
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import net.sf.jasperreports.export.SimplePdfExporterConfiguration
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.NotFoundException

@ApplicationScoped
class InvoiceGenerateBean @Inject constructor(val invoiceRepository: InvoiceRepository) {

    companion object {
        private val logger = LoggerFactory.getLogger(InvoiceGenerateBean::class.java)
    }

    fun generatePDFInvoice(invoiceID: Long): ByteArray {
        val invoice = invoiceRepository.findById(invoiceID)
        if (invoice != null) {
            return processInvoice(invoice)
        } else {
            logger.warn("Invoice with number $invoiceID not found!")
            throw NotFoundException("Invoice with number $invoiceID not found!")
        }
    }


    private fun processInvoice(invoice: InvoiceEntity): ByteArray {

        val ds = JRBeanCollectionDataSource(
            invoice.items.map {
                InvoiceItem(
                    it.requisition?.note ?: it.note ?: "Descriptiop missing",
                    it.price,
                    it.vat.times(it.price),
                    it.vat.minus(BigDecimal.ONE),
                    it.totalPrice
                )
            }
        )


//        val issuer = invoice.issuerCompany
//        val recipient = invoice.recipientCompany

        val params = mutableMapOf<String, Any>("CollectionBeanParam" to ds)

        val print = JasperFillManager.fillReport(
            javaClass.getResourceAsStream("/jasper/Invoice.jasper"),
            params,
            JREmptyDataSource()
        )
        val exporter = JRPdfExporter()
        exporter.setExporterInput(SimpleExporterInput(print))
        val out = ByteArrayOutputStream()
        exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)
        val exportConfig = SimplePdfExporterConfiguration()
        exporter.setConfiguration(exportConfig)
        exporter.exportReport()
        return out.toByteArray()
    }


}
