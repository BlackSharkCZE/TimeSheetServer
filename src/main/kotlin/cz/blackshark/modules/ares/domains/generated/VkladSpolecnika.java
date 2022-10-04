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
 * VK a jeho splacení
 *
 * <p>Java class for vklad_spolecnika complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="vklad_spolecnika">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VK" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}vklad" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SPL" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}splaceno" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TSP" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
 *         &lt;element name="ZP" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
 *         &lt;element name="OP" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
 *         &lt;element name="KOM" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
 *         &lt;element name="TUP" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
 *         &lt;element name="T" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}texty" minOccurs="0"/>
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
@XmlType(name = "vklad_spolecnika", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "vk",
    "spl",
    "tsp",
    "zp",
    "op",
    "kom",
    "tup",
    "t"
})
public class VkladSpolecnika {

    @XmlElement(name = "VK", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected List<Vklad> vk;
    @XmlElement(name = "SPL", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected List<Splaceno> spl;
    @XmlElement(name = "TSP", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty tsp;
    @XmlElement(name = "ZP", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty zp;
    @XmlElement(name = "OP", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty op;
    @XmlElement(name = "KOM", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty kom;
    @XmlElement(name = "TUP", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty tup;
    @XmlElement(name = "T", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected Texty t;
    @XmlAttribute(name = "dod")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dod;
    @XmlAttribute(name = "ddo")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ddo;

    /**
     * Gets the value of the vk property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vk property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVK().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vklad }
     *
     *
     */
    public List<Vklad> getVK() {
        if (vk == null) {
            vk = new ArrayList<Vklad>();
        }
        return this.vk;
    }

    /**
     * Gets the value of the spl property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spl property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPL().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Splaceno }
     *
     *
     */
    public List<Splaceno> getSPL() {
        if (spl == null) {
            spl = new ArrayList<Splaceno>();
        }
        return this.spl;
    }

    /**
     * Gets the value of the tsp property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getTSP() {
        return tsp;
    }

    /**
     * Sets the value of the tsp property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setTSP(Texty value) {
        this.tsp = value;
    }

    /**
     * Gets the value of the zp property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getZP() {
        return zp;
    }

    /**
     * Sets the value of the zp property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setZP(Texty value) {
        this.zp = value;
    }

    /**
     * Gets the value of the op property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getOP() {
        return op;
    }

    /**
     * Sets the value of the op property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setOP(Texty value) {
        this.op = value;
    }

    /**
     * Gets the value of the kom property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getKOM() {
        return kom;
    }

    /**
     * Sets the value of the kom property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setKOM(Texty value) {
        this.kom = value;
    }

    /**
     * Gets the value of the tup property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getTUP() {
        return tup;
    }

    /**
     * Sets the value of the tup property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setTUP(Texty value) {
        this.tup = value;
    }

    /**
     * Gets the value of the t property.
     *
     * @return
     *     possible object is
     *     {@link Texty }
     *
     */
    public Texty getT() {
        return t;
    }

    /**
     * Sets the value of the t property.
     *
     * @param value
     *     allowed object is
     *     {@link Texty }
     *
     */
    public void setT(Texty value) {
        this.t = value;
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