//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2021.03.20 at 10:11:56 PM CET
//


package cz.blackshark.modules.ares.domains.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Údaje fyzické osoby v OR
 *
 * <p>Java class for fyzicka_osoba complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fyzicka_osoba">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TP" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}titul_pred" minOccurs="0"/>
 *         &lt;element name="J" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}jmeno" minOccurs="0"/>
 *         &lt;element name="P" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}prijmeni" minOccurs="0"/>
 *         &lt;element name="TZ" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}titul_za" minOccurs="0"/>
 *         &lt;element name="DN" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="RC" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}rodne_cislo" minOccurs="0"/>
 *         &lt;element name="OT" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}osoba_textem" minOccurs="0"/>
 *         &lt;element name="B" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}adresa_ARES" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dod" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="ddo" type="{http://www.w3.org/2001/XMLSchema}date" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fyzicka_osoba", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "tp",
    "j",
    "p",
    "tz",
    "dn",
    "rc",
    "ot",
    "b"
})
public class FyzickaOsoba {

    @XmlElement(name = "TP", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String tp;
    @XmlElement(name = "J", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String j;
    @XmlElement(name = "P", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String p;
    @XmlElement(name = "TZ", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String tz;
    @XmlElement(name = "DN", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dn;
    @XmlElement(name = "RC", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String rc;
    @XmlElement(name = "OT", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String ot;
    @XmlElement(name = "B", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected List<AdresaARES> b;
    @XmlAttribute(name = "dod")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dod;
    @XmlAttribute(name = "ddo")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ddo;

    /**
     * Gets the value of the tp property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTP() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTP(String value) {
        this.tp = value;
    }

    /**
     * Gets the value of the j property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getJ() {
        return j;
    }

    /**
     * Sets the value of the j property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setJ(String value) {
        this.j = value;
    }

    /**
     * Gets the value of the p property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getP() {
        return p;
    }

    /**
     * Sets the value of the p property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setP(String value) {
        this.p = value;
    }

    /**
     * Gets the value of the tz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTZ() {
        return tz;
    }

    /**
     * Sets the value of the tz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTZ(String value) {
        this.tz = value;
    }

    /**
     * Gets the value of the dn property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDN() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDN(XMLGregorianCalendar value) {
        this.dn = value;
    }

    /**
     * Gets the value of the rc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRC() {
        return rc;
    }

    /**
     * Sets the value of the rc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRC(String value) {
        this.rc = value;
    }

    /**
     * Gets the value of the ot property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOT() {
        return ot;
    }

    /**
     * Sets the value of the ot property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOT(String value) {
        this.ot = value;
    }

    /**
     * Gets the value of the b property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the b property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getB().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdresaARES }
     *
     *
     */
    public List<AdresaARES> getB() {
        if (b == null) {
            b = new ArrayList<AdresaARES>();
        }
        return this.b;
    }

    /**
     * Gets the value of the dod property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDod() {
        return dod;
    }

    /**
     * Sets the value of the dod property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDod(XMLGregorianCalendar value) {
        this.dod = value;
    }

    /**
     * Gets the value of the ddo property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDdo() {
        return ddo;
    }

    /**
     * Sets the value of the ddo property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDdo(XMLGregorianCalendar value) {
        this.ddo = value;
    }

}
