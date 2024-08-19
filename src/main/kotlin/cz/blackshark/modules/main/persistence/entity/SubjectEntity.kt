package cz.blackshark.modules.main.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "subject")
data class SubjectEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_id_seq")
    @SequenceGenerator(name = "subject_id_seq", allocationSize = 1, sequenceName = "subject_id_seq")
    var id: Long?,
    val subject: String?,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "last_name")
    val lastName: String
) {

    constructor(): this(null, null, "", "")

}
