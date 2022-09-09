package cz.blackshark.modules.ares.domains

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Adresa_ARES", namespace = Namespace.ares)
class AdresaARES {

	@XmlElement(name = "Nazev_okresu", namespace = Namespace.datatypes)
	var nazevOkresu: String = ""

	@XmlElement(name = "Nazev_obce", namespace = Namespace.datatypes)
	var nazevObce: String = ""

	@XmlElement(name = "Nazev_casti_obce", namespace = Namespace.datatypes)
	var nazevCastiObce: String = ""

	@XmlElement(name = "Nazev_ulice", namespace = Namespace.datatypes)
	var nazevUlice: String = ""

	@XmlElement(name = "Cislo_domovni", namespace = Namespace.datatypes)
	var cisloDomovni: String = ""

	@XmlElement(name = "PSC", namespace = Namespace.datatypes)
	var psc: String = ""

	override fun toString(): String {
		return "AdresaARES(nazevOkresu='$nazevOkresu', nazevObce='$nazevObce', nazevCastiObce='$nazevCastiObce', nazevUlice='$nazevUlice', " +
				"cisloDomovni='$cisloDomovni', psc='$psc')"
	}


}
