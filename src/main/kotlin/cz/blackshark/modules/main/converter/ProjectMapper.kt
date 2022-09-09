package cz.blackshark.modules.main.converter

import cz.blackshark.modules.main.dto.ProjectVo
import cz.blackshark.modules.main.persistence.entity.ProjectEntity

object ProjectMapper {
    fun convert(entity: ProjectEntity): ProjectVo {
        return ProjectVo(
            entity.id,
            entity.name!!,
            entity.company!!.id!!,
            entity.company!!.companyName!!
        )
    }
}
