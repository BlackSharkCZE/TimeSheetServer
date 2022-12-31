package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.beans.remotewriters.DrMaxJiraWriter
import cz.blackshark.modules.main.beans.remotewriters.GemTimesheetWriter
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.quarkus.test.Mock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import javax.inject.Inject

@QuarkusTest
@Disabled
class RemoteWriterTest {

    @Inject
    lateinit var remoteWriterBean: RemoteWriterBean

    @InjectMock
    lateinit var gemTsWriter: GemTimesheetWriter

    @InjectMock
    lateinit var drMaxJiraWriter: DrMaxJiraWriter

    @InjectMock
    lateinit var timelineRepository: TimelineRepository


    @Test
    fun testRemoteJiraWriter() {

        whenever(timelineRepository.findById(any())).then {
            TimelineEntity().apply { note = "IPF-1133 - Daily Scrumm" }
        }


        remoteWriterBean.write(2)


    }

}
