package session;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.websocket.PongMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.picketbox.exceptions.PicketBoxProcessingException;

import com.sun.org.apache.bcel.internal.util.ClassPath;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import model.AID;
import model.Agent;
import model.AgentCenter;
import model.AgentType;
import sun.reflect.ConstructorAccessor;

/**
 * Session Bean implementation class AgentBean
 */
@Stateless
@LocalBean
@Path("agents")
public class AgentBean implements AgentBeanRemote {

	ArrayList<Agent> runningAgents = new ArrayList<Agent>();
	
	@GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
    	return "test";
    }
	
	@GET
	@Path("classes")
	@Override
	public ArrayList<AgentType> getAllAgentTypes() {
		AgentType at = new AgentType();
		return at.getAgentTypes();
	}

	@GET
	@Path("running")
	@Override
	public ArrayList<Agent> getAllRunningAgents() {
		// TODO Auto-generated method stub
		return null;
	}

	@PUT
	@Path("running/{type}/{name}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Override
	public void runAgent(@PathParam("type")String agentType, @PathParam("name")String agentName) {
		String host = AID.HOST_NAME;		
		System.out.println(agentType);
		AgentType at = new AgentType(agentName, "PingPong");
		AID aid = new AID(agentName, host, at);
		String className = agentType.split("\\$")[1];
		try {
			Class<?> cla55 = Class.forName(className);
			System.out.println("Class name " + cla55.getName());
			Constructor<?> constructor = cla55.getConstructor(String.class);
			Object object = constructor.newInstance(new Object[]{"test"});
			runningAgents.add((Agent)object);
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
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
