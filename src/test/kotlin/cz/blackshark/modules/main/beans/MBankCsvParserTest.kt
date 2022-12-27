package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.InstanceOfAssertFactories
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.nio.file.Path
import java.time.LocalDate
import javax.inject.Inject
import kotlin.io.path.exists

@QuarkusTest
class MBankCsvParserTest {

    @Inject
    private lateinit var mBankCsvParser: MBankCsvParser

    @Test
    fun `test parser MBankExport`() {
        val workingDir = Path.of("", "src/test/resources")
        val filePath = workingDir.resolve("mbank-10068176_180101_221222.csv")
        Assertions.assertThat(filePath.exists()).isTrue

        val result = mBankCsvParser.parseOperationList(filePath.toFile(), CompanyEntity())
        Assertions.assertThat(result).hasSize(10)
        Assertions.assertThat(result)
            .first().asInstanceOf(InstanceOfAssertFactories.type(PaymentEntity::class.java))
            .let {
                it.extracting("payment").isEqualTo(BigDecimal("-727.00"))
                it.extracting("paymentDate").isNotNull.isEqualTo(LocalDate.parse("2018-01-01"))
            }

        Assertions.assertThat(result)
            .last().asInstanceOf(InstanceOfAssertFactories.type(PaymentEntity::class.java))
            .let {
                it.extracting("payment").isEqualTo(BigDecimal("-10638.00"))
                it.extracting("paymentDate").isNotNull.isEqualTo(LocalDate.parse("2018-03-27"))
            }
    }
}
