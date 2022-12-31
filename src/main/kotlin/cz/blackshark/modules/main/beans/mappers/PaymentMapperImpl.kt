package cz.blackshark.modules.main.beans.mappers

import cz.blackshark.modules.main.dto.PaymentVo
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.InternalServerErrorException

@ApplicationScoped
class PaymentMapperImpl : PaymentMapper {
    override fun map(input: PaymentEntity): PaymentVo = PaymentVo(
        id = input.id!!,
        payment = input.payment,
        companyId = input.companyEntity?.id ?: throw InternalServerErrorException("Company ID is null"),
        paymentDate = input.paymentDate ?: throw InternalServerErrorException("PaymentDate is null"),
        note = input.note ?: throw InternalServerErrorException("Note is null")
    )
}
