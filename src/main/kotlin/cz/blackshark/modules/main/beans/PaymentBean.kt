package cz.blackshark.modules.main.beans

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.mappers.PaymentMapper
import cz.blackshark.modules.main.dto.PaymentVo
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.PaymentRepository
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import kotlin.streams.toList

@ApplicationScoped
class PaymentBean {

    @Inject
    private lateinit var companyRepository: CompanyRepository

    @Inject
    private lateinit var paymentRepository: PaymentRepository

    @Inject
    private lateinit var paymentMapper: PaymentMapper

    fun getAllPayments(companyId: Long?): List<PaymentVo> {
        val cId =
            companyId ?: companyRepository.findPrimaryCompany()?.id ?: throw CompanyExcetption("Company not found")

        return paymentRepository.find("companyEntity.id", Sort.descending("paymentDate"), cId)
            .stream<PaymentEntity>()
            .map(paymentMapper::map)
            .toList()
    }

    fun createNewPayment(input: PaymentVo): RestResponse<PaymentVo> {
        val result = companyRepository.findByIdOptional(input.companyId)
        if (result.isEmpty) {
            throw CompanyExcetption("Company with id ${input.companyId} not found!")
        }
        val pEntity = PaymentEntity().apply {
            this.companyEntity = result.get()
            this.paymentDate = input.paymentDate
            this.payment = input.payment
        }
        paymentRepository.persist(pEntity)
        return RestResponse(true, null, paymentMapper.map(pEntity))
    }

}
