package model;

import java.io.Serializable;
import java.util.ArrayList;

import session.AgentTypes;

public class AgentType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String module;
	
	private AgentTypes agentTypes;
	
	public AgentType() {
		super();
		agentTypes.getAgentTypes().add(new AgentType("Ping", "PingPong"));
		agentTypes.getAgentTypes().add(new AgentType("Pong", "PingPong"));
	}
	public AgentType(String name, String module) {
		super();
		this.name = name;
		this.module = module;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}

	public AgentTypes getAgentTypes() {
		return agentTypes;
	}
	public void setAgentTypes(AgentTypes agentTypes) {
		this.agentTypes = agentTypes;
	}
	@Override
	public String toString() {
		return "AgentType [name=" + name + ", module=" + module + "]";
	}
}
