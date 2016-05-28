package model;

public class Agent {

	private String id;

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Agent(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void handleMessage(){}
	
}
