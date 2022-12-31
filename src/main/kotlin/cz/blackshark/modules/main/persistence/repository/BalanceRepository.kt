package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.BalanceView
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BalanceRepository : PanacheRepositoryBase<BalanceView, UUID>
