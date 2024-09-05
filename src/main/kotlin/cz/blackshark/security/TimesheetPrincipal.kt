package cz.blackshark.security

import io.quarkus.security.runtime.QuarkusPrincipal

class TimesheetPrincipal(name: String) : QuarkusPrincipal(name) {
    var subjectValue: String? = null
    var roles: List<String> = emptyList()
    var companyId: Long? = null
}
