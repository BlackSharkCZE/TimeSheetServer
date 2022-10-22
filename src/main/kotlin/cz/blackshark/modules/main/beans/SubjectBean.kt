package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.SubjectRepository
import io.quarkus.cache.CacheResult
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class SubjectBean @Inject constructor(private val subjectRepository: SubjectRepository) {

    @CacheResult(cacheName = "user-by-subject-id")
    fun findByRemoteId(remoteId: String): SubjectEntity? {
        return subjectRepository.find("subject", remoteId).firstResultOptional<SubjectEntity>().orElse(null)
    }

    @Transactional
    fun createNewSubject(remoteId: String): SubjectEntity {
        val subjectEntity = SubjectEntity(null, remoteId)
        subjectRepository.persist(subjectEntity)
        return subjectEntity
    }

    fun findOrCreateSubject(remoteId: String): SubjectEntity {
        return findByRemoteId(remoteId) ?: createNewSubject(remoteId)
    }
}
