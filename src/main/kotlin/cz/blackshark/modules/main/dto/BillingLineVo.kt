package cz.blackshark.modules.main.dto

import java.math.BigDecimal
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.SqlResultSetMapping

data class BillingLineVo(
    val companyName: String,
    val companyId: Long,
    val workTime: Long,
    val earn: BigDecimal,
    val note: String
)


@SqlResultSetMapping(
    name = "BillingLineVoMapping",
    classes = [
        ConstructorResult(
            targetClass = BillingLineVo::class, columns = [
                ColumnResult(name = "company_name", type = String::class),
                ColumnResult(name = "company_id", type = Long::class),
                ColumnResult(name = "work_time", type = Long::class),
                ColumnResult(name = "earn", type = BigDecimal::class),
                ColumnResult(name = "note", type = String::class),
            ]
        )]
)
@Entity
class SqlMappingCfgEntity {
    @Id
    var id: Int? = null
}
