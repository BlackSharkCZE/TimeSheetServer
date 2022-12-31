package cz.blackshark.modules.main.http.controller

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.RequisitionBean
import cz.blackshark.modules.main.dto.RequisitionRequest
import cz.blackshark.modules.main.dto.RequisitionVo
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import io.quarkus.panache.common.Sort
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/requisition")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class RequisitionController @Inject constructor(
    private val appConf: ApplicationConfig,
    private val requisitionBean: RequisitionBean,
    private val requisitionRepository: RequisitionRepository
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
    fun createNewRequisition(@Valid @MultipartForm requisitionRequest: RequisitionRequest): RestResponse<Long> {
        logger.debug("Save Requisition invoked!")
        return RestResponse(true, "Requisition Saved!", requisitionBean.save(requisitionRequest))
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun getAllRequisition(): List<RequisitionVo> {
        return requisitionRepository.findAll(
            Sort.by("company.companyName").and("start_date", Sort.Direction.Descending)
        ).list<RequisitionEntity>().map {
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


    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/{id}/content")
    fun content(@PathParam("id") requisitionId: Long): Response {
        val rq = requisitionRepository.findById(requisitionId)
        val path = Paths.get(appConf.fileStoragePath() + "/" + rq.path)
        return if (path.toFile().exists()) {
            Response.ok(Files.newInputStream(path, StandardOpenOption.READ))
                .header("Content-Disposition", "attachment; filename=\"${rq.origFileName}\"")
                .build();
        } else {
            logger.warn("Requested requisition $requisitionId does not exists on path $path")
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

}
