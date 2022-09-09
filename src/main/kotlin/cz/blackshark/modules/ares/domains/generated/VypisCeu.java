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
import javax.xml.bind.annotation.XmlType;


/**
 * Výpis informací o úpadci z Centrální evidence úpadců dle ARES
 *
 * <p>Java class for vypis_ceu complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="vypis_ceu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UVOD" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}uvod"/>
 *         &lt;element name="Subjekt" type="{http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3}subjekt_CEU" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vypis_ceu", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", propOrder = {
    "uvod",
    "subjekt"
})
public class VypisCeu {

    @XmlElement(name = "UVOD", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected Uvod uvod;
    @XmlElement(name = "Subjekt", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/ares/ares_datatypes/v_1.0.3", required = true)
    protected List<SubjektCEU> subjekt;

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
     * Gets the value of the subjekt property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjekt property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjekt().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubjektCEU }
     *
     *
     */
    public List<SubjektCEU> getSubjekt() {
        if (subjekt == null) {
            subjekt = new ArrayList<SubjektCEU>();
        }
        return this.subjekt;
    }

}
