
package it.polito.dp2.NFV.sol3.client1.model.nfvdeployer;

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
 *         &lt;element name="nfvVNF" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="functionalType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="requiredMemory" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="requiredStorage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "nfvVNF"
})
@XmlRootElement(name = "nfvVNFs")
public class NfvVNFs {

    @XmlElement(nillable = true)
    protected List<NfvVNFs.NfvVNF> nfvVNF;

    /**
     * Gets the value of the nfvVNF property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nfvVNF property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNfvVNF().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NfvVNFs.NfvVNF }
     * 
     * 
     */
    public List<NfvVNFs.NfvVNF> getNfvVNF() {
        if (nfvVNF == null) {
            nfvVNF = new ArrayList<NfvVNFs.NfvVNF>();
        }
        return this.nfvVNF;
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
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="functionalType" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    public static class NfvVNF {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String functionalType;
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
         *     {@link String }
         *     
         */
        public String getFunctionalType() {
            return functionalType;
        }

        /**
         * Sets the value of the functionalType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFunctionalType(String value) {
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

}
