package cz.blackshark.modules.main.beans.mappers

import cz.blackshark.modules.main.dto.PaymentVo
import cz.blackshark.modules.main.persistence.entity.PaymentEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface PaymentMapper {

    @Mapping(source = "companyEntity.id", target = "companyId")
    fun map(input: PaymentEntity): PaymentVo

}
