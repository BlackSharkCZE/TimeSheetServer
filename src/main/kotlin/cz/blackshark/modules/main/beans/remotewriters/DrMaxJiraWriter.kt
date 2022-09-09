package cz.blackshark.modules.main.beans.remotewriters

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.jira.beans.JiraBean
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.RemoteWriteRepository
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@Logged
open class DrMaxJiraWriter: AbstractTimelineRemoteWriter() {

    @Inject
    lateinit var jiraBean: JiraBean

    @Inject
    lateinit var repository: RemoteWriteRepository


    override fun getName(): String = "DRMAX_JIRA"

    override fun getRemoteWriteRepository(): RemoteWriteRepository = repository

    override fun getWriterFunc(): (timeline: TimelineEntity,settings: RemoteWriteSettingsEntity) -> RestResponse<Unit> = jiraBean::logIntoJira

}
