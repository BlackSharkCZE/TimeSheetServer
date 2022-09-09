package cz.blackshark.modules.main.beans.remotewriters

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.gemts.service.GemTsBean
import cz.blackshark.modules.jira.beans.JiraBean
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.RemoteWriteRepository
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@Logged
open class GemTimesheetWriter: AbstractTimelineRemoteWriter() {

    @Inject
    lateinit var gemTsBean: GemTsBean

    @Inject
    lateinit var repository: RemoteWriteRepository

    override fun getRemoteWriteRepository(): RemoteWriteRepository = repository

    override fun getName(): String  = "GEM_TIMESHEET"

    override fun getWriterFunc(): (timeline: TimelineEntity, settings: RemoteWriteSettingsEntity) -> RestResponse<Unit> = gemTsBean::logIntoTimesheet
}
