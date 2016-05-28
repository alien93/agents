import model.Agent;

public class Ping extends Agent{

	public Ping(){
		super();
		System.out.println("default constructor");
	}
	
	public Ping(String id){
		super(id);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){}
	
}
