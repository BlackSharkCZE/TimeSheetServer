//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2021.03.20 at 10:11:56 PM CET
//


package cz.blackshark.modules.ares.domains.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for adresa_dorucovaci complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="adresa_dorucovaci">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UC" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}ulice_cislo" minOccurs="0"/>
 *         &lt;element name="PB" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}psc_obec" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="zdroj" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}zdroj_type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adresa_dorucovaci", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "uc",
    "pb"
})
public class AdresaDorucovaci {

    @XmlElement(name = "UC", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String uc;
    @XmlElement(name = "PB", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String pb;
    @XmlAttribute(name = "zdroj")
    protected ZdrojType zdroj;

    /**
     * Gets the value of the uc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUC() {
        return uc;
    }

    /**
     * Sets the value of the uc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUC(String value) {
        this.uc = value;
    }

    /**
     * Gets the value of the pb property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPB() {
        return pb;
    }

    /**
     * Sets the value of the pb property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPB(String value) {
        this.pb = value;
    }

    /**
     * Gets the value of the zdroj property.
     *
     * @return
     *     possible object is
     *     {@link ZdrojType }
     *
     */
    public ZdrojType getZdroj() {
        return zdroj;
    }

    /**
     * Sets the value of the zdroj property.
     *
     * @param value
     *     allowed object is
     *     {@link ZdrojType }
     *
     */
    public void setZdroj(ZdrojType value) {
        this.zdroj = value;
    }

}
