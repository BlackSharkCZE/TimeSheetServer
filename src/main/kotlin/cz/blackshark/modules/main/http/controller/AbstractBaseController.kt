package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import io.quarkus.security.identity.SecurityIdentity
import javax.inject.Inject
import javax.ws.rs.NotAuthorizedException

abstract class AbstractBaseController {

    @Inject
    protected lateinit var securityIdentity: SecurityIdentity

    @Throws(NotAuthorizedException::class)
    fun retrieveSubject(): SubjectEntity {

        return securityIdentity.attributes["SUBJECT"] as? SubjectEntity
            ?: throw NotAuthorizedException("Subject Entity not present in SecurityIdentity")
    }
}
