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
import sun.management.resources.agent_zh_HK;

@Singleton
public class Container {

	private static Container instance = null;
	private ArrayList<Agent> runningAgents = new ArrayList<Agent>();
	private HashMap<AgentCenter, ArrayList<Agent>> hosts = new HashMap<>();
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
	
	public void addRunningAgent(AgentCenter ac, Agent agent){
		runningAgents.add(agent);
		if(hosts.get(ac)==null){
			ArrayList<Agent> ra = new ArrayList<>();
			ra.add(agent);
			hosts.put(ac, ra);
		}
		else{
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
	
	
}
