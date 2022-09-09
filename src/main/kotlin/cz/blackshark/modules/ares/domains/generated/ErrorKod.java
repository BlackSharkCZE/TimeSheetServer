//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2021.03.20 at 10:11:56 PM CET
//


package cz.blackshark.modules.ares.domains.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for error_kod.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="error_kod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="versionMismatch"/>
 *     &lt;enumeration value="MustUnderstand"/>
 *     &lt;enumeration value="Client"/>
 *     &lt;enumeration value="Client.RequestNamespace"/>
 *     &lt;enumeration value="Client.SchemaNamespace"/>
 *     &lt;enumeration value="Client.Signature"/>
 *     &lt;enumeration value="Client.Validity"/>
 *     &lt;enumeration value="Client.WellFormedness"/>
 *     &lt;enumeration value="Client.XSLTNamespace"/>
 *     &lt;enumeration value="Server"/>
 *     &lt;enumeration value="Server.DatabaseConsistence"/>
 *     &lt;enumeration value="Server.Program"/>
 *     &lt;enumeration value="Server.Service"/>
 *     &lt;enumeration value="Server.SQLStatement"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "error_kod", namespace = "http://wwwinfo.mfcr.cz/ares/xml_doc/schemas/uvis_datatypes/v_1.0.3")
@XmlEnum
public enum ErrorKod {


    /**
     * nalezen neplatný jmenný prostor pro obálku SOAP
     *
     */
    @XmlEnumValue("versionMismatch")
    VERSION_MISMATCH("versionMismatch"),

    /**
     * pro dceřinný element záhlaví SOAP s nastavením atributu mustUnderstand na True přijímající strana zcela neporozuměla obsahu elementu
     *
     */
    @XmlEnumValue("MustUnderstand")
    MUST_UNDERSTAND("MustUnderstand"),

    /**
     * klientská třída chyb, indikující, že zpráva není korektně formátována nebo že neobsahuje požadované informace
     *
     */
    @XmlEnumValue("Client")
    CLIENT("Client"),

    /**
     * nepodporovaná verze dotazu
     *
     */
    @XmlEnumValue("Client.RequestNamespace")
    CLIENT_REQUEST_NAMESPACE("Client.RequestNamespace"),

    /**
     * nepodporovaná verze namespace pro validaci parserem
     *
     */
    @XmlEnumValue("Client.SchemaNamespace")
    CLIENT_SCHEMA_NAMESPACE("Client.SchemaNamespace"),

    /**
     * chyba ověření identity odesílatele pomocí digitálního podpisu
     *
     */
    @XmlEnumValue("Client.Signature")
    CLIENT_SIGNATURE("Client.Signature"),

    /**
     * chyba ověření validity dokumentu
     *
     */
    @XmlEnumValue("Client.Validity")
    CLIENT_VALIDITY("Client.Validity"),

    /**
     * chyba ověření správného strukturování dokumentu
     *
     */
    @XmlEnumValue("Client.WellFormedness")
    CLIENT_WELL_FORMEDNESS("Client.WellFormedness"),

    /**
     * nepodporovaná verze namespace pro XSLT validaci
     *
     */
    @XmlEnumValue("Client.XSLTNamespace")
    CLIENT_XSLT_NAMESPACE("Client.XSLTNamespace"),

    /**
     * serverová třída chyb, indikující, že zpráva není zpracována z důvodu problémů na straně služby
     *
     */
    @XmlEnumValue("Server")
    SERVER("Server"),

    /**
     * nekonzistence dat v databázi služby
     *
     */
    @XmlEnumValue("Server.DatabaseConsistence")
    SERVER_DATABASE_CONSISTENCE("Server.DatabaseConsistence"),

    /**
     * interní chyba serverové aplikace
     *
     */
    @XmlEnumValue("Server.Program")
    SERVER_PROGRAM("Server.Program"),

    /**
     * obecná chyba serverové služby
     *
     */
    @XmlEnumValue("Server.Service")
    SERVER_SERVICE("Server.Service"),

    /**
     * interní chyba SQL dotazu
     *
     */
    @XmlEnumValue("Server.SQLStatement")
    SERVER_SQL_STATEMENT("Server.SQLStatement");
    private final String value;

    ErrorKod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorKod fromValue(String v) {
        for (ErrorKod c: ErrorKod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
