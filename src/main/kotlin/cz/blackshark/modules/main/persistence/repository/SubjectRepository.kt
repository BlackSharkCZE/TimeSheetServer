package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SubjectRepository : PanacheRepositoryBase<SubjectEntity, Long> {

    fun findByValue(value: String): SubjectEntity? {
        return find("subject", value).firstResult()
    }
}
