package session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import model.Agent;
import model.AgentCenter;
import model.AgentType;

import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import utils.Container;

/**
 * Session Bean implementation class AgentCenterBean
 */
@Stateless
@Path("ac")
@LocalBean
public class AgentCenterBean implements AgentCenterBeanRemote {

	
	@GET
	@Path("amIMaster")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public boolean checkIfMasterNode() {
		return Container.amIMaster();
	};
	
	@POST
	@Path("nodes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void registerHosts(AgentHosts hosts){
		for(AgentCenter host : hosts.getHosts()){
			if(!hostExists(host)){
				Container.getInstance().addHost(host);
			}
		}
	}
	
	@POST
	@Path("node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void registerMe(AgentCenter ac) {
		System.out.println(ac.toString());
		if(hostExists(ac))
			System.out.println("Host already exists");
		else{
			System.out.println("Adding new host...");
			Container.getInstance().addHost(ac);
			ArrayList<AgentType> supportedAgents = getAllSupportedAgents(ac.getAddress());
			
			informNonMasterNodes(ac);
			informNonMasterAgentTypes(ac, supportedAgents);
			informNewHostHosts(ac, Container.getInstance().getHosts().keySet());
			informNewHostAgentTypes(ac, Container.getInstance().getAgentTypes());
			informNewHostRunningAgents(ac, Container.getInstance().getRunningAgents());
		}
	}
	
	/**
	 * Master čvor dostavlja spisak pokrenutih agenata novom ne-master čvoru koji
	 * se nalaze kod njega ili nekog od preostalih ne-master čvorova
	 * @param ac - novi cvor
	 * @param runningAgents - agenti koji rade
	 */
	private void informNewHostRunningAgents(AgentCenter ac, ArrayList<Agent> runningAgents) {
		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target("http://" + ac.getAddress() + ":8080/AgentsWeb/rest/ac/agents/running");
		Builder request = resource.request();
		RunningAgents ra = new RunningAgents();
		ra.setRunningAgents(runningAgents);
		Response response = request.post(Entity.json(ra));
		
		if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
			System.out.println("Informing non master nodes about new agent types was successfull");
		}
		else{
			System.out.println("Error: " + response.getStatus());
		}
	}


	/**
	 * Master čvor dostavlja spisak ostalih ne-master čvorova novom ne-master čvoru
	 * @param ac - novi host
	 * @param hosts - ostali hostovi
	 */
	private void informNewHostHosts(AgentCenter ac, Set<AgentCenter> hosts) {
		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target("http://" + ac.getAddress() + ":8080/AgentsWeb/rest/ac/nodes");
		Builder request = resource.request();
		AgentHosts ah = new AgentHosts();
		ah.setHosts(hosts);
		Response response = request.post(Entity.json(ah));
		
		if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
			System.out.println("Informing non master nodes about new agent types was successfull");
		}
		else{
			System.out.println("Error: " + response.getStatus());
		}
		
	}
	
	


	/**
	 * Master čvor dostavlja spisak tipova agenata novom ne-master čvoru koje
	 * podržava on ili neki od ostalih ne-master čvorova
	 * @param ac - novi cvor
	 * @param agentTypes - tipovi agenata mastera i ostalih ne-master cvorova
	 */
	private void informNewHostAgentTypes(AgentCenter ac, AgentTypes agentTypes) {
		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target("http://" + ac.getAddress() + ":8080/AgentsWeb/rest/ac/agents/classes");
		Builder request = resource.request();
		Response response = request.post(Entity.json(agentTypes));
		
		if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
			System.out.println("Informing new node about new agent types was successfull");
		}
		else{
			System.out.println("Error: " + response.getStatus());
		}
	}


	/**
	 * Obavestava ostale ne-master cvorove o tipovima agenata koje novi cvor podrzava
	 * @param ac - novododati cvor
	 * @param supportedAgents - podrzani tipovi agenata
	 */
	private void informNonMasterAgentTypes(AgentCenter ac, ArrayList<AgentType> supportedAgents) {
		Set<AgentCenter> hosts = Container.getInstance().getHosts().keySet();
		String masterIP = Container.getMasterIP();
		String newHostIP = ac.getAddress();
		for(AgentCenter host : hosts){
			if(!host.getAddress().equals(masterIP) &&		//ukoliko nije master
					!host.getAddress().equals(newHostIP)){	//ukoliko nije novi cvor
				//obavesti ostale o novom tipovima agenata
				Client client = ClientBuilder.newClient();
				WebTarget resource = client.target("http://" + host.getAddress() + ":8080/AgentsWeb/rest/ac/agents/classes");
				Builder request = resource.request();
				AgentTypes at = new AgentTypes();
				at.setAgentTypes(supportedAgents);
				Response response = request.post(Entity.json(at));
				
				if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
					System.out.println("Informing non master nodes about new agent types was successfull");
				}
				else{
					System.out.println("Error: " + response.getStatus());
				}
			}
		}		
	}


	/**
	 * Obavestava ostale ne-master cvorove da je novi cvor usao u mrezu
	 * @param ac - novi cvor
	 */
	private void informNonMasterNodes(AgentCenter ac) {
		Set<AgentCenter> hosts = Container.getInstance().getHosts().keySet();
		String masterIP = Container.getMasterIP();
		String newHostIP = ac.getAddress();
		for(AgentCenter host : hosts){
			if(!host.getAddress().equals(masterIP) &&		//ukoliko nije master
					!host.getAddress().equals(newHostIP)){	//ukoliko nije novi cvor
				//obavesti ostale o novom cvoru
				Client client = ClientBuilder.newClient();
				WebTarget resource = client.target("http://" + host.getAddress() + ":8080/AgentsWeb/rest/ac/node");
				Builder request = resource.request();
				Response response = request.post(Entity.json(ac));
				
				if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
					System.out.println("Informing non master nodes was successfull");
				}
				else{
					System.out.println("Error: " + response.getStatus());
				}
			}
		}
	}

	/**
	 * Proverava da li cvor postoji u listi cvorova
	 * @param ac - novi cvor
	 * @return
	 */
	private boolean hostExists(AgentCenter ac) {
		boolean retVal = false;
		HashMap<AgentCenter, ArrayList<Agent>> hosts = Container.getInstance().getHosts();
		
		for(AgentCenter ac_key : hosts.keySet()){
			if(ac_key.getAddress().equals(ac.getAddress())){
				retVal = true;
				break;
			}
		}
		
		return retVal;
	}

	/**
	 * Master cvor trazi spisak tipova agenata koje
	 * podrzava nov ne-master cvor
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<AgentType> getAllSupportedAgents(String ip) {
		ArrayList<AgentType> agentTypes = new ArrayList<AgentType>();
		
		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target("http://" + ip + ":8080/AgentsWeb/rest/agents/classes");
		Builder request = resource.request();
		Response response = request.get();
		
		if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
			AgentTypes at = response.readEntity(AgentTypes.class);
			System.out.println(at.getAgentTypes().toString());
		}
		else{
			System.out.println("Error: " + response.getStatus());
		}
		return agentTypes;
	}
	
	@POST
	@Path("agents/classes")
	@Override
	public void forwardNewAgentTypes(AgentTypes at) {
		System.out.println("I have received new agent types");
		System.out.println("AT: " + at);
		ArrayList<AgentType> myAgentTypes = Container.getInstance().getAgentTypes().getAgentTypes();
		ArrayList<AgentType> newAgentTypes = at.getAgentTypes();
		boolean typeExists = false;
		for(AgentType newAt: newAgentTypes){
			for(AgentType myAt : myAgentTypes){
				if(newAt.getModule().equals(myAt.getModule()) &&
						newAt.getName().equals(myAt.getName())){
					//vec postoji
					typeExists = true;
				}
			}
			if(!typeExists){
				Container.getInstance().addAgentType(newAt);
			}
		}
		System.out.println("My list of agent types looks like this: " + Container.getInstance().getAgentTypes());
	}
	
	@POST
	@Path("agents/running")
	@Override
	public void forwardRunningAgents(RunningAgents ra) {
		
	}

	@Override
	public void deleteNode(String alias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkIfAlive() {
		// TODO Auto-generated method stub
		
	}

}
