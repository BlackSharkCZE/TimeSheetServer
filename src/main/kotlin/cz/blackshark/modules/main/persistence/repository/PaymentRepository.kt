package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PaymentRepository : PanacheRepositoryBase<PaymentEntity, Long>
