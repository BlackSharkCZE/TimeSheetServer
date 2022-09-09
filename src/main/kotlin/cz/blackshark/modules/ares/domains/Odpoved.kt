package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Odpoved", namespace = Namespace.ares)
class Odpoved {
	@XmlElement(name = "Zaznam", namespace = Namespace.ares)
	lateinit var zaznam: Zaznam
	override fun toString(): String {
		return "Odpoved(zaznam=$zaznam)"
	}

}
