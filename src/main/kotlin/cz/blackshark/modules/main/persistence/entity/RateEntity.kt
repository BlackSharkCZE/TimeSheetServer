package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "rate")
class RateEntity : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_id_seq")
    @SequenceGenerator(name = "rate_id_seq", sequenceName = "rate_id_seq", allocationSize = 1)
    var id: Long? = null

    var rate: BigDecimal = BigDecimal.ZERO

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    var companyEntity: CompanyEntity? = null

    @Column(name = "valid_since")
    var validSince: ZonedDateTime = ZonedDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    var subject: SubjectEntity? = null
}
