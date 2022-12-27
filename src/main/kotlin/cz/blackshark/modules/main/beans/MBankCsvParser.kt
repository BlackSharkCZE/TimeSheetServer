package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import java.io.File
import java.math.BigDecimal
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MBankCsvParser {

    private val datePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    /**
     * Parse incoming file as PaymentEntities
     */
    fun parseOperationList(file: File, companyEntity: CompanyEntity): List<PaymentEntity> {
        return file.readLines(Charset.forName("CP1250"))
            .filter { !it.startsWith("#") }
            .map { line ->
                line.split(";").let {
                    PaymentEntity().apply {
                        this.payment = BigDecimal(it[9].replace(" ", "").replace(",","."))
                        this.companyEntity = companyEntity
                        this.paymentDate = LocalDate.parse(it[0], datePattern)
                    }
                }
            }
    }
}
