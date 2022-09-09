//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2021.03.20 at 10:11:56 PM CET
//


package cz.blackshark.modules.ares.domains.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Výpis z registru zdravotnických zařízení
 *
 * <p>Java class for vypis_RZZ complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="vypis_RZZ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UVOD" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}uvod" minOccurs="0"/>
 *         &lt;element name="ZAU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}zakladni_udaje_RZZ"/>
 *         &lt;element name="SI" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}adresa_ARES" minOccurs="0"/>
 *         &lt;element name="ZUJ" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}zuj" minOccurs="0"/>
 *         &lt;element name="DAU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}dalsi_udaje" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vypis_RZZ", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "uvod",
    "zau",
    "si",
    "zuj",
    "dau"
})
public class VypisRZZ {

    @XmlElement(name = "UVOD", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Uvod uvod;
    @XmlElement(name = "ZAU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected ZakladniUdajeRZZ zau;
    @XmlElement(name = "SI", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected AdresaARES si;
    @XmlElement(name = "ZUJ", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Zuj zuj;
    @XmlElement(name = "DAU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected DalsiUdaje dau;

    /**
     * Gets the value of the uvod property.
     *
     * @return
     *     possible object is
     *     {@link Uvod }
     *
     */
    public Uvod getUVOD() {
        return uvod;
    }

    /**
     * Sets the value of the uvod property.
     *
     * @param value
     *     allowed object is
     *     {@link Uvod }
     *
     */
    public void setUVOD(Uvod value) {
        this.uvod = value;
    }

    /**
     * Gets the value of the zau property.
     *
     * @return
     *     possible object is
     *     {@link ZakladniUdajeRZZ }
     *
     */
    public ZakladniUdajeRZZ getZAU() {
        return zau;
    }

    /**
     * Sets the value of the zau property.
     *
     * @param value
     *     allowed object is
     *     {@link ZakladniUdajeRZZ }
     *
     */
    public void setZAU(ZakladniUdajeRZZ value) {
        this.zau = value;
    }

    /**
     * Gets the value of the si property.
     *
     * @return
     *     possible object is
     *     {@link AdresaARES }
     *
     */
    public AdresaARES getSI() {
        return si;
    }

    /**
     * Sets the value of the si property.
     *
     * @param value
     *     allowed object is
     *     {@link AdresaARES }
     *
     */
    public void setSI(AdresaARES value) {
        this.si = value;
    }

    /**
     * Gets the value of the zuj property.
     *
     * @return
     *     possible object is
     *     {@link Zuj }
     *
     */
    public Zuj getZUJ() {
        return zuj;
    }

    /**
     * Sets the value of the zuj property.
     *
     * @param value
     *     allowed object is
     *     {@link Zuj }
     *
     */
    public void setZUJ(Zuj value) {
        this.zuj = value;
    }

    /**
     * Gets the value of the dau property.
     *
     * @return
     *     possible object is
     *     {@link DalsiUdaje }
     *
     */
    public DalsiUdaje getDAU() {
        return dau;
    }

    /**
     * Sets the value of the dau property.
     *
     * @param value
     *     allowed object is
     *     {@link DalsiUdaje }
     *
     */
    public void setDAU(DalsiUdaje value) {
        this.dau = value;
    }

}
