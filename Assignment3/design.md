
## Service design

The service is composed by 4 parts, or modules, that interact between each other. 

The *neo4jAPI* offers all the functionalities needed in order to access the Neo4J Service. 

The *nfvSystem* is the central part of the service and it keeps an updated database with all the objects currently existing in the NFV system and also their correspondence with the nodes and relations deployed on the Neo4J Web Service. this package implements all the interfaces for accessing data in the NFV System, and it provides the necessary layer to add Nodes, NFFGs or Links and also to remove them from the system. Then any deployable object is added to the system it is also deployed into the Neo4J service and when it's removed it is also undeployed from the Neo4J Service.

The database keeps objects ordered according to their name, this is useful when paginating, we have O(log(n)) time when adding a new object but ordering is not necessary when returning a specific page.

The *reachability* package provides access to the advanced functions of the Neo4J Service and implements the necessary interfaces for getting the extended information about nodes.

The *resources* package manages requests from clients and forwards them to the nfvSystem. Since the number of objects in the system may be very large, it provides, when deemed useful, and according to the REST API documentation, options to paginate responses or to return less-detailed objects.

The Service implements something similar to a HATEOAS functionality, yet it is not fully complete and the client may have to add optional queries to URIs by himself.

## Deploying an NFFG

The service offers the possibility to deploy a complete NFFG, with nodes and links, in a single request. When an NFFG is deployed all it Nodes and Links are added to the system, corresponding and necessary GraphNodes and Relationships are created into the Neo4J Service and if adding the complete NFFG fails, then loaded data is removed from the system and undeployed from the Neo4J Service.

An NFFG may also be deployed without nodes or link, or only with nodes.

When adding nodes (within an NFFG or independently) the client may suggest the hosting host. If this one is has not enough memory or storage available or no VNFs slots another one may be chosen randomly. 

## The position of Links

Since the Links have meaning only within an HFFG, there is not a global collection resource with Links, but they may be accessed from NFFGs or from Nodes always relatively to the "current" NFFG or Node.