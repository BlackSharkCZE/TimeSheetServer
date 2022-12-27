package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import org.hibernate.annotations.Immutable
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "v_earn_history")
@Immutable
class EarnHistoryView : PanacheEntityBase(), Serializable {

    @Id
    @Column(name = "uuid")
    var uuid: UUID = UUID.randomUUID()

    @Column(name = "issuer_id")
    var issuerId: Long = 0

    @Column(name = "issue_date")
    var issueDate: LocalDate = LocalDate.MIN

    @Column(name = "payment_date")
    var paymentDate: LocalDate = LocalDate.MIN

    @Column(name = "price")
    var price: BigDecimal = BigDecimal.ZERO

    @Column(name = "total_price")
    var totalPrice: BigDecimal = BigDecimal.ZERO

    @Column(name = "recipient_id")
    var recipientId: Long = 0L

    @Column(name = "recipient_name")
    var recipientName: String = ""


}
