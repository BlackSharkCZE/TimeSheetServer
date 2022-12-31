package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
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
import javax.validation.constraints.NotNull

@Entity
@Table(name = "requisition")
class RequisitionEntity : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requisition_id_seq")
    @SequenceGenerator(name = "requisition_id_seq", sequenceName = "requisition_id_seq", allocationSize = 1)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    var company: CompanyEntity? = null

    @Column(name = "cust_number")
    @NotNull
    var customerNumber: String? = null

    @Column(name = "start_date")
    @NotNull
    var startDate: LocalDate? = null

    @Column(name = "end_date")
    @NotNull
    var endDate: LocalDate? = null

    @Column(name = "note")
    @NotNull
    var note: String? = null

    @Column(name = "path")
    @NotNull
    var path: String? = null

    @Column(name = "orig_name")
    @NotNull
    var origFileName: String? = null

}
