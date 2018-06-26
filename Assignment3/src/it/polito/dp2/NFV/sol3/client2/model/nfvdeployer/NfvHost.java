
package it.polito.dp2.NFV.sol3.client2.model.nfvdeployer;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="maxVNFs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hostedNodes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="installedMemory" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="installedStorage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *         &lt;element name="hostedNodesLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
    "name",
    "maxVNFs",
    "hostedNodes",
    "installedMemory",
    "installedStorage",
    "self",
    "hostedNodesLink"
})
@XmlRootElement(name = "nfvHost")
public class NfvHost {

    @XmlElement(required = true)
    protected String name;
    protected int maxVNFs;
    @XmlElement(nillable = true)
    protected List<String> hostedNodes;
    protected int installedMemory;
    protected int installedStorage;
    protected Link self;
    protected Link hostedNodesLink;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the maxVNFs property.
     * 
     */
    public int getMaxVNFs() {
        return maxVNFs;
    }

    /**
     * Sets the value of the maxVNFs property.
     * 
     */
    public void setMaxVNFs(int value) {
        this.maxVNFs = value;
    }

    /**
     * Gets the value of the hostedNodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hostedNodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHostedNodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getHostedNodes() {
        if (hostedNodes == null) {
            hostedNodes = new ArrayList<String>();
        }
        return this.hostedNodes;
    }

    /**
     * Gets the value of the installedMemory property.
     * 
     */
    public int getInstalledMemory() {
        return installedMemory;
    }

    /**
     * Sets the value of the installedMemory property.
     * 
     */
    public void setInstalledMemory(int value) {
        this.installedMemory = value;
    }

    /**
     * Gets the value of the installedStorage property.
     * 
     */
    public int getInstalledStorage() {
        return installedStorage;
    }

    /**
     * Sets the value of the installedStorage property.
     * 
     */
    public void setInstalledStorage(int value) {
        this.installedStorage = value;
    }

    /**
     * Gets the value of the self property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getSelf() {
        return self;
    }

    /**
     * Sets the value of the self property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setSelf(Link value) {
        this.self = value;
    }

    /**
     * Gets the value of the hostedNodesLink property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getHostedNodesLink() {
        return hostedNodesLink;
    }

    /**
     * Sets the value of the hostedNodesLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setHostedNodesLink(Link value) {
        this.hostedNodesLink = value;
    }

}
