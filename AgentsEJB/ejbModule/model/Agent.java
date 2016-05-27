package model;

public class Agent {

	private String id;
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Agent(String id) {
		super();
		this.id = id;
	}



	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}



	public void handleMessage(){}
	
}
