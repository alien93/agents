package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.websocket.Session;

import model.Agent;
import model.AgentCenter;
import model.AgentType;
import session.AgentTypes;

@Singleton
public class Container{

	private static Container instance = null;
	private ArrayList<Agent> runningAgents = new ArrayList<Agent>();
	private HashMap<AgentCenter, ArrayList<Agent>> hosts = new HashMap<>();
	private AgentTypes agentTypes = new AgentTypes();
	private HashMap<AgentCenter, ArrayList<Session>> sessions = new HashMap<>();
	
	private Container(){

	}
	
	public static synchronized Container getInstance(){
		if(instance == null){
			System.out.println("Creating new instance");
			instance = new Container();
		}
		return instance;
	}
	
	public ArrayList<Agent> getRunningAgents(){
		return runningAgents;
	}
	
	public void addHost(AgentCenter ac){		
		if(hostExists(ac) == null){
			hosts.put(ac, new ArrayList<Agent>());
		}
		else{
			hosts.get(hostExists(ac)).addAll(new ArrayList<Agent>());
		}
	}
	
	public void addRunningAgent(AgentCenter ac, Agent agent){
		runningAgents.add(agent);
		
		if(hostExists(ac)==null){
			ArrayList<Agent> ra = new ArrayList<>();
			ra.add(agent);
			hosts.put(ac, ra);
		}
		else{
			//check if agent already exists
			ArrayList<Agent> ra = hosts.get(hostExists(ac));
			boolean agentExists = false;
			for(Agent a : ra){
				if(agentsEqual(a, agent)){
					agentExists = true;
				}
			}
			if(!agentExists)
				hosts.get(hostExists(ac)).add(agent);
		}
	}
	
	private AgentCenter hostExists(AgentCenter ac) {
		AgentCenter retVal = null;
		for(AgentCenter agentCenter : hosts.keySet()){
			if(agentCenter.getAddress().equals(ac.getAddress()) && agentCenter.getAlias().equals(ac.getAlias())){
				retVal = agentCenter;
				break;
			}
		}
		return retVal;
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

	public HashMap<AgentCenter, ArrayList<Session>> getSessions() {
		return sessions;
	}

	public void setSessions(HashMap<AgentCenter, ArrayList<Session>> sessions) {
		this.sessions = sessions;
	}
	
	public AgentCenter findAgentCenterByIP(String IP){
		AgentCenter retVal = null;
		for(AgentCenter ac : sessions.keySet()){
			if(ac.getAddress().equals(IP)){
				retVal = ac;
				break;
			}
		}
		return retVal;
	}
	
	public Session findSessionByID(String sessionID){
		Session retVal = null;
		for(Map.Entry<AgentCenter, ArrayList<Session>> session: sessions.entrySet()){
			for(Session s: session.getValue()){
				if(s.getId().equals(sessionID)){
					retVal = s;
					break;
				}
			}
		}
		return retVal;
	}
	
}
