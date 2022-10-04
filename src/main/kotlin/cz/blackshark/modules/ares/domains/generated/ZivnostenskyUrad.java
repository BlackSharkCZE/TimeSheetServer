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
 * Živnostenský úřad (vz_ciurady)
 *
 * <p>Java class for zivnostensky_urad complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="zivnostensky_urad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KZU" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NZU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}nazev_ZU"/>
 *         &lt;element name="N" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}nazev_obce" minOccurs="0"/>
 *         &lt;element name="NU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}nazev_ulice" minOccurs="0"/>
 *         &lt;element name="PSC" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}psc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "zivnostensky_urad", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "kzu",
    "nzu",
    "n",
    "nu",
    "psc"
})
public class ZivnostenskyUrad {

    @XmlElement(name = "KZU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected int kzu;
    @XmlElement(name = "NZU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected String nzu;
    @XmlElement(name = "N", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String n;
    @XmlElement(name = "NU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String nu;
    @XmlElement(name = "PSC", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String psc;

    /**
     * Gets the value of the kzu property.
     *
     */
    public int getKZU() {
        return kzu;
    }

    /**
     * Sets the value of the kzu property.
     *
     */
    public void setKZU(int value) {
        this.kzu = value;
    }

    /**
     * Gets the value of the nzu property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNZU() {
        return nzu;
    }

    /**
     * Sets the value of the nzu property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNZU(String value) {
        this.nzu = value;
    }

    /**
     * Gets the value of the n property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getN() {
        return n;
    }

    /**
     * Sets the value of the n property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setN(String value) {
        this.n = value;
    }

    /**
     * Gets the value of the nu property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNU() {
        return nu;
    }

    /**
     * Sets the value of the nu property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNU(String value) {
        this.nu = value;
    }

    /**
     * Gets the value of the psc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPSC() {
        return psc;
    }

    /**
     * Sets the value of the psc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPSC(String value) {
        this.psc = value;
    }

}