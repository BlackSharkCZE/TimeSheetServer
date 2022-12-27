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
@Table(name = "v_balance")
@Immutable
class BalanceView : PanacheEntityBase(), Serializable {

    @Id
    @Column(name = "uuid")
    var uuid: UUID = UUID.randomUUID()

    @Column(name = "issuer_id")
    var issuerId: Long = 0

    @Column(name = "company_id")
    var companyId: Long = 0

    @Column(name = "_date")
    var month: LocalDate = LocalDate.MIN

    @Column(name = "income")
    var income: BigDecimal = BigDecimal.ZERO

    @Column(name = "outcome")
    var outcome: BigDecimal = BigDecimal.ZERO

    @Column(name = "balance")
    var balance: BigDecimal = BigDecimal.ZERO

}
