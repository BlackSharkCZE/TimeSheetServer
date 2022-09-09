package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.InvoiceBean
import cz.blackshark.modules.main.beans.InvoiceUploadBean
import cz.blackshark.modules.main.beans.JasperReportGenerator
import cz.blackshark.modules.main.dto.InvoiceSummaryPreviewVo
import cz.blackshark.modules.main.persistence.dao.InvoiceDao
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import org.jboss.resteasy.spi.HttpResponseCodes
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/invoice")
class InvoiceController @Inject constructor(
    val logger: org.jboss.logging.Logger,
    val invoiceRepository: InvoiceRepository,
    val companyRepository: CompanyRepository,
    val invoiceDao: InvoiceDao,
    val invoiceBean: InvoiceBean,
    val jasperReportGenerator: JasperReportGenerator,
    val invoiceDetailController: InvoiceDetailController,
    val invoiceUploadBean: InvoiceUploadBean
) {

    @Path("detail")
    fun getInvocieDetail(): InvoiceDetailController {
        return invoiceDetailController
    }

    @GET
    @Path("{invoiceID: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getInvoice(@PathParam("invoiceID") invoiceID: Long): InvoiceEntity {
        return invoiceRepository.findById(invoiceID)
    }

    @GET
    @Path("{invoiceID: \\d+}/pdf")
    fun generatePDF(@PathParam("invoiceID") invoiceID: Long): Response {
        val res = invoiceBean.generatePDF(invoiceID)
        return Response.ok(res.first, MediaType.APPLICATION_OCTET_STREAM_TYPE)
            .header("Content-Disposition", "attachment; filename=${res.second}.pdf")
            .build()
    }

    @GET
    @Path("{invoiceID: \\d+}/html")
    fun generateHTML(@PathParam("invoiceID") invoiceID: Long): Response {
        val invoiceEntity = try {
            invoiceRepository.findById(invoiceID)
        } catch (e: Exception) {
            throw NotFoundException("Can not find Invoice with ID $invoiceID")
        }

        return Response.ok(
            jasperReportGenerator.generateInvoiceHTML(invoiceEntity),
            MediaType.APPLICATION_OCTET_STREAM_TYPE
        )
            .header("Content-Disposition", "attachment; filename=${invoiceEntity.number}.html")
            .build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllInvoices(): List<InvoiceEntity> {
        val listAll = invoiceRepository.listAll()
        return listAll
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun saveInvoice(@Valid invoice: InvoiceEntity): InvoiceEntity {
        invoice.issuerCompany = companyRepository.findById(invoice.issuerCompany?.id)
        invoice.recipientCompany = companyRepository.findById(invoice.recipientCompany?.id)
        invoiceRepository.persistAndFlush(invoice)
        return invoiceRepository.findById(invoice.id)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/summary-preview")
    fun getSummary(
        @QueryParam("from") fromDate: LocalDate,
        @QueryParam("to") toDate: LocalDate,
        @QueryParam("companyID") companyID: Long
    ): InvoiceSummaryPreviewVo {

        val res = invoiceDao.loadInvoiceSourceData(companyID, fromDate, toDate)
        val mdHours = BigDecimal(8)
        return InvoiceSummaryPreviewVo(
            res.map { it.workDay }.distinct().size,
            res.minByOrNull { it.workDay }?.workDay,
            res.maxByOrNull { it.workDay }?.workDay,
            BigDecimal.valueOf(res.sumOf { it.workTime.toDouble() }),
            BigDecimal.valueOf(res.sumOf { it.rate.divide(mdHours).times(it.workTime).toDouble() })
        )
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate")
    fun generateInvoice(
        @QueryParam("from") fromDate: LocalDate,
        @QueryParam("to") toDate: LocalDate,
        @QueryParam("companyID") companyID: Long
    ): RestResponse<InvoiceEntity> {


        val issuer = companyRepository.findPrimaryCompany()
        val receiver = companyRepository.findById(companyID)

        if (issuer != null) {
            if (receiver != null) {
                val invoice = invoiceBean.generateInvoice(fromDate, toDate, receiver, issuer)
                if (invoice != null) {
                    return RestResponse(true, "OK", invoice)
                } else {
                    logger.error("Can not save Invoice in the database. Invoice is null")
                    throw InternalServerErrorException("Can not save Invoice in the database. Invoice is null")
                }
            } else {
                logger.warn("Company with ID $companyID not found!")
            }
        } else {
            logger.warn("Primary company as issuer not found!")
        }

        throw BadRequestException("Generate invoice failed. See logs for more details.")

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload")
    @Transactional
    fun uploadInvoice(body: MultipartFormDataInput): RestResponse<InvoiceEntity> {
        val res = invoiceUploadBean.processUpload(body)
        return if (res.isError) {
            RestResponse(false, res.errorCode, null, null)
        } else {
            RestResponse(true, "Invoice created", res.entity, HttpResponseCodes.SC_OK)
        }
    }


}
