package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Ares_odpovedi", namespace = Namespace.ares)
class AresOdpovedi {
	@XmlElement(name = "Odpoved", namespace = Namespace.ares)
	lateinit var odpoved: Odpoved
	override fun toString(): String {
		return "AresOdpovedi(odpoved=$odpoved)"
	}


}
