
# NfvDeployer REST API Documentation

## Resources

Resource                                | XML representation |  URL                               | Meaning
--------------------------------------- | ------------------ | ---------------------------------- | -------------------------------------------------
services                                | services           | /services                          | the set of all collections accessible through this API
hosts                                   | nfvHosts           | /hosts                             | the set of all Hosts in the NFV System
hosts > {hostName}                      | nfvHost            | /hosts/{hostName}                  | a single Host
hosts > {hostName} > nodes              | nfvNodes           | /hosts/{hostName}/nodes            | the set of Nodes allocated on this Host
connections                             | nfvArcs            | /connections                       | the set of all Connections in the NFV System
vnfs                                    | nfvVNFs            | /vnfs                              | the set of all VNFs in the NFV System
vnfs > {vnfName}                        | nfvVNF             | /vnfs/{vnfName}                    | a single VNF  
nffgs                                   | nfvNFFGs           | /nffgs                             | the set of all NFFGs in the NFV System  
nffgs > {nffgName}                      | nfvNFFG            | /nffgs/{nffgName}                  | a single NFFG  
nffgs > {nffgName} > nodes              | nfvNodes           | /nffgs/{nffgName}/nodes            | the set of all Nodes in this NFFG  
nffgs > {nffgName} > links              | nfvArcs            | /nffgs/{nffgName}/links            | the set of all Links in this NFFG  
nffgs > {nffgName} > links > {linkName} | nfvArc             | /nffgs/{nffgName}/links/{linkName} | a single Link in this NFFG  
nodes                                   | nfvNodes           | /nodes                             | the set of all Nodes in the NFV System  
nodes > {nodeName}                      | nfvNode            | /nodes/{nodeName}                  | a single Node  
nodes > {nodeName} > reachableHosts     | nfvHosts           | /nodes/{nodeName}/reachableHosts   | the set of all reachable Hosts from this Node  
nodes > {nodeName} > links              | nfvArcs            | /nodes/{nodeName}/links            | the set of all links with this Node as the source


## Operations

### Operations on Hosts

#### Get all Hosts in the NFV System

```HTTP
GET .../hosts[?page=??&itemsPerPage=??]
```

###### Content Type
 - application/xml

###### Query options

Name          | Value Type  | Use      | Default
--------------|-------------|----------|----------
page          | int         | optional | 0 (all pages)
itemsPerPage  | int         | optional | 20

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvHosts
204           | No Content      |
500           | Server Error    |

###### Example Request

```HTTP
GET http://localhost:8080/NfvDeployer/rest/hosts/hosts
```

###### Example Response

*200* OK

```XML
<nfvHosts xmlns="http://www.example.org/NfvDeployer">
 <nfvHost>
  <name>H0</name>
  <maxVNFs>7</maxVNFs>
  <hostedNodes>NATc0Nffg0</hostedNodes>
  <hostedNodes>WEBCLIENTt0Nffg0</hostedNodes>
  <installedMemory>7900</installedMemory>
  <installedStorage>15000</installedStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <hostedNodesLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0/nodes"/>
 </nfvHost>
 <nfvHost>
  <name>H1</name>
  <maxVNFs>12</maxVNFs>
  <hostedNodes>VPNd1Nffg0</hostedNodes>
  <hostedNodes>WEBCLIENTt1Nffg0</hostedNodes>
  <installedMemory>3500</installedMemory>
  <installedStorage>86000</installedStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/hosts/H1"/>
  <hostedNodesLink href="http://localhost:8080/NfvDeployer/rest/hosts/H1/nodes"/>
 </nfvHost>
</nfvHosts>
```



#### Get a specific Host

```HTTP
GET .../hosts/{hostName}
```

###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvHost
400           | Bad Request     |
404           | Not Found       |
500           | Server Error    |


###### Example Request

```HTTP
GET http://localhost:8080/NfvDeployer/rest/hosts/hosts/H0
```

###### Example Response

*200* OK

```XML
<nfvHost xmlns="http://www.example.org/NfvDeployer">
 <name>H0</name>
 <maxVNFs>7</maxVNFs>
 <hostedNodes>NATc0Nffg0</hostedNodes>
 <hostedNodes>WEBCLIENTt0Nffg0</hostedNodes>
 <installedMemory>7900</installedMemory>
 <installedStorage>15000</installedStorage>
 <self href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
 <hostedNodesLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0/nodes"/>
</nfvHost>
```

#### Get all Nodes hosted on a specific Host

```HTTP
GET .../hosts/{hostName}/nodes
```

###### Content Type
 - application/xml

###### Query options

Name          | Value Type  | Use      | Default
--------------|-------------|----------|---------
detailed      | int         | optional | 1 (`true`)

*detailed* may be set to **0** (`false`) or **1** (`true`)

if *detailed* is `true` then for each Node also the Links starting from that Node
will be reported

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNodes
204           | No content      |
400           | Bad Request     |
404           | Not Found       |
500           | Server Error    |

###### Example Request

```HTTP
GET http://localhost:8080/NfvDeployer/rest/hosts/hosts/H0/nodes
```

###### Example Response

*200* OK

```XML
<nfvNodes xmlns="http://www.example.org/NfvDeployer">
 <nfvNode>
  <name>NATc0Nffg0</name>
  <functionalType>NATc</functionalType>
  <hostingHost>H0</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <nfvArc>
   <name>Link7</name>
   <src>NATc0Nffg0</src>
   <dst>WEBSERVERt1Nffg0</dst>
   <throughput>0.0</throughput>
   <latency>0</latency>
   <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link7"/>
   <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/NATc0Nffg0"/>
   <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/WEBSERVERt1Nffg0"/>
  </nfvArc>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/NATc0Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/NATc"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/NATc0Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/NATc0Nffg0/reachableHosts"/>
 </nfvNode>
 <nfvNode>
  <name>WEBCLIENTt0Nffg0</name>
  <functionalType>WEBCLIENTt</functionalType>
  <hostingHost>H0</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/WEBCLIENTt0Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/WEBCLIENTt"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/WEBCLIENTt0Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/WEBCLIENTt0Nffg0/reachableHosts"/>
 </nfvNode>
</nfvNodes>
```


### Operations on Connections

#### Get all Connections

```HTTP
GET .../connections[?sourceHost=??&destinationHost=??]
```

If *sourceHost* and *destinationHost* are specified, only the connection between these Hosts, if it exists, will be returned.

If only *sourceHost* or *destinationHost* are specified only connections starting from *sourceHost* or arriving to *destinationHost* will be returned accordingly.

If no query is specified, all connections in the NFV System will be returned.

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
sourceHost      | string      | optional | null
destinationHost | string      | optional | null

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvArcs
204           | No content      |
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/connections
```
###### Example Response
*200* OK
```XML
<nfvArcs xmlns="http://www.example.org/NfvDeployer">
 <nfvArc>
  <name>H0TOH0</name>
  <src>H0</src>
  <dst>H0</dst>
  <throughput>74.88</throughput>
  <latency>0</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/connections?sourceHost=H0&amp;destinationHost=H0"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
 </nfvArc>
 <nfvArc>
  <name>H0TOH1</name>
  <src>H0</src>
  <dst>H1</dst>
  <throughput>16.54</throughput>
  <latency>44</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/connections?sourceHost=H0&amp;destinationHost=H1"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/hosts/H1"/>
 </nfvArc>
</nfvArcs>
```




### Operations on VNFs

#### Get all VNFs (the Catalogue)

```HTTP
GET .../vnfs
```

###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvVNFs
204           | No content      |
500           | Server Error    |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/vnfs
```
###### Example Response
*200* OK
```XML
<nfvVNFs xmlns="http://www.example.org/NfvDeployer">
 <nfvVNF>
  <name>CACHEa</name>
  <functionalType>CACHE</functionalType>
  <requiredMemory>39</requiredMemory>
  <requiredStorage>110</requiredStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
 </nfvVNF>
 <nfvVNF>
  <name>FWb</name>
  <functionalType>FW</functionalType>
  <requiredMemory>47</requiredMemory>
  <requiredStorage>690</requiredStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/vnfs/FWb"/>
 </nfvVNF>
</nfvVNFs>
```


#### Get a specific VNF

```HTTP
GET .../vnfs/{vnfName}
```

###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvVNF
204           | No content      |
500           | Server Error    |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa
```
###### Example Response
*200* OK
```XML
<nfvVNF xmlns="http://www.example.org/NfvDeployer">
 <name>CACHEa</name>
 <functionalType>CACHE</functionalType>
 <requiredMemory>39</requiredMemory>
 <requiredStorage>110</requiredStorage>
 <self href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
</nfvVNF>
```



### Operations on NFFGs


#### Get all NFFGs

```HTTP
GET .../nffgs[?detailed=??&page=??&itemsPerPage=??&date=??]
```

If *page* number is specified the results will be returned paginated.

If *detailed* is `true` for each NFFG also details about allocated Nodes (and their Links) will be returned.

if *date* is specified only NFFG deployed after specified date will be returned.  

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
detailed        | int         | optional | 1 (`true`)
date            | dateTime    | optional |
page            | int         | optional | 0 (all pages)
itemsPerPage    | int         | optional | 20

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNFFGs
204           | No content      |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nffgs
```
###### Example Response
*200* OK
```XML
<nfvNFFGs xmlns="http://www.example.org/NfvDeployer">
 <nfvNFFG>
  <name>Nffg0</name>
  <deployTime>2016-09-28T16:11:20.000+02:00</deployTime>
  <nfvNode>
   <name>CACHEa4Nffg0</name>
   <functionalType>CACHEa</functionalType>
   <hostingHost>H8</hostingHost>
   <associatedNFFG>Nffg0</associatedNFFG>
   <self href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0"/>
   <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
   <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H8"/>
   <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
   <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/links"/>
   <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/reachableHosts"/>
  </nfvNode>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <allocatedNodesLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/nodes"/>
 </nfvNFFG>
 <nfvNFFG>
  <name>Nffg1</name>
  <deployTime>2016-10-28T16:08:20.000+02:00</deployTime>
  <nfvNode>
   <name>CACHEa4Nffg1</name>
   <functionalType>CACHEa</functionalType>
   <hostingHost>H8</hostingHost>
   <associatedNFFG>Nffg1</associatedNFFG>
   <self href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg1"/>
   <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
   <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H8"/>
   <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg1"/>
   <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/links"/>
   <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg1/reachableHosts"/>
  </nfvNode>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg1"/>
  <allocatedNodesLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg1/nodes"/>
 </nfvNFFG>

</nfvNFFGs>
```

#### Get a specific NFFG
```HTTP
GET .../nffgs/{nffgName}[?detailed=??]
```

If *detailed* is `true` then also details about allocated Nodes (and their Links) will be returned.

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
detailed        | int         | optional | 1 (`true`)

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNFFG
400           | Bad Request     |
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0
```
###### Example Response
*200* OK
```XML
<nfvNFFG xmlns="http://www.example.org/NfvDeployer">
 <name>Nffg0</name>
 <deployTime>2016-09-28T16:11:20.000+02:00</deployTime>
 <nfvNode>
  <name>CACHEa4Nffg0</name>
  <functionalType>CACHEa</functionalType>
  <hostingHost>H8</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H8"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/reachableHosts"/>
 </nfvNode>
 <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
 <allocatedNodesLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/nodes"/>
</nfvNFFG>
```

#### Deploy a new NFFG
```HTTP
POST .../nffgs
```

###### Content Type
 - application/xml


###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNFFG
400           | Bad Request     |
403           | Forbidden       |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0
```
*body*
```XML
<nfvNFFG>
  <name>Nffg0</name>
  <nfvNode>
   <name>CACHEa4Nffg0</name>
   <functionalType>CACHEa</functionalType>
   <hostingHost>H8</hostingHost>
   <associatedNFFG>Nffg0</associatedNFFG>
  </nfvNode>
 </nfvNFFG>
```
###### Example Response
*200 OK*
```XML
<nfvNFFG>
  <name>Nffg0</name>
  <deployTime>2016-09-28T16:11:20.000+02:00</deployTime>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <allocatedNodesLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/nodes"/>
 </nfvNFFG>
```

#### Undeploy an NFFG
```HTTP
DELETE .../nffg/{nffgName}
```

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
204           | No Content      | 
404           | Not Found       |

###### Example Request
```HTML
DELETE http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0
```
###### Example Response
*204 NO CONTENT*



#### Get all Nodes belonging to a specific NFFG

```HTTP
GET .../nffgs/{nffgName}/nodes[?detailed=??&page=??&itemsPerPage=??]
```

If *page* number is specified the results will be returned paginated.

If *detailed* is `true` for each Node also details about their Links will be returned.

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
detailed        | int         | optional | 1 (`true`)
page            | int         | optional | 0 (all pages)
itemsPerPage    | int         | optional | 20

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNodes
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/nodes
```
###### Example Response
*200* OK
```XML
<nfvNodes xmlns="http://www.example.org/NfvDeployer">
 <nfvNode>
  <name>CACHEa4Nffg0</name>
  <functionalType>CACHEa</functionalType>
  <hostingHost>H8</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/CACHEa"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H8"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/CACHEa4Nffg0/reachableHosts"/>
 </nfvNode>
 <nfvNode>
  <name>FWb6Nffg0</name>
  <functionalType>FWb</functionalType>
  <hostingHost>H1</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <nfvArc>
   <name>Link1</name>
   <src>FWb6Nffg0</src>
   <dst>MAILCLIENTt0Nffg0</dst>
   <throughput>0.0</throughput>
   <latency>0</latency>
   <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link1"/>
   <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0"/>
   <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
  </nfvArc>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/FWb"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H1"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0/reachableHosts"/>
 </nfvNode>
</nfvNodes>
```


#### Get all Links existing in a specific NFFG

```HTTP
GET .../nffgs/{nffgName}/links[?page=??&itemsPerPage=??]
```

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
page            | int         | optional | 0 (all pages)
itemsPerPage    | int         | optional | 20

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvArcs
204           | No Content      |
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links
```
###### Example Response
*200* OK
```XML
<nfvArcs xmlns="http://www.example.org/NfvDeployer">
 <nfvArc>
  <name>Link0</name>
  <src>MAILCLIENTt0Nffg0</src>
  <dst>FWb6Nffg0</dst>
  <throughput>0.0</throughput>
  <latency>0</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0"/>
 </nfvArc>
 <nfvArc>
  <name>Link1</name>
  <src>FWb6Nffg0</src>
  <dst>MAILCLIENTt0Nffg0</dst>
  <throughput>0.0</throughput>
  <latency>0</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link1"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWb6Nffg0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/></nfvArc>
</nfvArcs>
```

#### Get a specific Link in a specific NFFG

```HTTP
GET .../nffgs/{nffgName}/links/{linkName}
```
###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvArc
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0
```
###### Example Response
*200* Ok
```XML
<nfvArc xmlns="http://www.example.org/NfvDeployer">
 <name>Link0</name>
 <src>MAILCLIENTt0Nffg0</src>
 <dst>FWa0Nffg0</dst>
 <throughput>0.0</throughput>
 <latency>0</latency>
 <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0"/>
 <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
 <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
</nfvArc>
```


#### Add a new Link in an NFFG
```HTTP
POST .../nffgs/{nffgName}/links[?update={0|1}]
```

If *update* is set to 1, if the link exists already it will be updated.
If instead is set to 0, if the link exists already an error will be returned.

###### Content Type
 - application/xml
 
###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
update          | int         | optional | 1 
 
###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvArc
400           | Bad Request     |
403           | Forbidden       |
500           | Server Error    |

###### Example Request
```HTTP
POST http:/localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links
```
```XML
<nfvArc xmlns="http://www.example.org/NfvDeployer">
 <name>Link0</name>
 <src>MAILCLIENTt0Nffg0</src>
 <dst>FWa0Nffg0</dst>
 <throughput>0.0</throughput>
 <latency>0</latency>
</nfvArc>
```

###### Example Response
*200* Ok
```XML
<nfvArc xmlns="http://www.example.org/NfvDeployer">
 <name>Link0</name>
 <src>MAILCLIENTt0Nffg0</src>
 <dst>FWa0Nffg0</dst>
 <throughput>0.0</throughput>
 <latency>0</latency>
 <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0"/>
 <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
 <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
</nfvArc>
```


#### Delete a Link in an NFFG
```HTTP
DELETE .../nffgs/{nffgName}/links/{linkName}
```

If *update* is set to 1, if the link exists already it will be updated.
If instead is set to 0, if the link exists already an error will be returned.

###### Content Type
 - application/xml
 
###### Responses

Code          | Status          | Body
--------------|-----------------|------------
204           | No Content      | 
404           | Not Found       |

###### Example Request
```HTTP
DELETE http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0
```

###### Example Response
*204 No Content*


### Operations on Nodes

#### Get all Nodes

```HTTP
GET .../nodes[?detailed=??&page=??&itemsPerPage=??]
```

If *detailed* is **1** (`true`) then for each node also detailed info about its Links is returned.

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
detailed        | int         | optional | 1 (`true`)
page            | int         | optional | 0 (all pages)
itemsPerPage    | int         | optional | 20

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNodes
204           | No Content      |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nodes
```
###### Example Response
*200* OK
```XML
<nfvNodes>
 <nfvNode>
  <name>FWa0Nffg0</name>
  <functionalType>FWa</functionalType>
  <hostingHost>H3</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <nfvArc>
   <name>Link3</name>
   <src>FWa0Nffg0</src>
   <dst>WEBCLIENTt1Nffg0</dst>
   <throughput>0.0</throughput>
   <latency>0</latency>
   <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link3"/>
   <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
   <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/WEBCLIENTt1Nffg0"/>
  </nfvArc>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/FWa"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H3"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0/reachableHosts"/>
 </nfvNode>
 <nfvNode>
  <name>MAILCLIENTt0Nffg0</name>
  <functionalType>MAILCLIENTt</functionalType>
  <hostingHost>H0</hostingHost>
  <associatedNFFG>Nffg0</associatedNFFG>
  <self href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
  <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/MAILCLIENTt"/>
  <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
  <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/links"/>
  <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/reachableHosts"/>
 </nfvNode>
</nfvNodes>
```


#### Get a specific Node

```HTTP
GET .../nodes/{nodeName}[?detailed=??]
```

###### Content Type
 - application/xml

###### Query options

Name            | Value Type  | Use      | Default
----------------|-------------|----------|---------
detailed        | int         | optional | 1 (`true`)

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNode
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTML
GET http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0
```
###### Example Response
*200* OK
```XML
<nfvNode xmlns="http://www.example.org/NfvDeployer">
 <name>MAILCLIENTt0Nffg0</name>
 <functionalType>MAILCLIENTt</functionalType>
 <hostingHost>H0</hostingHost>
 <associatedNFFG>Nffg0</associatedNFFG>
 <nfvArc>
  <name>Link0</name>
  <src>MAILCLIENTt0Nffg0</src>
  <dst>FWa0Nffg0</dst>
  <throughput>0.0</throughput>
  <latency>0</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
 </nfvArc>
 <self href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
 <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/MAILCLIENTt"/>
 <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
 <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
 <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/links"/>
 <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/reachableHosts"/>
</nfvNode>
```

#### Add a new Node (and deploy it)
```HTTP
POST .../nodes
```

**Note:**

The user may specify the suggested hosting host by adding the *hostingHost* element in the XML body. 
If possible, the system will try to allocate the node on that particular host.

###### Content Type
 - application/xml
 
###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvNode
400           | Bad Request     |
403           | Forbidden       |
500           | Server Error    |

###### Example Request
```HTML
POST http://localhost:8080/NfvDeployer/rest/nodes
```
```XML
<nfvNode xmlns="http://www.example.org/NfvDeployer">
 <name>MAILCLIENTt0Nffg0</name>
 <functionalType>MAILCLIENTt</functionalType>
 <hostingHost>H0</hostingHost>
 <associatedNFFG>Nffg0</associatedNFFG> 
</nfvNode>
```
###### Example Response
*200* OK
```XML
<nfvNode xmlns="http://www.example.org/NfvDeployer">
 <name>MAILCLIENTt0Nffg0</name>
 <functionalType>MAILCLIENTt</functionalType>
 <hostingHost>H1</hostingHost>
 <associatedNFFG>Nffg0</associatedNFFG>
 <self href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
 <functionalTypeLink href="http://localhost:8080/NfvDeployer/rest/vnfs/MAILCLIENTt"/>
 <hostingHostLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
 <associatedNFFGLink href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0"/>
 <linksLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/links"/>
 <reachableHostsLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/reachableHosts"/>
</nfvNode>
```


#### Delete a Node (and undeploy it)
```HTTP
DELETE .../nodes/{nodeName}
```

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
204           | No Content      | 
403           | Forbidden       |
404           | Not Found       |

###### Example Request
```HTML
DELETE http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0
```

###### Example Response
*204 No Content*


#### Get all reachable Hosts for a specific Node
```HTTP
GET .../nodes/{nodeName}/reachableHosts
```
###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvHosts
404           | Not Found       |
500           | Server Error    |
502           | Bad Gateway     |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/reachableHosts
```
###### Example Response
*200* OK
```XML
<nfvHosts xmlns="http://www.example.org/NfvDeployer">
 <nfvHost>
  <name>H1</name>
  <maxVNFs>9</maxVNFs>
  <hostedNodes>WEBCLIENTt1Nffg0</hostedNodes>
  <hostedNodes>WEBSERVERt2Nffg0</hostedNodes>
  <installedMemory>8200</installedMemory>
  <installedStorage>71000</installedStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/hosts/H1"/>
  <hostedNodesLink href="http://localhost:8080/NfvDeployer/rest/hosts/H1/nodes"/>
 </nfvHost>
 <nfvHost>
  <name>H0</name>
  <maxVNFs>12</maxVNFs>
  <hostedNodes>MAILCLIENTt0Nffg0</hostedNodes>
  <hostedNodes>MAILSERVERt1Nffg0</hostedNodes>
  <installedMemory>6100</installedMemory>
  <installedStorage>28000</installedStorage>
  <self href="http://localhost:8080/NfvDeployer/rest/hosts/H0"/>
  <hostedNodesLink href="http://localhost:8080/NfvDeployer/rest/hosts/H0/nodes"/>
 </nfvHost>
</nfvHosts>
```

#### Get all Links starting from a specific Node
```HTTP
GET .../nodes/{nodeName}/links
```
###### Content Type
 - application/xml

###### Responses

Code          | Status          | Body
--------------|-----------------|------------
200           | OK              | nfvArcs
204           | No content      |
404           | Not Found       |
500           | Server Error    |

###### Example Request
```HTTP
GET http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0/links
```
###### Example Response
*200* Ok
```XML
<nfvArcs xmlns="http://www.example.org/NfvDeployer">
 <nfvArc>
  <name>Link0</name>
  <src>MAILCLIENTt0Nffg0</src>
  <dst>FWa0Nffg0</dst>
  <throughput>0.0</throughput>
  <latency>0</latency>
  <self href="http://localhost:8080/NfvDeployer/rest/nffgs/Nffg0/links/Link0"/>
  <srcLink href="http://localhost:8080/NfvDeployer/rest/nodes/MAILCLIENTt0Nffg0"/>
  <dstLink href="http://localhost:8080/NfvDeployer/rest/nodes/FWa0Nffg0"/>
 </nfvArc>
</nfvArcs>
```
