package cz.blackshark.modules.main.http.resource

import cz.blackshark.annotations.Logged
import cz.blackshark.modules.main.beans.TodoBean
import cz.blackshark.modules.main.persistence.entity.TodoEntity
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query
import javax.inject.Inject

@GraphQLApi
@Logged
class TodoResource {

    @Inject
    lateinit var todoBean: TodoBean

    @Query("allTodo")
    @Description("Get all todos in the system")
    fun getAllTodos(): List<TodoEntity> = todoBean.getAllTodos()

    @Mutation
    fun createTodo(todo: TodoEntity) : TodoEntity = todoBean.save(todo)

    @Mutation
    fun markDone(id: Long): TodoEntity = todoBean.markDone(id)

    @Mutation
    fun markNotDone(id: Long): TodoEntity = todoBean.markNotDone(id)

    @Mutation
    fun delete(id: Long): Long = todoBean.delete(id)
}
