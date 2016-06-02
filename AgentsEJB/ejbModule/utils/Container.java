package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.websocket.Session;

import model.AID;
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
	private ArrayList<String> loggerMessages = new ArrayList<String>();
	
	private Container(){

	}
	
	public static synchronized Container getInstance(){
		if(instance == null){
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
		//adding to arraylist of runningagents
		//check if agent already exists
		boolean ae = false;
		for(Agent a : runningAgents){
			if(agentsEqual(a, agent)){
				ae = true;
				break;
			}
		}
		if(!ae){
			runningAgents.add(agent);
		}
		
		if(hostExists(ac)==null){
			ArrayList<Agent> ra = new ArrayList<>();
			ra.add(agent);
			hosts.put(ac, ra);
		}
		else{
			//adding to hashmap of hosts and runningagents
			//check if agent already exists
			ArrayList<Agent> ra = hosts.get(hostExists(ac));
			boolean agentExists = false;
			for(Agent a : ra){
				if(agentsEqual(a, agent)){
					agentExists = true;
					break;
				}
			}
			if(!agentExists){
				hosts.get(hostExists(ac)).add(agent);
			}
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

	public ArrayList<String> getLoggerMessages() {
		return loggerMessages;
	}

	public void setLoggerMessages(ArrayList<String> loggerMessages) {
		this.loggerMessages = loggerMessages;
	}
	
	public void log(String message){
		this.loggerMessages.add(message);
	}
	
	public void removeHostAndRunningAgents(String ip){
		AgentCenter toBeRemoved = new AgentCenter();
		for(AgentCenter ac: hosts.keySet()){
			if(ac.getAddress().equals(ip)){
				toBeRemoved = ac;
				break;
			}
		}
		
		if(toBeRemoved!=null){
			ArrayList<Agent> raOnHost = hosts.get(toBeRemoved);
			hosts.remove(toBeRemoved);
			ArrayList<Agent> ra = runningAgents;
			for(Agent a : raOnHost){
				for(Agent b : ra){
					if(agentsEqual(a, b)){
						runningAgents.remove(a);
					}
				}
			}
		}
	}

	public void removeRunningAgent(Object ac, AID aid) {
		// TODO
	}
	
}
