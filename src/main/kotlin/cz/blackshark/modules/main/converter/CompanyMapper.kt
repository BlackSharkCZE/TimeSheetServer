package cz.blackshark.modules.main.converter

import cz.blackshark.modules.ares.domains.generated.AresOdpovedi
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.timesheet.commons.domain.CompanyBaseVo
import cz.blackshark.timesheet.commons.domain.CompanyVo

object CompanyMapper {

    fun convert(entity: CompanyEntity): CompanyVo {
        return CompanyVo().apply {
            id = entity.id
            ic = entity.ic
            companyName = entity.companyName
            okres = entity.okres
            obec = entity.obec
            castObce = entity.castObce
            ulice = entity.ulice
            cisloDomu = entity.cisloDomu
            psc = entity.ps
            dic = entity.dic
            platceDph = entity.platceDph
            email = entity.email
            phoneNumber = entity.phoneNumber
            primaryAccount = entity.primaryAccount
            bankAccountNumber = entity.bankAccountNumber
        }
    }

    fun convertBase(entity: CompanyEntity): CompanyBaseVo {
        return CompanyBaseVo().apply {
            id = entity.id
            ic = entity.ic
            companyName = entity.companyName
            dic = entity.dic
            platceDph = entity.platceDph
            email = entity.email
            primary = entity.primaryAccount
        }
    }

    fun convert(ares: AresOdpovedi): CompanyVo {
        return CompanyVo().apply {
            val vbas = ares.odpoved[0].vbas[0]
            val aa = vbas.aa

            ic = vbas.ico.value
            companyName = vbas.of.value
            okres = aa.nok
            obec = aa.n
            castObce = aa.nco
            ulice = aa.nu
            cisloDomu = aa.cd?.toString()
            psc = aa.psc
            dic = vbas.dic?.value
            platceDph = dic != null
            /*
            email=entity.email
            phoneNumber=entity.phoneNumber
            primaryAccount=entity.primaryAccount
            bankAccountNumber=entity.bankAccountNumber*/
        }
    }

}
