package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.TodoEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
open class TodoRepository : PanacheRepositoryBase<TodoEntity, Long> {
}
