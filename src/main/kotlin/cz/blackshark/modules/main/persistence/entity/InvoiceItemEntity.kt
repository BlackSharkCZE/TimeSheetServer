package cz.blackshark.modules.main.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "invoice_item")
open class InvoiceItemEntity : PanacheEntityBase(), Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_item_id_seq")
	@SequenceGenerator(name = "invoice_item_id_seq", sequenceName = "invoice_item_id_seq", allocationSize = 1)
	var id: Long? = null

	@Column(name = "vat", columnDefinition = "numeric(9,2)")
	var vat:BigDecimal = BigDecimal.valueOf(0.22)

	@Column(name = "price", columnDefinition = "numeric(9,2)")
	@NotNull
	var price:BigDecimal = BigDecimal.ZERO

	@Column(name = "total_price", columnDefinition = "numeric(9,2)")
	@NotNull
	var totalPrice:BigDecimal = BigDecimal.ZERO

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_int_id")
	@JsonIgnore
	var invoiceEntity: InvoiceEntity? = null

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "requisition_id")
	var requisition: RequisitionEntity? = null

	@Column(name = "note", columnDefinition = "text")
	var note: String? = null

}
