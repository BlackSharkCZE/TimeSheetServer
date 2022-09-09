package cz.blackshark.modules.main.persistence.entity

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.time.LocalDate
import javax.persistence.*
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
	var customerNumber:String? = null

	@Column(name = "start_date")
	@NotNull
	var startDate:LocalDate? = null

	@Column(name = "end_date")
	@NotNull
	var endDate:LocalDate? = null

	@Column(name = "note")
	@NotNull
	var note:String? = null

	@Column(name = "path")
	@NotNull
	var path:String? = null

	@Column(name = "orig_name")
	@NotNull
	var origFileName:String? = null

}
