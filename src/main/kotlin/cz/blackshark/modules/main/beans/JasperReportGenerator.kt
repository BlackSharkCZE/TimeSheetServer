package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.converter.InvoiceMapper
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.timesheet.commons.domain.InvoiceVo
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.HtmlExporter
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.*
import java.io.ByteArrayOutputStream
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class JasperReportGenerator {

    fun generateInvoicePDF(entity: InvoiceEntity, subject: SubjectEntity): ByteArray {

        val invoice: InvoiceVo = InvoiceMapper.toValueObject(entity, subject)
        val ds = JRBeanCollectionDataSource(invoice.items)
        val params = mutableMapOf("InvoiceItemDataSource" to ds, "InvoiceHeader" to invoice.header)

        val print = JasperFillManager.fillReport(
            javaClass.getResourceAsStream("/jasper/InvoiceV1.jasper"),
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

    fun generateInvoiceHTML(entity: InvoiceEntity, subject: SubjectEntity): ByteArray {

        val invoice: InvoiceVo = InvoiceMapper.toValueObject(entity, subject)
        val ds = JRBeanCollectionDataSource(invoice.items)
        val params = mutableMapOf("InvoiceItemDataSource" to ds, "InvoiceHeader" to invoice.header)

        val print = JasperFillManager.fillReport(
            javaClass.getResourceAsStream("/jasper/InvoiceV1.jasper"),
            params,
            JREmptyDataSource()
        )
        val exporter = HtmlExporter()
        exporter.setExporterInput(SimpleExporterInput(print))
        val out = ByteArrayOutputStream()
        exporter.exporterOutput = SimpleHtmlExporterOutput(out)
        val exportConfig = SimpleHtmlExporterConfiguration()
        exporter.setConfiguration(exportConfig)
        exporter.exportReport()
        return out.toByteArray()

    }


}
