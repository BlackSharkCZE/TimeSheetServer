package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.RequisitionBean
import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.commons.model.AbstractTableResponse
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.inject.Inject
import javax.transaction.Transactional
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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@Transactional
	fun save(body: MultipartFormDataInput): RestResponse<Long> {
		logger.debug("Save Requisition invoked!")
		val vRes = requisitionBean.validateIncomingMultipart(body)
		return if (vRes.first == null) {
			val newID = requisitionBean.processValidIncomingMultipart(body, vRes.second!!)
			if (newID != null) {
				RestResponse(true, "Requisition Saved!", newID)
			} else {
				RestResponse(false, "Save requisition failed!", -1L)
			}
		} else {
			RestResponse(false, vRes.first!!.message, -1)
		}
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	fun all(): AbstractTableResponse<RequisitionEntity> {
		return AbstractTableResponse(
			true, "OK",
			listOf("id", "company", "customerNumber", "startDate", "endDate", "note", "path"),
			requisitionRepository.findAll().list<RequisitionEntity>()
		)
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
