package cz.blackshark.modules.main.security

import cz.blackshark.modules.main.beans.SubjectBean
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import java.util.function.Supplier
import javax.enterprise.context.Dependent
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject
import javax.transaction.Transactional


@Dependent
class SubjectSupplier : Supplier<SecurityIdentity> {

    companion object {
        const val SUBJECT_KEY = "SUBJECT"
    }

    @Inject
    private lateinit var subjectBean: SubjectBean

    var identity: SecurityIdentity? = null

    @ActivateRequestContext
    @Transactional
    override fun get(): SecurityIdentity {
        return identity?.let { it: SecurityIdentity ->
            if (it.isAnonymous) {
                identity
            } else {
                val subjectMap = mutableMapOf<String, Any>()
                if (it.principal is OidcJwtCallerPrincipal) {
                    val strSubject = (it.principal as OidcJwtCallerPrincipal).claims.subject
                    val subject = subjectBean.findByRemoteId(strSubject)
                    subjectMap[SUBJECT_KEY] = subject
                }
                val builder = QuarkusSecurityIdentity.builder()
                    .setPrincipal(it.principal)
                    .addAttributes(it.attributes + subjectMap)
                    .addCredentials(it.credentials)
                    .addRoles(it.roles)
                return builder.build()

            }
        } ?: QuarkusSecurityIdentity.builder().setAnonymous(true).build()
    }


}
