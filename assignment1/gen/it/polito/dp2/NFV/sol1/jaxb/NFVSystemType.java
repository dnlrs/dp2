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
 * <p>Java class for NFVSystemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NFVSystemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IN" type="{http://www.example.org/nfvInfo}InfrastructureNetwork"/>
 *         &lt;element name="catalogue" type="{http://www.example.org/nfvInfo}Catalogue"/>
 *         &lt;element name="deployedNFFGs">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="nffg" type="{http://www.example.org/nfvInfo}NFFG" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "NFVSystemType", propOrder = {
    "in",
    "catalogue",
    "deployedNFFGs"
})
public class NFVSystemType {

    @XmlElement(name = "IN", required = true)
    protected InfrastructureNetwork in;
    @XmlElement(required = true)
    protected Catalogue catalogue;
    @XmlElement(required = true)
    protected NFVSystemType.DeployedNFFGs deployedNFFGs;

    /**
     * Gets the value of the in property.
     * 
     * @return
     *     possible object is
     *     {@link InfrastructureNetwork }
     *     
     */
    public InfrastructureNetwork getIN() {
        return in;
    }

    /**
     * Sets the value of the in property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfrastructureNetwork }
     *     
     */
    public void setIN(InfrastructureNetwork value) {
        this.in = value;
    }

    /**
     * Gets the value of the catalogue property.
     * 
     * @return
     *     possible object is
     *     {@link Catalogue }
     *     
     */
    public Catalogue getCatalogue() {
        return catalogue;
    }

    /**
     * Sets the value of the catalogue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Catalogue }
     *     
     */
    public void setCatalogue(Catalogue value) {
        this.catalogue = value;
    }

    /**
     * Gets the value of the deployedNFFGs property.
     * 
     * @return
     *     possible object is
     *     {@link NFVSystemType.DeployedNFFGs }
     *     
     */
    public NFVSystemType.DeployedNFFGs getDeployedNFFGs() {
        return deployedNFFGs;
    }

    /**
     * Sets the value of the deployedNFFGs property.
     * 
     * @param value
     *     allowed object is
     *     {@link NFVSystemType.DeployedNFFGs }
     *     
     */
    public void setDeployedNFFGs(NFVSystemType.DeployedNFFGs value) {
        this.deployedNFFGs = value;
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
     *         &lt;element name="nffg" type="{http://www.example.org/nfvInfo}NFFG" maxOccurs="unbounded" minOccurs="0"/>
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
        "nffg"
    })
    public static class DeployedNFFGs {

        protected List<NFFG> nffg;

        /**
         * Gets the value of the nffg property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the nffg property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNffg().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NFFG }
         * 
         * 
         */
        public List<NFFG> getNffg() {
            if (nffg == null) {
                nffg = new ArrayList<NFFG>();
            }
            return this.nffg;
        }

    }

}