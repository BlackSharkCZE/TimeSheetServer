package cz.blackshark.modules.main.http.controller

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.PrincipalService
import cz.blackshark.modules.main.beans.RequisitionBean
import cz.blackshark.modules.main.dto.RequisitionRequest
import cz.blackshark.modules.main.dto.RequisitionVo
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import io.quarkus.security.Authenticated
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.annotation.security.RolesAllowed
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/requisition")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class RequisitionController(
    private val appConf: ApplicationConfig,
    private val requisitionBean: RequisitionBean,
    private val requisitionRepository: RequisitionRepository,
    private val principalService: PrincipalService,
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RequisitionController::class.java)
    }

    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Throws(InternalServerErrorException::class)
    @RolesAllowed("admin", "superadmin")
    fun createNewRequisition(@Valid @MultipartForm requisitionRequest: RequisitionRequest): RestResponse<Long> {
        logger.debug("Save Requisition invoked!")
        return RestResponse(true, "Requisition Saved!", requisitionBean.save(requisitionRequest))
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("admin", "superadmin")
    fun getAllRequisition(@Context securityContext: SecurityContext): List<RequisitionVo> {
        return principalService.withTimesheetPrincipalSubject(securityContext) { tp, subject ->
            requisitionRepository.findAllForCompany(tp.companyId ?: -1).map {
                RequisitionVo(
                    id = it.id!!,
                    companyId = it.company!!.id!!,
                    companyName = it.company!!.companyName!!,
                    startDate = it.startDate!!,
                    endDate = it.endDate!!,
                    note = it.note!!,
                    documentName = it.origFileName!!
                )
            }
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/{id}/content")
    @RolesAllowed("admin", "superadmin")
    fun content(@PathParam("id") requisitionId: Long): Response {
        val rq = requisitionRepository.findById(requisitionId)
        val path = Paths.get(appConf.fileStoragePath() + "/" + rq.path)
        return if (path.toFile().exists()) {
            Response.ok(Files.newInputStream(path, StandardOpenOption.READ))
                .header("Content-Disposition", "attachment; filename=\"${rq.origFileName}\"")
                .build()
        } else {
            logger.warn("Requested requisition $requisitionId does not exists on path $path")
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}
