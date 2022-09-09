package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.RemoteWriteTimestampEntity
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RemoteWriteRepository : PanacheRepositoryBase<RemoteWriteTimestampEntity, Long> {

    fun findByTimelineAndWriter(timelineID:Long, writerName: String): RemoteWriteTimestampEntity? {
        return find("timeline.id=?1 and writer_name=?2", timelineID, writerName).firstResultOptional<RemoteWriteTimestampEntity>().orElseGet { null }
    }

}
