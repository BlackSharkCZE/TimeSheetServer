package cz.blackshark.modules.main.persistence.entity

import cz.blackshark.modules.main.persistence.Hql
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "timeline")
@NamedQueries(
    NamedQuery(name = "TimelineEntity.findAllWithDate", query = Hql.FIND_TIMELINE_WITH_DATE)
)
open class TimelineEntity : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeline_id_seq")
    @SequenceGenerator(name = "timeline_id_seq", sequenceName = "timeline_id_seq", allocationSize = 1)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    var projectEntity: ProjectEntity? = null

    @Column(name = "from_time")
    var fromTime: ZonedDateTime = ZonedDateTime.now()


    @Column(name = "to_time")
    var toTime: ZonedDateTime = ZonedDateTime.now()

    var pause: Int = 0

    var note: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_item_id")
    var invoiceItem: InvoiceItemEntity? = null

    @Column(name = "start_work_date")
    var startWorkDate: LocalDate? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "timeline", orphanRemoval = true)
    var remoteWriteTimestamp: MutableList<RemoteWriteTimestampEntity> = mutableListOf()

    @PrePersist
    fun prepersist() {
        startWorkDate = fromTime.toLocalDate()
    }

}
