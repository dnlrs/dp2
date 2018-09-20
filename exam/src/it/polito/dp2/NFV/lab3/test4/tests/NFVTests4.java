package it.polito.dp2.NFV.lab3.test4.tests;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NamedEntityReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.NfvClientFactory;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.test4.NfvClient3;
import it.polito.dp2.NFV.lab3.test4.NfvClient3Factory;
import it.polito.dp2.NFV.lab3.UnknownEntityException;


public class NFVTests4 {
	private static NfvReader referenceNfvReader;	// reference data generator
	private static VNFTypeReader referenceVNFTypeReader=null; // reference VNFTypeReader
	private static HostReader referenceHostReader=null; // reference HostReader

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Create reference data generator
		System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.Random.NfvReaderFactoryImpl");
		referenceNfvReader = NfvReaderFactory.newInstance().newNfvReader();

		// Check the data set is adequate for the test:
		// 1. initialize variables
		boolean found=false;
		TreeSet<VNFTypeReader> rvts = new TreeSet<VNFTypeReader>(new NamedEntityReaderComparator());

		// 2. Collect VNFTypes of Nodes in Nffg0 
		for(NodeReader n: referenceNfvReader.getNffg("Nffg0").getNodes()) {
			rvts.add(n.getFuncType());
		}

		// 3. For each collected VNFType
		for (VNFTypeReader vt:rvts) {
			// 3.1 if there is an adequate host we have found the reference VNFTypeReader
			if ((referenceHostReader = lookForHost(vt,"Nffg0"))!=null) {
				referenceVNFTypeReader = vt;
				break;
			}
		}
		// 4. Set found to false if we have not found the reference VNFTypeReader
		if (referenceVNFTypeReader!=null)
			found = true;
		assertEquals("Tests cannot run. Please choose another seed.",found,true);
		
	}

	/**
	 * Looks for an host on which a node with the given VNF type, can be allocated,
	 * under the assumption that only an NF-FG with the given name has been deployed.
	 * @param vt the VNF type
	 * @param nffgName the Nffg name
	 * @return a HostReader for the found host or null if no host has been found
	 */
	private static HostReader lookForHost(VNFTypeReader vt, String nffgName) {
		TreeSet<HostReader> hts = new TreeSet<HostReader>(new NamedEntityReaderComparator());
		// for all hosts
		hts.addAll(referenceNfvReader.getHosts());
		for (HostReader h:hts) {
			// compute non-allocated resources for the host:
			HashMap<String, Integer> map = getResource(h,nffgName);
			
			// if we have already found the host we stop the search
			if (	map.get("vnf")>0 &&
					map.get("memory") >= vt.getRequiredMemory() &&
					map.get("disk") >= vt.getRequiredStorage()
				) 
					return h;
		}
		return null;
	}

	/**
	 * Get the non-allocated resources (memory,disk,vnf) for a given Host, assuming a single
	 * NF-FG, with the given name, has been deployed. 
	 * In case the NF-FG name is null, the function will assume all NF-FGs have been deployed. 
	 * @param h the HostReader
	 * @param nffgName the Nffg name
	 * @return a HashMap<String, Integer> containing  the not allocated resource (memory,disk,vnf) for the HostReader h.
	 */
	private static HashMap<String, Integer> getResource(HostReader h, String nffgName){
		HashMap<String, Integer> result=new HashMap<String,Integer>();
		int freeMemory= h.getAvailableMemory();
		int	freeStorage = h.getAvailableStorage();
		int freeVNF=h.getMaxVNFs();
		VNFTypeReader type;
		for (NodeReader n:h.getNodes()) {
			if((nffgName==null) || (n.getNffg().getName().compareTo(nffgName)==0)) {
				type = n.getFuncType();
				freeMemory-=type.getRequiredMemory();
				freeStorage-=type.getRequiredStorage();
				freeVNF--;
			}
		}

		result.put("memory",freeMemory);
		result.put("disk",freeStorage);
		result.put("vnf", freeVNF);
		return result;
	}
	
	/**
	 * Counts how many nodes of the reference VNFType can be allocated on hosts with names different from
	 * the given host names, under the assumption that only the NF-FG with the given name has been deployed.
	 * @param nffgName the name of the NF-FG
	 * @param hostNames the set of host names
	 * @return the number of nodes that can be allocated
	 */
	private int countNodes(String nffgName, Set<String> hostNames) {
		int count=0;
		TreeSet<HostReader> hts = new TreeSet<HostReader>(new NamedEntityReaderComparator());
		hts.addAll(referenceNfvReader.getHosts());
		// for all hosts with names not included in hostnames
		for (HostReader h:hts)
			if (! hostNames.contains(h.getName())) {
				// compute non-allocated resources for this host, assuming only nffgName has been deployed:
				HashMap<String, Integer> map = getResource(h,nffgName);
				// store results in local variables
				int vnf = map.get("vnf");
				int memory = map.get("memory");
				int disk = map.get("disk");
				// increment count by as many extra nodes as we can allocate on this host
				for (; vnf>0 && memory >= referenceVNFTypeReader.getRequiredMemory() && 
								disk >= referenceVNFTypeReader.getRequiredStorage(); count++) {
					memory-=referenceVNFTypeReader.getRequiredMemory();
					disk-=referenceVNFTypeReader.getRequiredStorage();
					vnf--;
				}
			}
		return count;
	}

	@Before
	public void setUp() throws Exception {
	}

	// creates an instance of the NfvClient under test
	private NfvClient createTestNfvClient() throws NfvClientException, FactoryConfigurationError {
		// Create client1 under test
		NfvClient testNfvClient = NfvClientFactory.newInstance().newNfvClient();
		assertNotNull("The implementation under test generated a null NfvClient", testNfvClient);
		return testNfvClient;
	}

	// creates an instance of the NfvReader under test
	private NfvReader createTestNfvReader() throws NfvReaderException, FactoryConfigurationError {
		// Create client2 under test
		System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.sol3.client2.NfvReaderFactory");
		NfvReader testNfvReader = NfvReaderFactory.newInstance().newNfvReader();
		assertNotNull("The implementation under test generated a null NfvReader", testNfvReader);
		return testNfvReader;
	}

	// creates an instance of the NfvClient3 under test
	private NfvClient3 createTestNfvClient3() throws NfvClientException, FactoryConfigurationError {
		// Create client3 under test
		NfvClient3 testNfvClient3 = NfvClient3Factory.newInstance().newNfvClient3();
		assertNotNull("The implementation under test generated a null NfvClient3", testNfvClient3);
		return testNfvClient3;
	}


	/**
	 * Checks that the service and clients behave according to the specifications about unwanted hosts
	 * when allocating nodes
	 * @throws Exception
	 * @throws FactoryConfigurationError
	 */
	@Test
	public final void testNodeAddition() throws Exception, FactoryConfigurationError {
		System.out.println("INFO: starting testNodeAddition");

		System.out.println("[Debug]:"+referenceHostReader.getName());
		System.out.println("[Debug]:"+referenceVNFTypeReader.getName());

		
		NfvReader testNfvReader = createTestNfvReader();
		NfvClient3 testNfvClient3 = createTestNfvClient3();

		//Set correct URL
		String uriProp = System.getProperty("it.polito.dp2.NFV.lab3.URL");
		if(uriProp!=null) {
			testNfvClient3.setBaseServiceURL(new URL(uriProp));
		}else {
			testNfvClient3.setBaseServiceURL(new URL("http://localhost:8080/NfvDeployer/rest"));
		}

		// Create a set containing the reference host name
		Set<String> selectedHostNames = new HashSet<String>();
		selectedHostNames.add(referenceHostReader.getName());
		// and, possibly, add a second host name to the set
		for (HostReader h:referenceNfvReader.getHosts())
			if (! h.getName().equals(referenceHostReader.getName())) {
				selectedHostNames.add(h.getName());
				break;
			}

		// get the expected number of nodes that can be added having selectedHostNames as unwanted hosts
		int expectedNodeNumber = countNodes("Nffg0", selectedHostNames);
		assertTrue("Internal test error (wrong expected node number): please inform the teacher",expectedNodeNumber>=0);
		
		// set selected host names as unwanted hosts in client3
		testNfvClient3.setUnwantedHosts(selectedHostNames);
		
		// get DeployedNffg "Nffg0" from client3
		DeployedNffg nffg = testNfvClient3.getDeployedNffg("Nffg0");
		
		// add expectedNodeNumber nodes to deployed Nffg with reference VNFTypeReader and no host name;
		// these operations are expected to succeed
		for (int i=0; i<expectedNodeNumber; i++) {
			NodeReader newNode = nffg.addNode(referenceVNFTypeReader,null);
			assertNotNull("Null NodeReader returned by addNode",newNode);
			
			String newNodeName = newNode.getName();
			//assertNotNull("Null node name returned by added node",newNodeName);
			
			HostReader host = newNode.getHost();
			assertNotNull("Null HostReader returned by added node",host);
			
			String hostName = host.getName();
			assertNotNull("Null node name returned by addNode",newNodeName);
			
			assertFalse("Node was allocated on an unwanted host",selectedHostNames.contains(hostName));
		}
		// try one more addition, which should fail
		try {
			nffg.addNode(referenceVNFTypeReader,null);
			fail("Expected exception was not thrown when adding extra node");
		} catch (Exception e) {
			// now, remove the reference host's name from the set of unwanted host names
			selectedHostNames.remove(referenceHostReader.getName());
			testNfvClient3.setUnwantedHosts(selectedHostNames);
			// and check that now addition succeeds
			NodeReader newNode = nffg.addNode(referenceVNFTypeReader,null);
			assertNotNull("Null NodeReader returned by addNode",newNode);
			String newNodeName = newNode.getName();
			assertNotNull("Null node name returned by addNode",newNodeName);
			HostReader host = newNode.getHost();
			assertNotNull("Null HostReader returned by added node",host);
			String hostName = host.getName();
			assertNotNull("Null node name returned by addNode",newNodeName);
			// and that addition was made on the right host
			assertEquals("Allocation was not made on the expected host",hostName,referenceHostReader.getName());
		}

		System.out.println("INFO: testNodeAddition completed");

	}

	/**
	 * Tests that client3 fails as expected when data are requested with wrong URL
	 * @throws Exception
	 * @throws FactoryConfigurationError
	 */
	@Test(expected=ServiceException.class)
	public final void testWrongURL() throws Exception, FactoryConfigurationError {
		System.out.println("INFO: starting testWrongURL");
		NfvClient3 testNfvClient3 = createTestNfvClient3();
		testNfvClient3.setBaseServiceURL(new URL("http://localhost/wrong"));
		DeployedNffg nffg = testNfvClient3.getDeployedNffg("Nffg0");
		nffg.addNode(referenceVNFTypeReader,null);
		System.out.println("INFO: testWrongURL completed");
	}
	
	
	/**
	 * Tests that client3 fails as expected when unknown host names are passed
	 * @throws Exception
	 * @throws FactoryConfigurationError
	 */
	@Test(expected=UnknownEntityException.class)
	public final void testWrongHostName() throws Exception, FactoryConfigurationError {
		System.out.println("INFO: starting testWrongHostName");
		NfvClient3 testNfvClient3 = createTestNfvClient3();

		//Set correct URL
		String uriProp = System.getProperty("it.polito.dp2.NFV.lab3.URL");
		if(uriProp!=null) {
			testNfvClient3.setBaseServiceURL(new URL(uriProp));
		}else {
			testNfvClient3.setBaseServiceURL(new URL("http://localhost:8080/NfvDeployer/rest"));
		}
		
		// Create a set containing an unknown host name
		Set<String> selectedHostNames = new HashSet<String>();
		selectedHostNames.add("unknownName");

		// Set this set as the set of unwanted hosts in client3
		testNfvClient3.setUnwantedHosts(selectedHostNames);
	
		System.out.println("INFO: testWrongHostName completed");
		
	}	

}

class NamedEntityReaderComparator implements Comparator<NamedEntityReader> {
	public int compare(NamedEntityReader f0, NamedEntityReader f1) {
		return f0.getName().compareTo(f1.getName());
	}
}
