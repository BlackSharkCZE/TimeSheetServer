package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.UserEntity
import io.quarkus.cache.CacheKey
import io.quarkus.cache.CacheResult
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import org.hibernate.SessionFactory
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository(
    private val sessionFactory: SessionFactory,
    private val log: Logger
    ) : PanacheRepositoryBase<UserEntity, String> {


    companion object {
        private val LOG = Logger.getLogger(UserRepository::class.java)
    }

    @CacheResult(cacheName = "user-entity")
    fun findByIdAsync(@CacheKey login: String): Uni<UserEntity?> {
        return Uni.createFrom().deferred {
            Uni.createFrom().item {
                val session = sessionFactory.openSession()
                val transaction = session.beginTransaction()
                try {
                    Log.debugf("Load UserEntity $login from database - DEBUG LEVEL")
                    Log.infof("Load UserEntity $login from database - INFO LEVEL")

                    log.debugf("Load UserEntity $login from database - DEBUG LEVEL - INJECTED")
                    log.infof("Load UserEntity $login from database - INFO LEVEL - INJECTED")

                    log.debugf("Load UserEntity $login from database - DEBUG LEVEL - C OBJECT")
                    log.infof("Load UserEntity $login from database - INFO LEVEL - C OBJECT")



                    val user = session.createQuery("from UserEntity where login = :login", UserEntity::class.java)
                        .setParameter("login", login)
                        .uniqueResult()
                    transaction.commit()
                    user
                } catch (e: Exception) {
                    transaction.rollback()
                    throw e
                } finally {
                    session.close()
                }
            }
        }.memoize().atLeast(java.time.Duration.ofMinutes(2))
    }
}
