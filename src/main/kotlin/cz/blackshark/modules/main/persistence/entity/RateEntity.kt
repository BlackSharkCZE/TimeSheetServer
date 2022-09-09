package cz.blackshark.modules.main.persistence.entity

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "rate")
class RateEntity : PanacheEntityBase() {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_id_seq")
	@SequenceGenerator(name = "rate_id_seq", sequenceName = "rate_id_seq", allocationSize = 1)
	var id: Long? = null

	var rate:BigDecimal = BigDecimal.ZERO

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	var companyEntity: CompanyEntity? = null

	@Column(name= "valid_since")
	var validSince:ZonedDateTime = ZonedDateTime.now()
}
