package cz.blackshark

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.http.client.GemTimesheetClient
import cz.blackshark.modules.gemts.service.GemTsBean
import io.quarkus.test.junit.QuarkusTest
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@QuarkusTest
@Disabled
class GemTimesheetTest {

    @RestClient
    lateinit var client: GemTimesheetClient

    @Inject
    lateinit var service: GemTsBean

    @Inject
    lateinit var config: GemTimesheetConfig

    @Test
    fun getTimelistTest() {
        val data = client.workLog("2021/11")
        Assertions.assertNotNull(data)
        Assertions.assertTrue(data.isNotEmpty())
        println(data)
    }

    @Test
    fun getProjects() {
        val data = client.getRootProjects()
        Assertions.assertNotNull(data)
        Assertions.assertTrue(data.isNotEmpty())
        println(data)
    }


}
