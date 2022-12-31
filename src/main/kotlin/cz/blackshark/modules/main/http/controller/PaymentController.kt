package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.ImportDataBean
import cz.blackshark.modules.main.beans.PaymentBean
import cz.blackshark.modules.main.dto.GenericImportRequest
import cz.blackshark.modules.main.dto.PaymentVo
import io.quarkus.security.Authenticated
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("payment")
@Authenticated
class PaymentController : AbstractBaseController() {

    @Inject
    private lateinit var importDataBean: ImportDataBean

    @Inject
    private lateinit var paymentBean: PaymentBean

    @Path("import/outgoing-payments")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun importOutgoingPayments(@Valid @MultipartForm importData: GenericImportRequest): Response {
        importDataBean.importOutgoingPayments(importData)
        return Response.noContent().build()
    }

    @Path("list")
    @GET
    fun getAllPayments(@QueryParam("company") companyId: Long?): List<PaymentVo> {
        return paymentBean.getAllPayments(companyId)
    }

    @POST
    @Transactional
    fun createPayment(@Valid input: PaymentVo): RestResponse<PaymentVo> {
        return paymentBean.createNewPayment(input)
    }

}
