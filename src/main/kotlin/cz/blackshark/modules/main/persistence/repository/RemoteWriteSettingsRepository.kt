package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RemoteWriteSettingsRepository : PanacheRepositoryBase<RemoteWriteSettingsEntity, Long> {


}
