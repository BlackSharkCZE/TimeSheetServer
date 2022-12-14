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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Základní údaje o ekonomickém subjektu resp. fyzické osobě z CEU, vyčištěné, upravené.
 *
 * <p>Java class for k_eksubj complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="k_eksubj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PFO" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}pfc"/>
 *         &lt;element name="Cele_jmeno" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}popis"/>
 *         &lt;element name="ICO" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}ico"/>
 *         &lt;element name="A_sidla" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}adresa_ARES" minOccurs="0"/>
 *         &lt;element name="K_cosoba" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}k_cosoba" minOccurs="0"/>
 *         &lt;element name="Ares" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}ano_ne"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "k_eksubj", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "pfo",
    "celeJmeno",
    "ico",
    "aSidla",
    "kCosoba",
    "ares"
})
public class KEksubj {

    @XmlElement(name = "PFO", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    @XmlSchemaType(name = "string")
    protected Pfc pfo;
    @XmlElement(name = "Cele_jmeno", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected String celeJmeno;
    @XmlElement(name = "ICO", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected String ico;
    @XmlElement(name = "A_sidla", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected AdresaARES aSidla;
    @XmlElement(name = "K_cosoba", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected KCosoba kCosoba;
    @XmlElement(name = "Ares", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected String ares;

    /**
     * Gets the value of the pfo property.
     *
     * @return
     *     possible object is
     *     {@link Pfc }
     *
     */
    public Pfc getPFO() {
        return pfo;
    }

    /**
     * Sets the value of the pfo property.
     *
     * @param value
     *     allowed object is
     *     {@link Pfc }
     *
     */
    public void setPFO(Pfc value) {
        this.pfo = value;
    }

    /**
     * Gets the value of the celeJmeno property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCeleJmeno() {
        return celeJmeno;
    }

    /**
     * Sets the value of the celeJmeno property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCeleJmeno(String value) {
        this.celeJmeno = value;
    }

    /**
     * Gets the value of the ico property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getICO() {
        return ico;
    }

    /**
     * Sets the value of the ico property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setICO(String value) {
        this.ico = value;
    }

    /**
     * Gets the value of the aSidla property.
     *
     * @return
     *     possible object is
     *     {@link AdresaARES }
     *
     */
    public AdresaARES getASidla() {
        return aSidla;
    }

    /**
     * Sets the value of the aSidla property.
     *
     * @param value
     *     allowed object is
     *     {@link AdresaARES }
     *
     */
    public void setASidla(AdresaARES value) {
        this.aSidla = value;
    }

    /**
     * Gets the value of the kCosoba property.
     *
     * @return
     *     possible object is
     *     {@link KCosoba }
     *
     */
    public KCosoba getKCosoba() {
        return kCosoba;
    }

    /**
     * Sets the value of the kCosoba property.
     *
     * @param value
     *     allowed object is
     *     {@link KCosoba }
     *
     */
    public void setKCosoba(KCosoba value) {
        this.kCosoba = value;
    }

    /**
     * Gets the value of the ares property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAres() {
        return ares;
    }

    /**
     * Sets the value of the ares property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAres(String value) {
        this.ares = value;
    }

}
