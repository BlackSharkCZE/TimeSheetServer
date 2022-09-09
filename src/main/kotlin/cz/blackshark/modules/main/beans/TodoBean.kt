package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.TodoEntity
import cz.blackshark.modules.main.persistence.repository.TodoRepository
import io.quarkus.panache.common.Sort
import org.jboss.logging.Logger
import java.time.ZonedDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceException
import javax.transaction.Transactional

@ApplicationScoped
class TodoBean @Inject constructor(
    private val logger: Logger,
    private val todoRepository: TodoRepository
) {

    fun getAllTodos(): List<TodoEntity> =
        todoRepository.findAll(Sort.by("done", Sort.Direction.Descending).and("created", Sort.Direction.Descending )).list()


    @Transactional
    fun save(todo: TodoEntity): TodoEntity {
        if (todo.created == null) {
            todo.created = ZonedDateTime.now()
        }
        todoRepository.persist(todo)
        return todo
    }

    @Transactional
    fun delete(id: Long) : Long {
        val res = todoRepository.deleteById(id)
        if (!res) throw PersistenceException("Can not delete TODO with ID=$id")
        return id
    }

    @Transactional
    fun markDone(id: Long): TodoEntity {
        val entity =
            todoRepository.findById(id) ?: throw EntityNotFoundException("TODO with ID $id not found in database")
        entity.done = ZonedDateTime.now()
        todoRepository.persist(entity)
        return entity
    }

    @Transactional
    fun markNotDone(id: Long): TodoEntity {
        val entity =
            todoRepository.findById(id) ?: throw EntityNotFoundException("TODO with ID $id not found in database")
        entity.done = null
        todoRepository.persist(entity)
        return entity
    }
}
