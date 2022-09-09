package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.remotewriters.AbstractTimelineRemoteWriter
import cz.blackshark.modules.main.dto.ProjectVo
import cz.blackshark.modules.main.persistence.entity.ProjectEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.ProjectRepository
import io.vertx.core.json.JsonArray
import org.slf4j.LoggerFactory
import javax.enterprise.inject.Instance
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/project")
class ProjectController {

    companion object {
        val logger = LoggerFactory.getLogger(ProjectController::class.java)
    }

    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var companyRepository: CompanyRepository

    @Inject
    lateinit var writers: Instance<AbstractTimelineRemoteWriter>

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllProject(): List<ProjectEntity> {
        return projectRepository.listAll()
    }

    @GET
    @Path("by-company/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllProjectByCompany(@PathParam("id") id: Long): List<ProjectEntity> {
        return projectRepository.list("company.id = ?1", id)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun save(@Valid projectVo: ProjectVo): ProjectVo {
        val company = companyRepository.findById(projectVo.companyID)
        if (company == null) {
            logger.warn("Can not find Company with ID: ${projectVo.companyID} to save Project!")
            throw NotFoundException("Company with ID ${projectVo.companyID} not found!")
        } else {
            val pE = ProjectEntity()
            pE.company = company
            pE.name = projectVo.name
            projectRepository.persist(pE)
            return projectVo.copy(id = pE.id)
        }

    }

    @GET
    @Path("remote-writer-list")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRemoteWriterList(): JsonArray = JsonArray(writers.map { it.getName() })


}
