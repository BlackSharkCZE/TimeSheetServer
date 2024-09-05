package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "company")
/*@NamedQueries(
)*/
@NamedQuery(name="CompanyEntity.findPrimary", query = "from CompanyEntity where primaryAccount = true")
open class CompanyEntity : PanacheEntityBase(), Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
	@SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1)
	var id: Long? = null

	@NotNull
	@Basic(fetch =  FetchType.EAGER)
	var ic: String? = null

	@Column(name = "company_name")
	@NotNull
	var companyName: String? = null

	var okres: String? = null

	@NotNull
	var obec: String? = null

	@Column(name = "cast_obce")
	@NotNull
	var castObce: String? = null

	@NotNull
	var ulice: String? = null

	@Column(name = "cislo_domu")
	@NotNull
	var cisloDomu: String? = null

	@NotNull
	var ps: String? = null

	var dic: String? = null

	@Column(name = "platce_dph")
	@NotNull
	var platceDph: Boolean = true

	@NotNull
	var email: String? = null

	@Column(name = "phone_number")
	@NotNull
	var phoneNumber: String? = null

	@Column(name = "primary_account")
	@NotNull
	var primaryAccount: Boolean = false

	@Column(name = "bank_account_number")
	@NotNull
	var bankAccountNumber: String? = null

}
