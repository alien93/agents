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
		// TODO Auto-generated method stub
		
	}


	/**
	 * Master čvor dostavlja spisak ostalih ne-master čvorova novom ne-master čvoru
	 * @param ac - novi host
	 * @param hosts - ostali hostovi
	 */
	private void informNewHostHosts(AgentCenter ac, Set<AgentCenter> hosts) {
		// TODO Auto-generated method stub
		
	}
	
	


	/**
	 * Master čvor dostavlja spisak tipova agenata novom ne-master čvoru koje
	 * podržava on ili neki od ostalih ne-master čvorova
	 * @param ac - novi cvor
	 * @param agentTypes - tipovi agenata mastera i ostalih ne-master cvorova
	 */
	private void informNewHostAgentTypes(AgentCenter ac, AgentTypes agentTypes) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Obavestava ostale ne-master cvorove o tipovima agenata koje novi cvor podrzava
	 * @param ac - novododati cvor
	 * @param supportedAgents - podrzani tipovi agenata
	 */
	private void informNonMasterAgentTypes(AgentCenter ac, ArrayList<AgentType> supportedAgents) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Obavestava ostale ne-master cvorove da je novi cvor usao u mrezu
	 * @param ac - novi cvor
	 */
	private void informNonMasterNodes(AgentCenter ac) {
		// TODO Auto-generated method stub
		
	}


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
			System.out.println(response.getEntity());
			System.out.println((ArrayList<AgentType>)response.getEntity());
			agentTypes = (ArrayList<AgentType>)response.getEntity();
		}
		else{
			System.out.println("Error: " + response.getStatus());
		}
		return agentTypes;
	}
	
	@POST
	@Path("agents/classes")
	@Override
	public void forwardNewAgentTypes() {
		// TODO Auto-generated method stub
		
	}
	
	@POST
	@Path("agents/running")
	@Override
	public void forwardRunningAgents() {
		// TODO Auto-generated method stub
		
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
