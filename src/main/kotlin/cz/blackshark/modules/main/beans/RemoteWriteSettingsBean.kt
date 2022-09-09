package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.repository.RemoteWriteSettingsRepository
import io.quarkus.cache.CacheResult
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class RemoteWriteSettingsBean @Inject constructor(
    private val logger: Logger,
    private val remoteWriteSettingsRepository: RemoteWriteSettingsRepository
) {

    fun getSettingsForNote(note: String): List<String> {
        return getSettings()
            .firstOrNull {
                it.keyPattern!!.toRegex().containsMatchIn(note)
            }?.remoteWriterList?.split(";") ?: listOf()
    }

    @CacheResult(cacheName = "remote-writer-settings")
    fun getSettings(): List<RemoteWriteSettingsEntity> {
        logger.info("Getting writer settings from repository instead of cache!")
        return remoteWriteSettingsRepository.findAll().list()
    }

}
