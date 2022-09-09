package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Typ_registru", namespace = Namespace.ares)
class TypRegistru {
    @XmlElement(name = "Kod", namespace = Namespace.datatypes)
    var kod: Int = 0

    @XmlElement(name = "Text", namespace = Namespace.datatypes)
    lateinit var text: String

    override fun toString(): String {
        return "TypRegistru(kod=$kod, text='$text')"
    }


}
