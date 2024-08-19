package cz.blackshark.modules.main.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.PasswordType
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "UserEntity")
@Table(name = "user_entity")
@UserDefinition
open class UserEntity {

    @Username
    @Id
    var login: String? = null

    @Column(name = "password")
    @Password(value = PasswordType.MCF)
    @JsonIgnore
    lateinit var password: String

    @Column(name = "role")
    @Roles
    val role: String? = null

    @Column(name = "company_id")
    var companyId: Long? = null

    @Column(name = "subject_id")
    var subjectId: String? = null
}
