import model.AID;
import model.Agent;

public class Pong extends Agent{

	private static final long serialVersionUID = 1L;

	public Pong(){
		super();
		System.out.println("default constructor");
	}
	
	public Pong(AID id){
		super(id);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){
		
	}
	
}