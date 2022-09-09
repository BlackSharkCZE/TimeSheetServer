package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "remote_write_timestamp")
class RemoteWriteTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "remote_write_timestamp_seq")
    @SequenceGenerator(
        name = "remote_write_timestamp_seq",
        sequenceName = "remote_write_timestamp_seq",
        allocationSize = 1
    )
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheet_id")
    var timeline: TimelineEntity? = null

    @Column(name = "writer_name")
    var writerName: String? = null

    @Column(name = "write_timestamp")
    var writeTimestamp: ZonedDateTime? = null

    @Column(name = "success")
    var success: Boolean? = null
}
