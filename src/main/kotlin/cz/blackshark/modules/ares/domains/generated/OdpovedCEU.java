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
 * <p>Java class for odpoved_CEU complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="odpoved_CEU">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="E" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}error_ARES"/>
 *         &lt;element name="VH" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}vysledek_hledani"/>
 *         &lt;element name="PZA" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Vypis_CEU" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}vypis_ceu" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "odpoved_CEU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "pid",
    "e",
    "vh",
    "pza",
    "vypisCEU"
})
public class OdpovedCEU {

    @XmlElement(name = "PID", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected int pid;
    @XmlElement(name = "E", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected ErrorARES e;
    @XmlElement(name = "VH", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected VysledekHledani vh;
    @XmlElement(name = "PZA", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected int pza;
    @XmlElement(name = "Vypis_CEU", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3")
    protected VypisCeu vypisCEU;

    /**
     * Gets the value of the pid property.
     *
     */
    public int getPID() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     *
     */
    public void setPID(int value) {
        this.pid = value;
    }

    /**
     * Gets the value of the e property.
     *
     * @return
     *     possible object is
     *     {@link ErrorARES }
     *
     */
    public ErrorARES getE() {
        return e;
    }

    /**
     * Sets the value of the e property.
     *
     * @param value
     *     allowed object is
     *     {@link ErrorARES }
     *
     */
    public void setE(ErrorARES value) {
        this.e = value;
    }

    /**
     * Gets the value of the vh property.
     *
     * @return
     *     possible object is
     *     {@link VysledekHledani }
     *
     */
    public VysledekHledani getVH() {
        return vh;
    }

    /**
     * Sets the value of the vh property.
     *
     * @param value
     *     allowed object is
     *     {@link VysledekHledani }
     *
     */
    public void setVH(VysledekHledani value) {
        this.vh = value;
    }

    /**
     * Gets the value of the pza property.
     *
     */
    public int getPZA() {
        return pza;
    }

    /**
     * Sets the value of the pza property.
     *
     */
    public void setPZA(int value) {
        this.pza = value;
    }

    /**
     * Gets the value of the vypisCEU property.
     *
     * @return
     *     possible object is
     *     {@link VypisCeu }
     *
     */
    public VypisCeu getVypisCEU() {
        return vypisCEU;
    }

    /**
     * Sets the value of the vypisCEU property.
     *
     * @param value
     *     allowed object is
     *     {@link VypisCeu }
     *
     */
    public void setVypisCEU(VypisCeu value) {
        this.vypisCEU = value;
    }

}
