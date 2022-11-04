package cz.blackshark.modules.main.security

import io.quarkus.oidc.AccessTokenCredential
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.SecurityIdentityAugmentor
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class SubjectAugmentor : SecurityIdentityAugmentor {

    @Inject
    private lateinit var jwtParser: JWTParser

    override fun augment(identity: SecurityIdentity, context: AuthenticationRequestContext): Uni<SecurityIdentity> {
        if (identity.isAnonymous) {
            val a=1
            return Uni.createFrom().item(identity)
        } else {
            if (identity is QuarkusSecurityIdentity) {
                val c = identity.credentials.first()
                if (c is AccessTokenCredential) {
                    val token = c.token
                }
            }
            return Uni.createFrom().item(identity)
        }
    }
}
