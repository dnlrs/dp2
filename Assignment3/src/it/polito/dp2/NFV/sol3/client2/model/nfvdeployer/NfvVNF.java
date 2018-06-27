//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.27 at 11:25:45 PM CEST 
//


package it.polito.dp2.NFV.sol3.client2.model.nfvdeployer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="functionalType" type="{http://www.example.org/NfvDeployer}FunctionalTypeEnum"/>
 *         &lt;element name="requiredMemory" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="requiredStorage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
    "functionalType",
    "requiredMemory",
    "requiredStorage",
    "self"
})
@XmlRootElement(name = "nfvVNF")
public class NfvVNF {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected FunctionalTypeEnum functionalType;
    protected int requiredMemory;
    protected int requiredStorage;
    protected Link self;

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
     * Gets the value of the functionalType property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionalTypeEnum }
     *     
     */
    public FunctionalTypeEnum getFunctionalType() {
        return functionalType;
    }

    /**
     * Sets the value of the functionalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionalTypeEnum }
     *     
     */
    public void setFunctionalType(FunctionalTypeEnum value) {
        this.functionalType = value;
    }

    /**
     * Gets the value of the requiredMemory property.
     * 
     */
    public int getRequiredMemory() {
        return requiredMemory;
    }

    /**
     * Sets the value of the requiredMemory property.
     * 
     */
    public void setRequiredMemory(int value) {
        this.requiredMemory = value;
    }

    /**
     * Gets the value of the requiredStorage property.
     * 
     */
    public int getRequiredStorage() {
        return requiredStorage;
    }

    /**
     * Sets the value of the requiredStorage property.
     * 
     */
    public void setRequiredStorage(int value) {
        this.requiredStorage = value;
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

}
