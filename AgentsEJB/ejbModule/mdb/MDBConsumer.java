package mdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jboss.security.AltClientLoginModule;

import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentCenter;
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
						AgentCenter ac = entry.getKey();
			    		ArrayList<Agent> agents = entry.getValue();
						//prodji kroz listu svih agenata i pronadji pravog
						for(Agent agent : agents){
							//prodji kroz listu receivera
							for(int i=0; i<receivers.length; i++){
								System.out.println(agent.getId());
								System.out.println(receivers[i].getName());
								System.out.println(receivers[i].getHost());
								if(agent.getId().equals(receivers[i].getName()) &&
										receivers[i].getHost().equals(ac)){
									agent.handleMessage(acl);
								}
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
