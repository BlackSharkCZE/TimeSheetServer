package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
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

        return CSVFormat.Builder.create().apply {
            setDelimiter(';')
            setIgnoreSurroundingSpaces(true)
            setIgnoreEmptyLines(true)
            setIgnoreHeaderCase(true)
        }.build()
            .parse(file.reader(Charset.forName("CP1250")))
            .drop(30).mapNotNull { rec ->
                if (rec.size() < 10) {
                    null
                } else {
                    PaymentEntity().apply {
                        this.payment = BigDecimal(rec[9].replace(" ", "").replace(",", "."))
                        this.companyEntity = companyEntity
                        this.note = getNote(rec)
                        this.paymentDate = LocalDate.parse(rec[0], datePattern)
                    }
                }

            }
    }

    private fun getNote(input: CSVRecord): String {
        println("Processing input for note: ${input.get(2)}, ${input.get(3)}")
        return input.get(2) + if (input.get(3).isEmpty() || input.get(3) == "\"\"") {
            ""
        } else {
            ": " + input[3].replace("\\s+".toRegex(), " ")
        }
    }

}
