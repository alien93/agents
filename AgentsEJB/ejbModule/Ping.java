import model.Agent;
import model.AgentCenter;

public class Ping extends Agent{

	public Ping(){
		super();
		System.out.println("default constructor");
	}
	
	public Ping(String id, AgentCenter ac){
		super(id, ac);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){}
	
}
