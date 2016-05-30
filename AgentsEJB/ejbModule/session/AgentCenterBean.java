package session;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import utils.Container;

/**
 * Session Bean implementation class AgentCenterBean
 */
@Stateless
@Path("ac")
@LocalBean
public class AgentCenterBean implements AgentCenterBeanRemote {

	@POST
	@Path("node")
	@Override
	public void registerMe() {
		if(!Container.amIMaster()){
			System.out.println("I am not a master node, I need to register");
		}
		else{
			System.out.println("I am a master node");
		}
	}

	@GET
	@Path("agents/classes")
	@Override
	public void getAllSupportedAgents() {
		// TODO Auto-generated method stub
		
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
