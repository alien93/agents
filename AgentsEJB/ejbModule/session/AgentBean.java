package session;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.json.JSONObject;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import model.AID;
import model.Agent;
import model.AgentCenter;
import model.AgentType;
import utils.Container;

/**
 * Session Bean implementation class AgentBean
 */
@Stateless
@LocalBean
@Path("agents")
public class AgentBean implements AgentBeanRemote {

	
	@GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
    	return "test";
    }
	
	@GET
	@Path("classes")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public AgentTypes getAllAgentTypes() {
		
		new AgentType();
		return Container.getInstance().getAgentTypes();
	}

	@GET
	@Path("running")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ArrayList<Agent> getAllRunningAgents() {
		return Container.getInstance().getRunningAgents();
	}

	@PUT
	@Path("running/{type}/{name}")
	@Override
	public void runAgent(@PathParam("type")String agentType, @PathParam("name")String agentName) {
		String host = AID.HOST_NAME;		
		AgentCenter ac = null;
		ac = new AgentCenter(host, Container.getLocalIP());
		AgentType at = new AgentType(agentName, "PingPong");
		AID aid = new AID(agentName, ac, at);
		String className = agentType.split("\\$")[1];
		try {
			Class<?> cla55 = Class.forName(className);
			Constructor<?> constructor = cla55.getConstructor(AID.class);
			Object object = constructor.newInstance(new Object[]{aid});
			Container.getInstance().addRunningAgent(ac, (Agent)object);
			//TODO: Svaki put kada se desi pokretanje agenta potrebno je izvršiti POST /agents/running zahtev na sve preostale
			//čvorove u mreži, kako bi svi imali informaciju o najnovijem agentu.
			
			for(AgentCenter agentCenter : Container.getInstance().getHosts().keySet()){
					Client client = ClientBuilder.newClient();
					WebTarget resource = client.target("http://" + agentCenter.getAddress() + ":8080/AgentsWeb/rest/ac/agents/running");
					Builder request = resource.request();
					RunningAgents ra = new RunningAgents();
					ra.setRunningAgents(Container.getInstance().getRunningAgents());
					Response response = request.post(Entity.json(ra));
					
					if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
						System.out.println("Forwarding new agent was successfull");
					}
					else{
						System.out.println("Error: " + response.getStatus());
					}
			}
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@DELETE
	@Path("running")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void stopRunningAgent(AID aid) {
		System.out.println("Aid: " + aid);
		//obj.getString("");
	}

}
