package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Zaznam", namespace = Namespace.ares)
class Zaznam {

    @XmlElement(name = "Obchodni_firma", namespace = Namespace.ares)
    lateinit var obchodniFirma: String

    @XmlElement(name = "ICO", namespace = Namespace.ares)
    lateinit var ico: String

    @XmlElement(name = "Identifikace", namespace = Namespace.ares)
    lateinit var identifikace: Identifikace

    @XmlElement(name = "Pravni_forma", namespace = Namespace.ares)
    lateinit var pravniForma: PravniForma

    @XmlElement(name = "Typ_registru", namespace = Namespace.ares)
    lateinit var typRegistru: TypRegistru

    override fun toString(): String {
        return "Zaznam(obchodniFirma='$obchodniFirma', ico='$ico', identifikace=$identifikace, pravniForma=$pravniForma, typRegistru=$typRegistru)"
    }
}
