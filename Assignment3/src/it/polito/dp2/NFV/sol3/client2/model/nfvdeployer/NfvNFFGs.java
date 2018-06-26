
package it.polito.dp2.NFV.sol3.client2.model.nfvdeployer;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="nfvNFFG" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="deployTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element name="nfvNode" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="functionalType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="hostingHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="associatedNFFG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="nfvArc" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="dst" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="throughput" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                       &lt;element name="latency" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                       &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                                       &lt;element name="srcLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                                       &lt;element name="dstLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                             &lt;element name="functionalTypeLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                             &lt;element name="hostingHostLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                             &lt;element name="associatedNFFGLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                             &lt;element name="linksLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                             &lt;element name="reachableHostsLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                   &lt;element name="linksLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
 *                   &lt;element name="allocatedNodesLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
    "nfvNFFG"
})
@XmlRootElement(name = "nfvNFFGs")
public class NfvNFFGs {

    @XmlElement(nillable = true)
    protected List<NfvNFFGs.NfvNFFG> nfvNFFG;

    /**
     * Gets the value of the nfvNFFG property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nfvNFFG property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNfvNFFG().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NfvNFFGs.NfvNFFG }
     * 
     * 
     */
    public List<NfvNFFGs.NfvNFFG> getNfvNFFG() {
        if (nfvNFFG == null) {
            nfvNFFG = new ArrayList<NfvNFFGs.NfvNFFG>();
        }
        return this.nfvNFFG;
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
     *         &lt;element name="deployTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element name="nfvNode" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="functionalType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="hostingHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="associatedNFFG" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="nfvArc" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="src" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="dst" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="throughput" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                             &lt;element name="latency" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                             &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                             &lt;element name="srcLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                             &lt;element name="dstLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                   &lt;element name="functionalTypeLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                   &lt;element name="hostingHostLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                   &lt;element name="associatedNFFGLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                   &lt;element name="linksLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                   &lt;element name="reachableHostsLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *         &lt;element name="linksLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
     *         &lt;element name="allocatedNodesLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
        "deployTime",
        "nfvNode",
        "self",
        "linksLink",
        "allocatedNodesLink"
    })
    public static class NfvNFFG {

        @XmlElement(required = true)
        protected String name;
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar deployTime;
        @XmlElement(nillable = true)
        protected List<NfvNFFGs.NfvNFFG.NfvNode> nfvNode;
        protected Link self;
        protected Link linksLink;
        protected Link allocatedNodesLink;

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
         * Gets the value of the deployTime property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDeployTime() {
            return deployTime;
        }

        /**
         * Sets the value of the deployTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDeployTime(XMLGregorianCalendar value) {
            this.deployTime = value;
        }

        /**
         * Gets the value of the nfvNode property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the nfvNode property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNfvNode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NfvNFFGs.NfvNFFG.NfvNode }
         * 
         * 
         */
        public List<NfvNFFGs.NfvNFFG.NfvNode> getNfvNode() {
            if (nfvNode == null) {
                nfvNode = new ArrayList<NfvNFFGs.NfvNFFG.NfvNode>();
            }
            return this.nfvNode;
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
         * Gets the value of the linksLink property.
         * 
         * @return
         *     possible object is
         *     {@link Link }
         *     
         */
        public Link getLinksLink() {
            return linksLink;
        }

        /**
         * Sets the value of the linksLink property.
         * 
         * @param value
         *     allowed object is
         *     {@link Link }
         *     
         */
        public void setLinksLink(Link value) {
            this.linksLink = value;
        }

        /**
         * Gets the value of the allocatedNodesLink property.
         * 
         * @return
         *     possible object is
         *     {@link Link }
         *     
         */
        public Link getAllocatedNodesLink() {
            return allocatedNodesLink;
        }

        /**
         * Sets the value of the allocatedNodesLink property.
         * 
         * @param value
         *     allowed object is
         *     {@link Link }
         *     
         */
        public void setAllocatedNodesLink(Link value) {
            this.allocatedNodesLink = value;
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
         *         &lt;element name="hostingHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="associatedNFFG" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
         *         &lt;element name="self" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
         *         &lt;element name="functionalTypeLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
         *         &lt;element name="hostingHostLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
         *         &lt;element name="associatedNFFGLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
         *         &lt;element name="linksLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
         *         &lt;element name="reachableHostsLink" type="{http://www.example.org/NfvDeployer}Link" minOccurs="0"/>
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
            "hostingHost",
            "associatedNFFG",
            "nfvArc",
            "self",
            "functionalTypeLink",
            "hostingHostLink",
            "associatedNFFGLink",
            "linksLink",
            "reachableHostsLink"
        })
        public static class NfvNode {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String functionalType;
            protected String hostingHost;
            @XmlElement(required = true)
            protected String associatedNFFG;
            @XmlElement(nillable = true)
            protected List<NfvNFFGs.NfvNFFG.NfvNode.NfvArc> nfvArc;
            protected Link self;
            protected Link functionalTypeLink;
            protected Link hostingHostLink;
            protected Link associatedNFFGLink;
            protected Link linksLink;
            protected Link reachableHostsLink;

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
             * Gets the value of the hostingHost property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHostingHost() {
                return hostingHost;
            }

            /**
             * Sets the value of the hostingHost property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHostingHost(String value) {
                this.hostingHost = value;
            }

            /**
             * Gets the value of the associatedNFFG property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAssociatedNFFG() {
                return associatedNFFG;
            }

            /**
             * Sets the value of the associatedNFFG property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAssociatedNFFG(String value) {
                this.associatedNFFG = value;
            }

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
             * {@link NfvNFFGs.NfvNFFG.NfvNode.NfvArc }
             * 
             * 
             */
            public List<NfvNFFGs.NfvNFFG.NfvNode.NfvArc> getNfvArc() {
                if (nfvArc == null) {
                    nfvArc = new ArrayList<NfvNFFGs.NfvNFFG.NfvNode.NfvArc>();
                }
                return this.nfvArc;
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
             * Gets the value of the functionalTypeLink property.
             * 
             * @return
             *     possible object is
             *     {@link Link }
             *     
             */
            public Link getFunctionalTypeLink() {
                return functionalTypeLink;
            }

            /**
             * Sets the value of the functionalTypeLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link Link }
             *     
             */
            public void setFunctionalTypeLink(Link value) {
                this.functionalTypeLink = value;
            }

            /**
             * Gets the value of the hostingHostLink property.
             * 
             * @return
             *     possible object is
             *     {@link Link }
             *     
             */
            public Link getHostingHostLink() {
                return hostingHostLink;
            }

            /**
             * Sets the value of the hostingHostLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link Link }
             *     
             */
            public void setHostingHostLink(Link value) {
                this.hostingHostLink = value;
            }

            /**
             * Gets the value of the associatedNFFGLink property.
             * 
             * @return
             *     possible object is
             *     {@link Link }
             *     
             */
            public Link getAssociatedNFFGLink() {
                return associatedNFFGLink;
            }

            /**
             * Sets the value of the associatedNFFGLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link Link }
             *     
             */
            public void setAssociatedNFFGLink(Link value) {
                this.associatedNFFGLink = value;
            }

            /**
             * Gets the value of the linksLink property.
             * 
             * @return
             *     possible object is
             *     {@link Link }
             *     
             */
            public Link getLinksLink() {
                return linksLink;
            }

            /**
             * Sets the value of the linksLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link Link }
             *     
             */
            public void setLinksLink(Link value) {
                this.linksLink = value;
            }

            /**
             * Gets the value of the reachableHostsLink property.
             * 
             * @return
             *     possible object is
             *     {@link Link }
             *     
             */
            public Link getReachableHostsLink() {
                return reachableHostsLink;
            }

            /**
             * Sets the value of the reachableHostsLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link Link }
             *     
             */
            public void setReachableHostsLink(Link value) {
                this.reachableHostsLink = value;
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

    }

}
