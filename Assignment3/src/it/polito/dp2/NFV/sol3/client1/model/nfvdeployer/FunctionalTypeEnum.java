
package it.polito.dp2.NFV.sol3.client1.model.nfvdeployer;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FunctionalTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FunctionalTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CACHE"/>
 *     &lt;enumeration value="DPI"/>
 *     &lt;enumeration value="FW"/>
 *     &lt;enumeration value="MAIL_CLIENT"/>
 *     &lt;enumeration value="MAIL_SERVER"/>
 *     &lt;enumeration value="NAT"/>
 *     &lt;enumeration value="SPAM"/>
 *     &lt;enumeration value="VPN"/>
 *     &lt;enumeration value="WEB_CLIENT"/>
 *     &lt;enumeration value="WEB_SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FunctionalTypeEnum")
@XmlEnum
public enum FunctionalTypeEnum {

    CACHE,
    DPI,
    FW,
    MAIL_CLIENT,
    MAIL_SERVER,
    NAT,
    SPAM,
    VPN,
    WEB_CLIENT,
    WEB_SERVER;

    public String value() {
        return name();
    }

    public static FunctionalTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
