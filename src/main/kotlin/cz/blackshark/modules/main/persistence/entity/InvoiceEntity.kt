package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "invoice")
@Entity
open class InvoiceEntity : PanacheEntityBase(), Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
	@SequenceGenerator(name = "invoice_id_seq", sequenceName = "invoice_id_seq", allocationSize = 1)
	var id: Long? = null

	@Column(name = "i_number")
	var number:String? = null

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "issuer_id")
	@NotNull
	var issuerCompany: CompanyEntity? = null

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "recipient_id")
	@NotNull
	var recipientCompany: CompanyEntity? = null

	@Column(name = "issue_date")
	@NotNull
	var issueDate:LocalDate? = null

	@Column(name = "payment_date")
	@NotNull
	var paymentDate:LocalDate? = null

	@Column(name = "vat_payment_date")
	var vatPaymentDate:LocalDate? = null

	@Column(name = "store_path")
	var storePath: String? = null

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "invoiceEntity", orphanRemoval = true)
	var items:MutableList<InvoiceItemEntity> = mutableListOf()

}
