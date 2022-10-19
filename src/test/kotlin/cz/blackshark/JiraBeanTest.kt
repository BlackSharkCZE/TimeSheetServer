package cz.blackshark

import cz.blackshark.config.JiraConfig
import cz.blackshark.modules.jira.beans.JiraBean
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@QuarkusTest
@Disabled
class JiraBeanTest {

    @Inject
    lateinit var jiraConfig: JiraConfig

    @Inject
    lateinit var jiraBean: JiraBean

    @Test
    fun findWorkLogTest() {
        val data = jiraBean.findWorkLogForCurrentUser(LocalDate.now().withDayOfMonth(1),
        LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
        Assertions.assertNotNull(data)
        data.forEach {
            Assertions.assertEquals(jiraConfig.accountId(), it.author.accountId)
        }
    }

}
