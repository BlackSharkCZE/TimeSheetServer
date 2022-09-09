package cz.blackshark.modules.ares.domains

import cz.blackshark.modules.ares.domains.Namespace
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Pravni_forma", namespace = Namespace.ares)
class PravniForma {

	@XmlElement(name = "Kod_PF", namespace = Namespace.datatypes)
	var kod: String? = null

	override fun toString(): String {
		return "PravniForma(kod='$kod')"
	}


}
