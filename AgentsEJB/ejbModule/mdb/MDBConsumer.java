package mdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.sound.midi.Synthesizer;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.json.JSONObject;

import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentCenter;
import session.MessageBeanRemote;
import session.RunningAgents;
import utils.Container;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(
		activationConfig = { 
			@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "queue/mojQueue")	
		})
public class MDBConsumer implements MessageListener {

    /**
     * Default constructor. 
     */
    public MDBConsumer() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	//vrsi lookup agenta kome je poruka namenjena i prosledjuje mu je
    	if(message instanceof ObjectMessage){
    		ObjectMessage objMsg = (ObjectMessage)message;
    		try {
				if(objMsg.getObject() instanceof ACLMessage){
					ACLMessage acl = (ACLMessage)objMsg.getObject();
					AID[] receivers = acl.getReceivers();
		    		//prodji kroz listu svih cvorova
					HashMap<AgentCenter, ArrayList<Agent>> hosts = Container.getInstance().getHosts();
					for(Map.Entry<AgentCenter, ArrayList<Agent>> entry: hosts.entrySet()){
					//	synchronized (entry.getValue()) {
						AgentCenter ac = entry.getKey();
			    		ArrayList<Agent> agents = entry.getValue();
						//prodji kroz listu svih agenata i pronadji pravog
			    		if(receivers!=null){
							for(Agent agent : agents){
								//prodji kroz listu receivera
								for(int i=0; i<receivers.length; i++){
									if(agent.getId().getName().equals(receivers[i].getName()) &&
											receivers[i].getHost().getAddress().equals(ac.getAddress()))
										if(receivers[i].getHost().getAddress().equals(Container.getLocalIP())){	//agent je na trenutnom cvoru
											agent.handleMessage(acl);
											break;
										}
										else{	//agent nije na trenutnom cvoru
											Client client = ClientBuilder.newClient();
											WebTarget resource = client.target("http://" + receivers[i].getHost().getAddress() + ":8080/AgentsWeb/rest/messages");
											Builder request = resource.request();
											Response response = request.post(Entity.json(acl));
											
											if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL){
												System.out.println("Sending was successfull");
											}
											else{
												System.out.println("Error: " + response.getStatus());
											}
										}
								}
						//	}
							}
			    		}
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
    	}
    }
}
