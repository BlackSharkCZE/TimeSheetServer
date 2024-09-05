package cz.blackshark.security

import cz.blackshark.modules.main.persistence.repository.UserRepository
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.SecurityIdentityAugmentor
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.infrastructure.Infrastructure
import java.security.Principal
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TimesheetPrincipalAugmentor(
    private val userRepository: UserRepository,
) : SecurityIdentityAugmentor {

    override fun augment(identity: SecurityIdentity, context: AuthenticationRequestContext?): Uni<SecurityIdentity> {
        return Uni.createFrom().item(identity)
            .runSubscriptionOn(Infrastructure.getDefaultExecutor())
            .flatMap { currentIdentity ->
                if (currentIdentity.isAnonymous) {
                    Uni.createFrom().item(identity)
                } else {
                    val name = identity?.principal?.name ?: throw RuntimeException("Security principal name is null!")
                    userRepository.findByIdAsync(name)
                        .onItem().ifNull().failWith(SecurityException("User with login $name not found!"))
                        .onItem().transform { e ->
                            val tp = TimesheetPrincipal(name).apply {
                                subjectValue= e?.subjectId
                                roles = e?.role?.split(",") ?: emptyList()
                                companyId = e?.companyId
                            }
                            val newidentity = object : SecurityIdentity by identity {
                                override fun getPrincipal(): Principal {
                                    return tp
                                }
                            }
                            newidentity
                        }
                }
            }
    }
}
