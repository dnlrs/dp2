//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.25 at 08:28:31 PM CEST 
//


package it.polito.dp2.NFV.sol3.service.model.nfvdeployer;

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
@XmlRootElement(name = "nfvArc")
public class NfvArc {

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
