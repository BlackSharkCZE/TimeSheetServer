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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Opíše došlý dotaz, příp. doplní implicitní hodnoty místo nezadaných a přidá k němu nalezenou odpověď
 *
 * <p>Java class for stadrs_dotaz complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="stadrs_dotaz">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KS" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}kod_statu" minOccurs="0"/>
 *         &lt;element name="KOK" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}kod_okresu" minOccurs="0"/>
 *         &lt;element name="N" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}nazev_obce" minOccurs="0"/>
 *         &lt;element name="NCO" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}nazev_casti_obce" minOccurs="0"/>
 *         &lt;element name="NU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}nazev_ulice" minOccurs="0"/>
 *         &lt;element name="CA" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}cislo_do_adresy" minOccurs="0"/>
 *         &lt;element name="PSC" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3}psc" minOccurs="0"/>
 *         &lt;element name="AT" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}adresa_textem" minOccurs="0"/>
 *         &lt;element name="Diakritika" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="K_diakritiky" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}kod_diakritiky"/>
 *         &lt;element name="Max_pocet" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="Preteceni" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}preteceni"/>
 *         &lt;element name="Redukce_slov" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}ano_ne"/>
 *         &lt;element name="Format_odpovedi" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}vystup_format"/>
 *         &lt;element name="Chyba" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}chyba" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stadrs_dotaz", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "ks",
    "kok",
    "n",
    "nco",
    "nu",
    "ca",
    "psc",
    "at",
    "diakritika",
    "kDiakritiky",
    "maxPocet",
    "preteceni",
    "redukceSlov",
    "formatOdpovedi",
    "chyba"
})
public class StadrsDotaz {

    @XmlElement(name = "KS", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String ks;
    @XmlElement(name = "KOK", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    @XmlSchemaType(name = "integer")
    protected Integer kok;
    @XmlElement(name = "N", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String n;
    @XmlElement(name = "NCO", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String nco;
    @XmlElement(name = "NU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String nu;
    @XmlElement(name = "CA", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String ca;
    @XmlElement(name = "PSC", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String psc;
    @XmlElement(name = "AT", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected String at;
    @XmlElement(name = "Diakritika", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", defaultValue = "true")
    protected boolean diakritika;
    @XmlElement(name = "K_diakritiky", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true, defaultValue = "ISO")
    @XmlSchemaType(name = "string")
    protected KodDiakritiky kDiakritiky;
    @XmlElement(name = "Max_pocet", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", defaultValue = "20")
    protected short maxPocet;
    @XmlElement(name = "Preteceni", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true, defaultValue = "N")
    @XmlSchemaType(name = "string")
    protected Preteceni preteceni;
    @XmlElement(name = "Redukce_slov", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true, defaultValue = "A")
    protected String redukceSlov;
    @XmlElement(name = "Format_odpovedi", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true, defaultValue = "XML")
    @XmlSchemaType(name = "string")
    protected VystupFormat formatOdpovedi;
    @XmlElement(name = "Chyba", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected List<Chyba> chyba;

    /**
     * Gets the value of the ks property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getKS() {
        return ks;
    }

    /**
     * Sets the value of the ks property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setKS(String value) {
        this.ks = value;
    }

    /**
     * Gets the value of the kok property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getKOK() {
        return kok;
    }

    /**
     * Sets the value of the kok property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setKOK(Integer value) {
        this.kok = value;
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
     * Gets the value of the nco property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNCO() {
        return nco;
    }

    /**
     * Sets the value of the nco property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNCO(String value) {
        this.nco = value;
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
     * Gets the value of the ca property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCA() {
        return ca;
    }

    /**
     * Sets the value of the ca property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCA(String value) {
        this.ca = value;
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

    /**
     * Gets the value of the at property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAT() {
        return at;
    }

    /**
     * Sets the value of the at property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAT(String value) {
        this.at = value;
    }

    /**
     * Gets the value of the diakritika property.
     *
     */
    public boolean isDiakritika() {
        return diakritika;
    }

    /**
     * Sets the value of the diakritika property.
     *
     */
    public void setDiakritika(boolean value) {
        this.diakritika = value;
    }

    /**
     * Gets the value of the kDiakritiky property.
     *
     * @return
     *     possible object is
     *     {@link KodDiakritiky }
     *
     */
    public KodDiakritiky getKDiakritiky() {
        return kDiakritiky;
    }

    /**
     * Sets the value of the kDiakritiky property.
     *
     * @param value
     *     allowed object is
     *     {@link KodDiakritiky }
     *
     */
    public void setKDiakritiky(KodDiakritiky value) {
        this.kDiakritiky = value;
    }

    /**
     * Gets the value of the maxPocet property.
     *
     */
    public short getMaxPocet() {
        return maxPocet;
    }

    /**
     * Sets the value of the maxPocet property.
     *
     */
    public void setMaxPocet(short value) {
        this.maxPocet = value;
    }

    /**
     * Gets the value of the preteceni property.
     *
     * @return
     *     possible object is
     *     {@link Preteceni }
     *
     */
    public Preteceni getPreteceni() {
        return preteceni;
    }

    /**
     * Sets the value of the preteceni property.
     *
     * @param value
     *     allowed object is
     *     {@link Preteceni }
     *
     */
    public void setPreteceni(Preteceni value) {
        this.preteceni = value;
    }

    /**
     * Gets the value of the redukceSlov property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRedukceSlov() {
        return redukceSlov;
    }

    /**
     * Sets the value of the redukceSlov property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRedukceSlov(String value) {
        this.redukceSlov = value;
    }

    /**
     * Gets the value of the formatOdpovedi property.
     *
     * @return
     *     possible object is
     *     {@link VystupFormat }
     *
     */
    public VystupFormat getFormatOdpovedi() {
        return formatOdpovedi;
    }

    /**
     * Sets the value of the formatOdpovedi property.
     *
     * @param value
     *     allowed object is
     *     {@link VystupFormat }
     *
     */
    public void setFormatOdpovedi(VystupFormat value) {
        this.formatOdpovedi = value;
    }

    /**
     * Gets the value of the chyba property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chyba property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChyba().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Chyba }
     *
     *
     */
    public List<Chyba> getChyba() {
        if (chyba == null) {
            chyba = new ArrayList<Chyba>();
        }
        return this.chyba;
    }

}
