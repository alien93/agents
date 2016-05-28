import model.Agent;

public class Pong extends Agent{

	public Pong(){
		super();
		System.out.println("default constructor");
	}
	
	public Pong(String id){
		super(id);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){
		
	}
	
}