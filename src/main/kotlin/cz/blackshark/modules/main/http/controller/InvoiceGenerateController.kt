package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.InvoiceBean
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.security.SubjectSupplier
import io.quarkus.security.identity.SecurityIdentity
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@ApplicationScoped
class InvoiceGenerateController {

    @Inject
    private lateinit var securityIdentity: SecurityIdentity

    @Inject
    private lateinit var invoiceBean: InvoiceBean

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate")
    @Throws(NotAuthorizedException::class)
    @Transactional
    fun generateInvoice(
        @QueryParam("issueDate") issueDate: LocalDate?,
        @QueryParam("companyID") companyID: Long
    ): RestResponse<InvoiceEntity> {
        val subject = retrieveSubject()
        val _issueDate = issueDate ?: LocalDate.now()

        val entity = invoiceBean.generateInvoice(subject, companyID, _issueDate)

        return RestResponse(true, null, entity, null)
    }


    @Throws(NotAuthorizedException::class)
    private fun retrieveSubject(): SubjectEntity {
        return securityIdentity.attributes[SubjectSupplier.SUBJECT_KEY] as? SubjectEntity
            ?: throw NotAuthorizedException("Subject Entity not present in SecurityIdentity")

    }

}
