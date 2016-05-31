import model.AID;
import model.Agent;

public class Ping extends Agent{

	private static final long serialVersionUID = 1L;

	public Ping(){
		super();
		System.out.println("default constructor");
	}
	
	public Ping(AID id){
		super(id);
		System.out.println("parameters");

	}
	
	@Override
	public void handleMessage(){}
	
}
