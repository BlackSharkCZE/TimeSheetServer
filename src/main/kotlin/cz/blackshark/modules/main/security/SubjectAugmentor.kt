package cz.blackshark.modules.main.security

import cz.blackshark.modules.main.beans.SubjectBean
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.SecurityIdentityAugmentor
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject


@ApplicationScoped
class SubjectAugmentor : SecurityIdentityAugmentor {

    @Inject
    private lateinit var subjectSupplier: SubjectSupplier

    override fun augment(identity: SecurityIdentity, context: AuthenticationRequestContext): Uni<SecurityIdentity> {
        subjectSupplier.identity = identity
        return context.runBlocking(subjectSupplier)
    }
}
