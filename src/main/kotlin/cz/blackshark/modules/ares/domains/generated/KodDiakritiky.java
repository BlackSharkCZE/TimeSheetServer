//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2021.03.20 at 10:11:56 PM CET
//


package cz.blackshark.modules.ares.domains.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for kod_diakritiky.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="kod_diakritiky">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ISO"/>
 *     &lt;enumeration value="WIN"/>
 *     &lt;enumeration value="LAT"/>
 *     &lt;enumeration value="UTF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "kod_diakritiky", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
@XmlEnum
public enum KodDiakritiky {

    ISO,
    WIN,
    LAT,
    UTF;

    public String value() {
        return name();
    }

    public static KodDiakritiky fromValue(String v) {
        return valueOf(v);
    }

}
