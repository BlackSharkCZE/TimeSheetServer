package cz.blackshark.modules.main.persistence.entity

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import javax.persistence.*

@Entity
@Table(name = "project")
class ProjectEntity : PanacheEntityBase() {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
	@SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq", allocationSize = 1)
	var id: Long? = null

	var name:String? = null

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	var company: CompanyEntity? = null
}
