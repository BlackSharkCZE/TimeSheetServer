package cz.blackshark

import cz.blackshark.modules.gemts.converter.GemTimesheetConverter
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import javax.inject.Inject

@QuarkusTest
class GemTimesheetConverterTest {

    @Inject
    private lateinit var gemTimesheetConverter: GemTimesheetConverter

    @Test
    fun convertTest() {
        val data = TimelineEntity().apply {
            note = "Test Note"
            pause = 30
            fromTime = ZonedDateTime.now().withHour(6)
            toTime = ZonedDateTime.now().withHour(15)
        }
        val settings = RemoteWriteSettingsEntity().apply { projectId = 1; projectRootId = 2 }

        val result = gemTimesheetConverter.convertTimeline(data, settings)

        Assertions.assertThat(result).isNotNull
        result?.let {
            Assertions.assertThat(it.hoursFrom.hour).isEqualTo(6)
            Assertions.assertThat(it.hoursTo.hour).isEqualTo(15)
        }


    }

}
