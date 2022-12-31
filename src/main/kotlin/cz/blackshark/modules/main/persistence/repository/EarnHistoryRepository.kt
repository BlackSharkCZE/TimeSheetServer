package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.EarnHistoryView
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class EarnHistoryRepository : PanacheRepositoryBase<EarnHistoryView, UUID> {
    fun findByIssuer(issuer: Long): List<EarnHistoryView> {
        return find("issuerId", issuer).list()
    }
}
