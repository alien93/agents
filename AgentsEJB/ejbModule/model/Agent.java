package model;

import java.io.Serializable;

public class Agent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private AgentCenter ac;

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Agent(String id, AgentCenter ac) {
		super();
		this.id = id;
		this.ac = ac;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void handleMessage(){}
	
	@Override
	public String toString(){
		return "Agent [id=" + id + "]";
	}

	public AgentCenter getAc() {
		return ac;
	}

	public void setAc(AgentCenter ac) {
		this.ac = ac;
	}
	
}
