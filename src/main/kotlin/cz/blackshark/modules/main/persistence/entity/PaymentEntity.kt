package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.math.BigDecimal
import java.time.LocalDate
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
@Table(name = "payments")
class PaymentEntity : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_id_seq")
    @SequenceGenerator(name = "payments_id_seq", sequenceName = "payments_id_seq", allocationSize = 1)
    var id: Long? = null

    @Column(name = "payment")
    var payment: BigDecimal = BigDecimal.ZERO

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    var companyEntity: CompanyEntity? = null

    @Column(name = "payment_date")
    var paymentDate: LocalDate? = null

}
