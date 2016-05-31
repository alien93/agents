package model;

import java.io.Serializable;

public class Agent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AID id;
	private AgentCenter ac;

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Agent(AID id) {
		super();
		this.id = id;
	}
	
	public AID getId() {
		return id;
	}
	
	public void setId(AID id) {
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
