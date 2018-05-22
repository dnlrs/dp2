//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.22 at 10:55:01 PM CEST 
//


package it.polito.dp2.NFV.sol1.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Host complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Host">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="maxVNFs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="installedMemory" type="{http://www.example.org/nfvInfo}sizeInMB"/>
 *         &lt;element name="installedStorage" type="{http://www.example.org/nfvInfo}sizeInMB"/>
 *         &lt;element name="allocatedNodes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="node" type="{http://www.example.org/nfvInfo}NodeRef" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Host", propOrder = {
    "name",
    "maxVNFs",
    "installedMemory",
    "installedStorage",
    "allocatedNodes"
})
public class Host {

    @XmlElement(required = true)
    protected String name;
    protected int maxVNFs;
    @XmlElement(required = true)
    protected SizeInMB installedMemory;
    @XmlElement(required = true)
    protected SizeInMB installedStorage;
    @XmlElement(required = true)
    protected Host.AllocatedNodes allocatedNodes;

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
     * Gets the value of the installedMemory property.
     * 
     * @return
     *     possible object is
     *     {@link SizeInMB }
     *     
     */
    public SizeInMB getInstalledMemory() {
        return installedMemory;
    }

    /**
     * Sets the value of the installedMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link SizeInMB }
     *     
     */
    public void setInstalledMemory(SizeInMB value) {
        this.installedMemory = value;
    }

    /**
     * Gets the value of the installedStorage property.
     * 
     * @return
     *     possible object is
     *     {@link SizeInMB }
     *     
     */
    public SizeInMB getInstalledStorage() {
        return installedStorage;
    }

    /**
     * Sets the value of the installedStorage property.
     * 
     * @param value
     *     allowed object is
     *     {@link SizeInMB }
     *     
     */
    public void setInstalledStorage(SizeInMB value) {
        this.installedStorage = value;
    }

    /**
     * Gets the value of the allocatedNodes property.
     * 
     * @return
     *     possible object is
     *     {@link Host.AllocatedNodes }
     *     
     */
    public Host.AllocatedNodes getAllocatedNodes() {
        return allocatedNodes;
    }

    /**
     * Sets the value of the allocatedNodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Host.AllocatedNodes }
     *     
     */
    public void setAllocatedNodes(Host.AllocatedNodes value) {
        this.allocatedNodes = value;
    }


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
     *         &lt;element name="node" type="{http://www.example.org/nfvInfo}NodeRef" maxOccurs="unbounded"/>
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
        "node"
    })
    public static class AllocatedNodes {

        @XmlElement(required = true)
        protected List<NodeRef> node;

        /**
         * Gets the value of the node property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the node property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NodeRef }
         * 
         * 
         */
        public List<NodeRef> getNode() {
            if (node == null) {
                node = new ArrayList<NodeRef>();
            }
            return this.node;
        }

    }

}
