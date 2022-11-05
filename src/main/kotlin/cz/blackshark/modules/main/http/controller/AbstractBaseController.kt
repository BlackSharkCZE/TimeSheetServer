package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.security.SubjectSupplier
import io.quarkus.security.identity.SecurityIdentity
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.inject.Inject
import javax.ws.rs.NotAuthorizedException

abstract class AbstractBaseController {

    @Inject
    protected lateinit var securityIdentity: SecurityIdentity

    @Inject
    protected lateinit var jwtToken: JsonWebToken

    @Throws(NotAuthorizedException::class)
    fun retrieveSubject(): SubjectEntity {

        return securityIdentity.attributes[SubjectSupplier.SUBJECT_KEY] as? SubjectEntity
            ?: throw NotAuthorizedException("Subject Entity not present in SecurityIdentity")

    }

}
