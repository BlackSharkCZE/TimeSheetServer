package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.dto.NewPassword
import cz.blackshark.modules.main.exceptions.NewPasswordException
import cz.blackshark.modules.main.exceptions.TsException
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.logging.Log
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserBean(
    private val userRepository: UserRepository,
) {

    fun setNewPassword(subject: SubjectEntity, password: NewPassword) {

        subject.subject?.let { sub ->

            if (sub != password.subject) {
                throw NewPasswordException("Invalid subject")
            }

            if (password.oldPassword == password.newPassword) {
                throw NewPasswordException("Old and new password are the same")
            }
            userRepository.findBySubject(sub)?.let { ent ->
                val newHash = BcryptUtil.bcryptHash(password.newPassword.trim())
                if (!BcryptUtil.matches(password.oldPassword, ent.password)) {
                    throw NewPasswordException("Old password does not match with current password")
                }

                ent.password = newHash
                userRepository.persist(ent)
                Log.infof("Password changed for user %s", ent.login)
            } ?: throw NewPasswordException("UserEntity with subject $subject not found")
        } ?: throw TsException("Subject value can not be null: [$subject]", null)
    }
}
