import model.Agent;
import model.AgentCenter;

public class Pong extends Agent{

	public Pong(){
		super();
		System.out.println("default constructor");
	}
	
	public Pong(String id, AgentCenter ac){
		super(id, ac);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){
		
	}
	
}