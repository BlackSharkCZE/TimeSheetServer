package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.exceptions.TsException
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.SubjectRepository
import cz.blackshark.security.TimesheetPrincipal
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.SecurityContext

@ApplicationScoped
class PrincipalService(
    private val subjectRepository: SubjectRepository,
) {
    fun <T> withTimesheetPrincipal(
        securityContext: SecurityContext,
        callback: (TimesheetPrincipal) -> T,
    ): T {
        return (securityContext.userPrincipal as? TimesheetPrincipal)?.let {
            callback(it)
        } ?: throw SecurityException("Invalid principal in security context")
    }

    fun <T> withTimesheetPrincipalSubject(
        securityContext: SecurityContext,
        callback: (TimesheetPrincipal, String) -> T,
    ): T {
        return withTimesheetPrincipal(securityContext) {
            it.subjectValue?.let { subject ->
                callback(it, subject)
            } ?: throw TsException("Subject value not present in principal [${it.name}]", null)
        }
    }


    fun <T> withTimesheetPrincipalAndSubjectEntity(securityContext: SecurityContext, callback: (TimesheetPrincipal, SubjectEntity) -> T): T {
        return withTimesheetPrincipalSubject(securityContext) { principal, subject ->
            subjectRepository.findByValue(subject)?.let {
                callback(principal, it)
            } ?: throw TsException("Subject not found", null)
        }
    }

}
