package cz.blackshark.modules.main.beans.mappers

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@QuarkusTest
class PaymentMapperTest {

    @Inject
    private lateinit var paymentMapper: PaymentMapper

    @Test
    fun `test mapping entity to dto`() {
        val input = PaymentEntity().apply {
            this.id = 1L
            this.paymentDate = LocalDate.now()
            this.payment = BigDecimal.TEN
            this.companyEntity = CompanyEntity().apply {
                this.id = 2L
            }
            this.note = "TestNote"
        }

        val output = paymentMapper.map(input)
        Assertions.assertThat(output.id).isNotNull.isEqualTo(input.id)
        Assertions.assertThat(output.companyId).isEqualTo(input.companyEntity?.id)
        Assertions.assertThat(output.paymentDate).isEqualTo(input.paymentDate)
        Assertions.assertThat(output.payment).isEqualTo(input.payment)
        Assertions.assertThat(output.note).isEqualTo(input.note)
    }
}
