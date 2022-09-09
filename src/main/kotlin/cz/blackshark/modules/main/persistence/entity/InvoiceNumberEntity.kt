package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "invoice_number")
@Entity
open class InvoiceNumberEntity : PanacheEntityBase() {

	@Id
	@Column(name = "company_id")
	var companyID:Long? = null

	@Column(name = "prefix")
	var prefix:Long = 1

	@Column(name = "current_val")
	var currentValue:Long = 1

}
