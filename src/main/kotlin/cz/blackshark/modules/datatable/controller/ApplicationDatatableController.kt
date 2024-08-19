package cz.blackshark.modules.datatable.controller

import cz.blackshark.modules.datatable.domain.DataTableResponse
import cz.blackshark.modules.datatable.domain.FilterSetting
import cz.blackshark.modules.datatable.domain.Paginator
import cz.blackshark.modules.main.beans.PrincipalService
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
import cz.blackshark.modules.main.persistence.repository.SubjectRepository
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import io.quarkus.security.Authenticated
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
import javax.ws.rs.BadRequestException
import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

@Path("/datatable")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class ApplicationDatatableController {

    @Inject
    lateinit var timelineRepository: TimelineRepository

    @Inject
    lateinit var invoiceRepository: InvoiceRepository

    @Inject
    lateinit var companyRepository: CompanyRepository

    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var remoteWriterSettingsBean: RemoteWriteSettingsBean

    @Inject
    private lateinit var pricipalService: PrincipalService

    @Path("{resource}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(NotAuthorizedException::class)
    fun getTimeline(
        @Context securityContext: SecurityContext,
        @PathParam("resource") resource: String,
        @QueryParam("page") @DefaultValue("0") page: Int,
        @QueryParam("pageSize") @DefaultValue("25") pageSize: Int,
        @QueryParam("sort") sort: String,
        filter: String,
    ): DataTableResponse<Any> {

        return pricipalService.withTimesheetPrincipalAndSubjectEntity(securityContext) { tp, subject ->
            val filterJson = Json.decodeValue(filter) as JsonObject  // prichozi JSON String
            val filterMap = mutableMapOf<String, FilterSetting>()    // pozadovana mapa
            filterJson.fieldNames().forEach { key ->
                filterMap[key] = filterJson.getJsonObject(key).mapTo(FilterSetting::class.java)
            }

            when (resource) {
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
                    filterMap["subject"] = FilterSetting(type = "=", value = subject.id!!, childKey = "id")
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
                "invoices" -> {
                    // todo limit only to invoice where current user is issuer or recipient
                    filterMap["issuerCompany"] = FilterSetting(type = "=", value = tp.companyId ?: -1, childKey = "id")
                    filterMap["recipientCompany"] = FilterSetting(type = "=", value = tp.companyId ?: -1, childKey = "id")

                    buildDataTableResult(
                        invoiceRepository,
                        page, pageSize,
                        sort,
                        buildParams(filterMap, InvoiceEntity::class)
                    ) { it }
                }
                else -> throw BadRequestException("Invalid request to read data for datatable. Unknown resource")
            }
        }
    }

    private fun <I, O> buildDataTableResult(
        repository: PanacheRepositoryBase<I, *>,
        page: Int,
        pageSize: Int,
        sort: String,
        params: Pair<String, Map<String, Any?>>?,
        convert: (I) -> O,
    ): DataTableResponse<O> {
        if (params == null) {
            val query: PanacheQuery<I> = repository
                .findAll(Sort.by(sort, Sort.Direction.Descending))
                .page(Page.of(page, pageSize))
            val rows: List<O> = query.list<I>().map { convert.invoke(it) }
            val paginator = Paginator(query.pageCount(), pageSize, page, query.count())
            return DataTableResponse(rows, paginator)
        } else {
            val query: PanacheQuery<I> = repository
                .find(params.first, Sort.by(sort, Sort.Direction.Descending), params.second)
                .page(Page.of(page, pageSize))
            val rows: List<O> = query.list<I>().map { convert.invoke(it) }
            val paginator = Paginator(query.pageCount(), pageSize, page, query.count())
            return DataTableResponse(rows, paginator)
        }
    }

    private fun buildParams(
        filter: Map<String, FilterSetting>,
        entityClass: KClass<*>,
    ): Pair<String, Map<String, Any?>>? {
        if (filter.isEmpty()) {
            return null
        } else {
            val whereParts = mutableListOf<String>()
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

            if (filter.containsKey("issuerCompany") && filter.containsKey("recipientCompany")) {
                val issuer = filter["issuerCompany"]
                val recipient = filter["recipientCompany"]
                val qp = "((issuerCompany.id = :issuerCompany) OR (recipientCompany.id = :recipientCompany))"
                whereParts.add(qp)
                params["issuerCompany"] = issuer?.value
                params["recipientCompany"] = recipient?.value
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
