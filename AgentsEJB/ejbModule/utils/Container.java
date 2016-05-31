package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.ejb.Singleton;
import javax.mail.internet.InternetAddress;

import model.Agent;
import model.AgentCenter;
import model.AgentType;
import session.AgentTypes;
import sun.management.resources.agent_zh_HK;

@Singleton
public class Container {

	private static Container instance = null;
	private ArrayList<Agent> runningAgents = new ArrayList<Agent>();
	private HashMap<AgentCenter, ArrayList<Agent>> hosts = new HashMap<>();
	private AgentTypes agentTypes = new AgentTypes();
	
	private Container(){

	}
	
	public static Container getInstance(){
		if(instance == null)
			instance = new Container();
		return instance;
	}
	
	public ArrayList<Agent> getRunningAgents(){
		return runningAgents;
	}
	
	public void addHost(AgentCenter ac){
		hosts.put(ac, new ArrayList<Agent>());
	}
	
	public void addRunningAgent(AgentCenter ac, Agent agent){
		runningAgents.add(agent);
		if(hosts.get(ac)==null){
			ArrayList<Agent> ra = new ArrayList<>();
			ra.add(agent);
			hosts.put(ac, ra);
		}
		else{
			//check if agent already exists
			ArrayList<Agent> ra = hosts.get(ac);
			boolean agentExists = false;
			for(Agent a : ra){
				if(agentsEqual(a, agent)){
					agentExists = true;
				}
			}
			if(!agentExists)
				hosts.get(ac).add(agent);
		}
	}
	
	public void removeRunningAgent(AgentCenter ac, Agent agent){
		runningAgents.remove(agent);
		hosts.get(ac).remove(agent);
	}
	
	public HashMap<AgentCenter, ArrayList<Agent>> getHosts(){
		return hosts;
	}
	
	public static String getLocalIP(){
		PropertiesReader pr = new PropertiesReader();
		return pr.getLocal();
	}
	
	public static String getMasterIP(){
		PropertiesReader pr = new PropertiesReader();
		return pr.getMaster();
	}
	
	public static boolean amIMaster(){
		return getLocalIP().equals(getMasterIP())?true:false;
	}

	public AgentTypes getAgentTypes() {
		return agentTypes;
	}

	public void setAgentTypes(AgentTypes agentTypes) {
		this.agentTypes = agentTypes;
	}
	
	public void addAgentType(AgentType at){
		if(!agentTypeExists(at))
			this.agentTypes.getAgentTypes().add(at);
	}

	private boolean agentTypeExists(AgentType at) {
		boolean retVal = false;
		ArrayList<AgentType> agentTypes = this.agentTypes.getAgentTypes();
		for(AgentType att : agentTypes){
			if(att.getModule().equals(at.getModule()) &&
					att.getName().equals(at.getName())){
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
	private boolean agentsEqual(Agent myA, Agent newA) {
		return myA.getId().getName().equals(newA.getId().getName()) &&
			   myA.getId().getHost().getAddress().equals(newA.getId().getHost().getAddress()) &&
			   myA.getId().getHost().getAlias().equals(newA.getId().getHost().getAlias()) &&
			   myA.getId().getType().getName().equals(newA.getId().getType().getName()) &&
			   myA.getId().getType().getModule().equals(newA.getId().getType().getModule());
	}
	
	
}
