package cz.blackshark.modules.main.beans.mappers

import cz.blackshark.modules.main.dto.PaymentVo
import cz.blackshark.modules.main.persistence.entity.PaymentEntity

interface PaymentMapper {

    fun map(input: PaymentEntity): PaymentVo

}
