package cz.blackshark.modules.datatable.controller

import cz.blackshark.modules.datatable.domain.DataTableResponse
import cz.blackshark.modules.datatable.domain.FilterSetting
import cz.blackshark.modules.datatable.domain.Paginator
import cz.blackshark.modules.main.beans.RemoteWriteSettingsBean
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.modules.main.converter.ProjectMapper
import cz.blackshark.modules.main.dto.TimelineVo
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.ProjectEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import cz.blackshark.modules.main.persistence.repository.ProjectRepository
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import java.io.InvalidClassException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

@Path("/datatable")
@Produces(MediaType.APPLICATION_JSON)
class ApplicationDatatableController @Inject constructor(
    val timelineRepository: TimelineRepository,
    val invoiceRepository: InvoiceRepository,
    val companyRepository: CompanyRepository,
    val projectRepository: ProjectRepository,
    val remoteWriterSettingsBean: RemoteWriteSettingsBean
) {

    @Path("{resource}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun getTimeline(
        @PathParam("resource") resource: String,
        @QueryParam("page") @DefaultValue("0") page: Int,
        @QueryParam("pageSize") @DefaultValue("25") pageSize: Int,
        @QueryParam("sort") sort: String,
        filter: String
    ): DataTableResponse<Any> {

        val filterJson = Json.decodeValue(filter) as JsonObject  // prichozi JSON String
        val filterMap = mutableMapOf<String, FilterSetting>()    // pozadovana mapa
        filterJson.fieldNames().forEach { key ->
            filterMap[key] = filterJson.getJsonObject(key).mapTo(FilterSetting::class.java)
        }

        return when (resource) {
            "projects" -> buildDataTableResult(
                projectRepository,
                page, pageSize,
                sort,
                buildParams(filterMap, ProjectEntity::class)
            ) { en -> ProjectMapper.convert(en) }
            "companies" -> buildDataTableResult(
                companyRepository,
                page, pageSize,
                sort,
                buildParams(filterMap, CompanyEntity::class)
            ) { en -> CompanyMapper.convertBase(en) }
            "timeline" -> {
                buildDataTableResult(
                    timelineRepository,
                    page, pageSize,
                    sort,
                    buildParams(filterMap, TimelineEntity::class)
                ) { en ->
                    TimelineVo(
                        en, remoteWriterSettingsBean.getSettingsForNote(en.note)
                    )
                }
            }
            "invoices" -> buildDataTableResult(
                invoiceRepository,
                page, pageSize,
                sort,
                buildParams(filterMap, InvoiceEntity::class)
            ) { it }
            else -> throw BadRequestException("Invalid request to read data for datatable. Unknow resource")
        }
    }

    private fun <I, O> buildDataTableResult(
        repository: PanacheRepositoryBase<I, *>,
        page: Int,
        pageSize: Int,
        sort: String,
        params: Pair<String, Map<String, Any?>>?,
        convert: (I) -> O
    ): DataTableResponse<O> {
        if (params == null) {
            val query: PanacheQuery<I> = repository
                .findAll(Sort.by(sort, Sort.Direction.Descending))
                .page(Page.of(page, pageSize))
            val rows: List<O> = query.list<I>().map { convert.invoke(it) }
            val paginator = Paginator(query.pageCount(), pageSize, page)
            return DataTableResponse(rows, paginator)
        } else {
            val query: PanacheQuery<I> = repository
                .find(params.first, Sort.by(sort, Sort.Direction.Descending), params.second)
                .page(Page.of(page, pageSize))
            val rows: List<O> = query.list<I>().map { convert.invoke(it) }
            val paginator = Paginator(query.pageCount(), pageSize, page)
            return DataTableResponse(rows, paginator)
        }

    }

    private fun buildParams(
        filter: Map<String, FilterSetting>,
        entityClass: KClass<*>
    ): Pair<String, Map<String, Any?>>? {
        if (filter.isEmpty()) {
            return null
        } else {
            val whereParts = mutableListOf<String>()
//			val whereBuilder = StringBuilder()
            val params = mutableMapOf<String, Any?>()
            filter.forEach { (key, filter) ->
                when (filter.type) {
                    "=" -> {
                        if (filter.childKey != null) {
                            whereParts.add("$key.${filter.childKey} = :$key")
                        } else {
                            whereParts.add("$key = :$key")
                        }
                        params[key] = getValueByType(key, filter, entityClass)
                    }
                    "like" -> {
                        if (filter.childKey != null) {
                            whereParts.add("$key.${filter.childKey} like :$key")
                        } else {
                            whereParts.add("$key like :$key")
                        }

                        params[key] = getValueByType(key, filter, entityClass)
                    }
                    "between" -> {
                        whereParts.add("$key between :${key}_1 AND :${key}_2")
                        params[key + "_1"] = getMultiValue(key, filter, entityClass, 0)
                        params[key + "_2"] = getMultiValue(key, filter, entityClass, 1)
                    }
                }
            }
            return Pair(whereParts.joinToString(" AND "), params)
        }
    }

    private fun getMultiValue(key: String, filter: FilterSetting, entityClass: KClass<*>, index: Int): Any? {
        val property = entityClass.members.firstOrNull { it.name == key }
        return if (property == null) {
            null
        } else {
            when (property.returnType.jvmErasure) {
                ZonedDateTime::class -> getZonedDateTime((filter.value as String).split(";")[index], null, filter.type)
                LocalDate::class -> LocalDate.parse((filter.value as String).split(";")[index])
                else -> null
            }
        }
    }

    private fun getValueByType(key: String, filter: FilterSetting, entityClass: KClass<*>): Any? {
        val property = entityClass.members.firstOrNull { it.name == key }
        return if (property == null) {
            null
        } else {
            when (property.returnType.jvmErasure) {
                Long::class -> asLong(filter.value)
                String::class -> filter.value as String
                ZonedDateTime::class -> getZonedDateTime(filter.value as String, null, filter.type)
                else -> {
                    if (filter.childKey != null) {
                        getValueByType(
                            filter.childKey!!,
                            FilterSetting(filter.type, filter.value),
                            property.returnType.jvmErasure
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    private fun asLong(x: Any): Long {
        if (x is Long) {
            return x
        } else {
            if (x is Int) {
                return x.toLong()
            }
        }
        throw InvalidClassException("Excpected Long or Int on argument")
    }

    private fun getZonedDateTime(strValue: String, pattern: String?, filterType: String): ZonedDateTime {
        return if (pattern == null) {
            try {
                LocalDateTime.parse(strValue).atZone(ZoneId.systemDefault())
            } catch (e: DateTimeParseException) {
                LocalDateTime.parse("${strValue}T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
                    .atZone(ZoneId.systemDefault())
            }
        } else {
            ZonedDateTime.parse(strValue, DateTimeFormatter.ofPattern(pattern))
        }
    }

}
