package cz.blackshark.modules.gemts.http.client

import com.fasterxml.jackson.core.type.TypeReference
import cz.blackshark.modules.gemts.converter.GemTimesheetConverter
import cz.blackshark.modules.gemts.dto.WriteWorkLogRequestVo
import cz.blackshark.modules.jira.customizations.JiraClientObjectMapper
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class GemTsWriteMapperTest {

    @Inject
    private lateinit var timelineRepository: TimelineRepository

    @Inject
    private lateinit var converter: GemTimesheetConverter

    val data = """  [
  {
    "id": 373204,
    "projectId": 3931,
    "rootProjectId": 2912,
    "tagId": 0,
    "employeeId": "jpejsa",
    "dateStringFrom": "2021-12-10",
    "dateStringTo": "2021-12-10",
    "hoursFrom": "09:00",
    "hoursTo": "09:45",
    "pause": 0,
    "hours": 0.75,
    "onCall": false,
    "description": "...",
    "status": 0,
    "blockedEditing": false,
    "extId": null
  }
]"""

@Test
fun mapperTest() {
    val objectMapper = JiraClientObjectMapper().getContext(null)

    val strData = objectMapper.readValue(data, object: TypeReference<List<WriteWorkLogRequestVo>>(){})
    println(strData)
}



}
