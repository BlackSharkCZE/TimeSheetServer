package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.converter.InvoiceMapper
import cz.blackshark.modules.main.dto.DphInvoiceSumPreviewVo
import cz.blackshark.modules.main.dto.VatReport
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.dao.DphDao
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DphBean @Inject constructor(
    private val invoiceBean: InvoiceBean,
    private val invoiceRepository: InvoiceRepository,
    private val companyRepository: CompanyRepository,
    private val dphDao: DphDao
) {

    fun createInvoiceZip(companyID: Long, month: LocalDate, subjectEntity: SubjectEntity): ByteArray {

        val issued = invoiceRepository.findIssued(
            companyID,
            month.with(TemporalAdjusters.firstDayOfMonth()),
            month.with(TemporalAdjusters.lastDayOfMonth())
        )
        val received = invoiceRepository.findReceived(
            companyID,
            month.with(TemporalAdjusters.firstDayOfMonth()),
            month.with(TemporalAdjusters.lastDayOfMonth())
        )

        val baos = ByteArrayOutputStream()
        val zip = ZipOutputStream(baos)

        issued.forEach { invoice -> putInvoiceToZip(invoice, zip, subjectEntity) }
        received.forEach { invoice -> putInvoiceToZip(invoice, zip, subjectEntity) }

        zip.close()
        return baos.toByteArray()
    }

    fun generateTaxReport(year: Int, companyID: Long, subject: SubjectEntity): VatReport {
        val data = findInvoices(year, companyID).map { InvoiceMapper.toValueObject(it, subject)} .map { it.header }
        return VatReport(data, data.sumOf { it.paymentSum }, data.sumOf { it.paymentSumWithoutVat })
    }

    fun createInvoiceTaxBundle(year: Int, companyID: Long, subject: SubjectEntity): ByteArray {
        val baos = ByteArrayOutputStream()
        val zip = ZipOutputStream(baos)
        val data = findInvoices(year, companyID)
        val sumData = data.map{ InvoiceMapper.toValueObject(it, subject)}.map { it.header }
        data.forEach { putInvoiceToZip(it, zip, subject) }

        val entry = ZipEntry("report.info.txt")
        val reportData =
            ("Částka celkem: " + sumData.sumOf { it.paymentSum } + "\nČástka celkem bez DPH: " + sumData.sumOf { it.paymentSumWithoutVat }).toByteArray()
        entry.size = reportData.size.toLong()
        zip.putNextEntry(entry)
        zip.write(reportData)
        zip.closeEntry()
        zip.close()
        return baos.toByteArray()
    }

    private fun findInvoices(year: Int, companyID: Long): List<InvoiceEntity> {
        val fromDate = LocalDate.of(year - 1, 12, 1)
        val toDate = LocalDate.of(year, 11, 1)
        return invoiceRepository.findIssued(companyID, fromDate, toDate)
    }


    private fun putInvoiceToZip(invoice: InvoiceEntity, zip: ZipOutputStream, subject: SubjectEntity): Unit {
        invoiceBean.generatePDF(invoice.id!!, subject).let { (f, s) ->
            val entry = ZipEntry("$s.pdf")
            entry.size = f.size.toLong()
            zip.putNextEntry(entry)
            zip.write(f)
            zip.closeEntry()
        }
    }

    fun getAllIssuedInvoice(issuedByCompany: Long): List<DphInvoiceSumPreviewVo> {
        return dphDao.findIssuedInvoices(issuedByCompany)

    }

    fun getAllReceivedInvoice(recipientCompany: Long): List<DphInvoiceSumPreviewVo> {
        return dphDao.findReceivedInvoices(recipientCompany)

    }
}
