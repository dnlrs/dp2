
package it.polito.dp2.NFV.sol3.client1.model.nfvdeployer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *         &lt;element name="hostsLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *         &lt;element name="connectionsLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *         &lt;element name="vnfsLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *         &lt;element name="nffgsLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *         &lt;element name="nodesLink" type="{http://www.example.org/NfvDeployer}Link"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceLink",
    "hostsLink",
    "connectionsLink",
    "vnfsLink",
    "nffgsLink",
    "nodesLink"
})
@XmlRootElement(name = "services")
public class Services {

    @XmlElement(required = true)
    protected Link serviceLink;
    @XmlElement(required = true)
    protected Link hostsLink;
    @XmlElement(required = true)
    protected Link connectionsLink;
    @XmlElement(required = true)
    protected Link vnfsLink;
    @XmlElement(required = true)
    protected Link nffgsLink;
    @XmlElement(required = true)
    protected Link nodesLink;

    /**
     * Gets the value of the serviceLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getServiceLink() {
        return serviceLink;
    }

    /**
     * Sets the value of the serviceLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setServiceLink(Link value) {
        this.serviceLink = value;
    }

    /**
     * Gets the value of the hostsLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getHostsLink() {
        return hostsLink;
    }

    /**
     * Sets the value of the hostsLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setHostsLink(Link value) {
        this.hostsLink = value;
    }

    /**
     * Gets the value of the connectionsLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getConnectionsLink() {
        return connectionsLink;
    }

    /**
     * Sets the value of the connectionsLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setConnectionsLink(Link value) {
        this.connectionsLink = value;
    }

    /**
     * Gets the value of the vnfsLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getVnfsLink() {
        return vnfsLink;
    }

    /**
     * Sets the value of the vnfsLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setVnfsLink(Link value) {
        this.vnfsLink = value;
    }

    /**
     * Gets the value of the nffgsLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getNffgsLink() {
        return nffgsLink;
    }

    /**
     * Sets the value of the nffgsLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setNffgsLink(Link value) {
        this.nffgsLink = value;
    }

    /**
     * Gets the value of the nodesLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getNodesLink() {
        return nodesLink;
    }

    /**
     * Sets the value of the nodesLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setNodesLink(Link value) {
        this.nodesLink = value;
    }

}
