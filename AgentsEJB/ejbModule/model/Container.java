package model;

import java.util.ArrayList;

import javax.ejb.Singleton;

@Singleton
public class Container {

	private static Container instance = null;
	private ArrayList<Agent> runningAgents = new ArrayList<Agent>();
	
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
	
	public void addRunningAgent(Agent agent){
		runningAgents.add(agent);
	}
	
	public void removeRunningAgent(Agent agent){
		runningAgents.remove(agent);
	}
	
	
}
