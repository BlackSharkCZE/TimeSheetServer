package cz.blackshark.modules.main.persistence.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import org.eclipse.microprofile.graphql.DateFormat
import java.io.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "todo")
open class TodoEntity : PanacheEntityBase(), Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_id_seq")
	@SequenceGenerator(name = "todo_id_seq", sequenceName = "todo_id_seq", allocationSize = 1)
	var id: Long? = null

	@Column(name="created")
	@DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	var created: ZonedDateTime? = null

	@Column(name = "todo")
	@NotNull
	var todo: String? = null

	@Column(name="done")
	@DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	var done: ZonedDateTime? = null
}
