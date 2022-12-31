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
        val filePath = workingDir.resolve("mbank-10068176_180101_221228.csv")
        Assertions.assertThat(filePath.exists()).isTrue

        val result = mBankCsvParser.parseOperationList(filePath.toFile(), CompanyEntity())
        Assertions.assertThat(result).hasSize(10)
        Assertions.assertThat(result)
            .first().asInstanceOf(InstanceOfAssertFactories.type(PaymentEntity::class.java))
            .let {
                it.extracting("note").isEqualTo("POPLATEK ZA OKAMŽITOU PLATBU")
                it.extracting("payment").isEqualTo(BigDecimal("-1.00"))
                it.extracting("paymentDate").isNotNull.isEqualTo(LocalDate.parse("2022-12-14"))
            }

        Assertions.assertThat(result)
            .last().asInstanceOf(InstanceOfAssertFactories.type(PaymentEntity::class.java))
            .let {
                it.extracting("note").isEqualTo("VLASTNÍ PŘEVOD: PŘEVOD PROSTŘEDKŮ")
                it.extracting("payment").isEqualTo(BigDecimal("-20000.00"))
                it.extracting("paymentDate").isNotNull.isEqualTo(LocalDate.parse("2022-12-28"))
            }
    }
}
