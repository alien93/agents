package session;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


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
		AID aid = new AID(agentName, host, at);
		String className = agentType.split("\\$")[1];
		try {
			Class<?> cla55 = Class.forName(className);
			Constructor<?> constructor = cla55.getConstructor(String.class);
			//TODO: Prosledi agent id
			Object object = constructor.newInstance(new Object[]{agentType + ":  " + agentName});
			Container.getInstance().addRunningAgent(ac, (Agent)object);
			//TODO: Svaki put kada se desi pokretanje agenta potrebno je izvršiti POST /agents/running zahtev na sve preostale
			//čvorove u mreži, kako bi svi imali informaciju o najnovijem agentu.
		
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/*@DELETE
	@Path("running/{aid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void stopRunningAgent(@PathParam("aid")AID aid) {
		// TODO Auto-generated method stub
		
	}

    */

}
