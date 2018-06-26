
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
 *         &lt;element name="nfvArc" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dst" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="throughput" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="latency" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                   &lt;element name="srcLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                   &lt;element name="dstLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
    "nfvArc"
})
@XmlRootElement(name = "nfvArcs")
public class NfvArcs {

    @XmlElement(nillable = true)
    protected List<NfvArcs.NfvArc> nfvArc;

    /**
     * Gets the value of the nfvArc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nfvArc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNfvArc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NfvArcs.NfvArc }
     * 
     * 
     */
    public List<NfvArcs.NfvArc> getNfvArc() {
        if (nfvArc == null) {
            nfvArc = new ArrayList<NfvArcs.NfvArc>();
        }
        return this.nfvArc;
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
     *         &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dst" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="throughput" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="latency" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *         &lt;element name="srcLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *         &lt;element name="dstLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
        "src",
        "dst",
        "throughput",
        "latency",
        "self",
        "srcLink",
        "dstLink"
    })
    public static class NfvArc {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String src;
        @XmlElement(required = true)
        protected String dst;
        protected float throughput;
        protected int latency;
        protected Link self;
        protected Link srcLink;
        protected Link dstLink;

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
         * Gets the value of the src property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSrc() {
            return src;
        }

        /**
         * Sets the value of the src property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSrc(String value) {
            this.src = value;
        }

        /**
         * Gets the value of the dst property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDst() {
            return dst;
        }

        /**
         * Sets the value of the dst property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDst(String value) {
            this.dst = value;
        }

        /**
         * Gets the value of the throughput property.
         * 
         */
        public float getThroughput() {
            return throughput;
        }

        /**
         * Sets the value of the throughput property.
         * 
         */
        public void setThroughput(float value) {
            this.throughput = value;
        }

        /**
         * Gets the value of the latency property.
         * 
         */
        public int getLatency() {
            return latency;
        }

        /**
         * Sets the value of the latency property.
         * 
         */
        public void setLatency(int value) {
            this.latency = value;
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
         * Gets the value of the srcLink property.
         * 
         * @return
         *     possible object is
         *     {@link Link }
         *     
         */
        public Link getSrcLink() {
            return srcLink;
        }

        /**
         * Sets the value of the srcLink property.
         * 
         * @param value
         *     allowed object is
         *     {@link Link }
         *     
         */
        public void setSrcLink(Link value) {
            this.srcLink = value;
        }

        /**
         * Gets the value of the dstLink property.
         * 
         * @return
         *     possible object is
         *     {@link Link }
         *     
         */
        public Link getDstLink() {
            return dstLink;
        }

        /**
         * Sets the value of the dstLink property.
         * 
         * @param value
         *     allowed object is
         *     {@link Link }
         *     
         */
        public void setDstLink(Link value) {
            this.dstLink = value;
        }

    }

}
