package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.persistence.dao.ReportDao
import cz.blackshark.modules.main.persistence.dao.TimelineDao
import cz.blackshark.modules.main.dto.ReportVo
import java.time.LocalDate
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ReportController {

	@Inject
	lateinit var reportDao: ReportDao

	@Inject
	lateinit var timelineDao: TimelineDao


	@GET
	@Path("/work-time")
	@Produces(MediaType.APPLICATION_JSON)
	fun getWorkTime(@QueryParam("month") month: Int?,
	                @QueryParam("year") year: Int?,
	                @QueryParam("company") companyId: Long?): ReportVo {
		return reportDao.getReport(year, month, companyId)

	}

	@GET
	@Path("/min-year-in-timeline")
	@Produces(MediaType.APPLICATION_JSON)
	fun getMinYearInTimeline(): RestResponse<Map<String, Any?>> {
		val x = timelineDao.minYearInTimeline()
		return RestResponse(true, null, mapOf("value" to x))
	}

	@GET
	@Path("/day-max-time")
	@Produces(MediaType.APPLICATION_JSON)
	fun getMaxTimeInDay(@QueryParam("date") date: LocalDate): RestResponse<Map<String, Any?>> {
		val x = timelineDao.maxTimeInDay(date)
		return RestResponse(true, null, mapOf("value" to x))
	}


}
