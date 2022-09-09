package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Identifikace", namespace = Namespace.ares)
class Identifikace {

	@XmlElement(name = "Adresa_ARES", namespace = Namespace.ares)
	lateinit var adresaAres: AdresaARES

	override fun toString(): String {
		return "Identifikace(adresaAres=$adresaAres)"
	}


}
