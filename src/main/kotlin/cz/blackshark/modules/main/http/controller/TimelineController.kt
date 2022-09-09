package cz.blackshark.modules.main.http.controller

import cz.blackshark.model.TimeOverlapVo
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.RemoteWriteSettingsBean
import cz.blackshark.modules.main.beans.RemoteWriterBean
import cz.blackshark.modules.main.beans.TimelineBean
import cz.blackshark.modules.main.domains.OperationResult
import cz.blackshark.modules.main.dto.EarningVo
import cz.blackshark.modules.main.dto.TimelineVo
import cz.blackshark.modules.main.persistence.RepositoryResult
import cz.blackshark.modules.main.persistence.dao.ReportDao
import cz.blackshark.modules.main.persistence.dao.TimelineDao
import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.repository.ProjectRepository
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.vertx.core.json.JsonObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.streams.toList


@Path("/timeline")
class TimelineController {

    @Inject
    lateinit var timelineRepository: TimelineRepository

    @Inject
    lateinit var timelineDao: TimelineDao

    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var reportDao: ReportDao

    @Inject
    lateinit var timelineBean: TimelineBean

    @Inject
    lateinit var remoteWriterBean: RemoteWriterBean

    @Inject
    lateinit var remoteWriteSettingsBean: RemoteWriteSettingsBean

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("remote-write/{timelineId}")
    fun remoteWrite(@PathParam("timelineId") timelineId: Long): RestResponse<Map<String, RestResponse<RemoteWriteTimestampEntity?>?>> {
        return RestResponse(true, null, remoteWriterBean.write(timelineId), null)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun saveTimeline(@Valid timeline: TimelineVo): OperationResult {
        return timelineBean.saveUpdate(timeline)
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("{id}")
    fun deleteTimeline(@PathParam("id") timelineId: Long): Response {
        val status = timelineRepository.deleteById(timelineId)
        return Response
            .ok(JsonObject().put("success", status).put("id", timelineId).toString())
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("time-overlap/{fromTime}/{toTime}")
    fun timeOverlap(
        @PathParam("fromTime") fromTime: LocalDateTime, //RestLocalDateTime,
        @PathParam("toTime") toTime: LocalDateTime //RestLocalDateTime
    ): TimeOverlapVo {
        return timelineDao.getOverlapInfo(fromTime, toTime)
//		return timelineDao.getOverlapInfo(fromTime.value, toTime.value)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateTimeline(@Valid timeline: TimelineVo): OperationResult {
        return timelineBean.saveUpdate(timeline)
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getByID(@PathParam("id") id: Long): RepositoryResult<TimelineVo> {
        val data = timelineRepository.findById(id)
        return RepositoryResult(listOf(TimelineVo(data,remoteWriteSettingsBean.getSettingsForNote(data.note))), 1, 1)
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllTimeline(
        @QueryParam("from") fromDate: LocalDate?,
        @QueryParam("to") toDate: LocalDate?,
        @QueryParam("page") @DefaultValue("1") page: Int,
        @QueryParam("pageSize") @DefaultValue("200") pageSize: Int
    ): RepositoryResult<TimelineVo> {
        val selectFromDate = fromDate ?: LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val selectToDate = toDate ?: LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        val data = timelineRepository.findAllWithDate(selectFromDate, selectToDate, page - 1, pageSize)
        return RepositoryResult(data.items.map { TimelineVo(it,remoteWriteSettingsBean.getSettingsForNote(it.note)) }, data.pageSize, data.pages)
    }

    @GET
    @Path("/work-time")
    @Produces(MediaType.APPLICATION_JSON)
    fun getWorkTime(
        @QueryParam("from") fromDate: LocalDate?,
        @QueryParam("to") toDate: LocalDate?,
        @QueryParam("company") companyId: Long?
    ): String {

        val selectFromDate = fromDate ?: LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val selectToDate = toDate?.plusDays(1) ?: LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())

        val time = reportDao.getReport(selectFromDate, selectToDate, companyId)
        return """{
			"value": $time
			}
		""".trimMargin()

    }

    @GET
    @Path("/earning")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEarning(
        @QueryParam("from") fromDate: LocalDate?,
        @QueryParam("to") toDate: LocalDate?
    ): List<EarningVo> {

        val selectFromDate = fromDate ?: LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val selectToDate = toDate ?: LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        return reportDao.getEarning(selectFromDate, selectToDate)
    }

    @GET
    @Path("/daily-earning")
    @Produces(MediaType.APPLICATION_JSON)
    fun getDailyEarning(
        @QueryParam("day") day: LocalDate,
        @QueryParam("company") companyId: Long?
    ): JsonObject {

        val res = timelineDao.dailyEarning(day, companyId)
        if (res == null) {
            throw NotFoundException()
        } else {
            return res
        }
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    fun processTest(@QueryParam("from") fromDate: LocalDate?): String {
        return "Here we are! $fromDate"
    }


    @GET
    @Path("/by-project/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllTimelineByProject(@PathParam("id") id: Long): List<TimelineVo> {
        return timelineRepository.stream("projectEntity.id = :id", mapOf<String, Any>("id" to id))
            .map { e -> TimelineVo(e,remoteWriteSettingsBean.getSettingsForNote(e.note)) }.toList()
    }

}
