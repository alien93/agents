package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.SplittableRandom;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import jdk.nashorn.internal.parser.JSONParser;
import model.ACLMessage;
import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.Performative;
import session.AgentTypes;
import session.MessageBean;
import session.MessageBeanRemote;
import session.RunningAgents;
import utils.Container;

/**
 * WebSocket manager
 * @author nina
 *
 */

@ServerEndpoint(value="/websocket", encoders={RunningAgentsEncoder.class, AgentTypesEncoder.class})
public class WSManager {

		
	public WSManager(){
	}
	
	@OnOpen
	public void onOpen(Session session) {
		AgentCenter ac = Container.getInstance().findAgentCenterByIP(Container.getLocalIP());
		if(ac!=null){
			if(!Container.getInstance().getSessions().get(ac).contains(session))
				Container.getInstance().getSessions().get(ac).add(session);
		}
		else{
			AgentCenter acc = new AgentCenter("local_" + Container.getLocalIP(), Container.getLocalIP());
			ArrayList<Session> sessions = new ArrayList<>();
			sessions.add(session);
			Container.getInstance().getSessions().put(acc, sessions);
		}
	}
	
	@OnMessage
	public void onMessage(Session session, String message){
		try{
			if(session.isOpen()){
				switch(message){
				
				case("RUNNING_AGENTS"):
						RunningAgents ra = new RunningAgents();
						ra.setRunningAgents(Container.getInstance().getRunningAgents());
						session.getBasicRemote().sendObject(ra);
						break;
				case("AGENT_TYPES"):
						new AgentType();
						session.getBasicRemote().sendObject(Container.getInstance().getAgentTypes());	
						break;
				case("PERFORMATIVES"):
					ArrayList<String> retVal = new ArrayList<>();
					for(Performative performative : Performative.values()){
						retVal.add(performative.toString());
					}
					JSONArray json = new JSONArray(retVal);
					String jsonText = json.toString();
					String msg = "{\"performatives\":" + jsonText + "}";
					session.getBasicRemote().sendObject(msg);
					break;
				default:
					ObjectMapper mapper = new ObjectMapper();
					ACLMessage aclMessage = mapper.readValue(message, ACLMessage.class);
					sendMessage(aclMessage);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void sendMessage(ACLMessage aclMessage) {
		Context context;
		try {
			context = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory)context.lookup("java:/ConnectionFactory");
			final Queue target = (Queue) context.lookup("java:jboss/exported/jms/queue/mojQueue");
			context.close();
			Connection con = factory.createConnection();
			try{
				javax.jms.Session session = con.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(target);
				producer.send(session.createObjectMessage(aclMessage));
			}
			finally{
				con.close();
			}
			
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}		
	}
	
	private ACLMessage jsonToMsg(JSONObject jsonObj) {
		ACLMessage message = new ACLMessage();
		ObjectMapper mapper = new ObjectMapper();
		
		
		
		String performative = jsonObj.getString("performative");
		JSONObject sender = jsonObj.getJSONObject("sender");
		JSONArray receivers = jsonObj.getJSONArray("receivers");
		JSONObject replyTo = jsonObj.getJSONObject("replyTo");
		Long replyBy = (long) jsonObj.getInt("replyBy");
		//String userArgs = jsonObj.getString("userArgs");
		String content = jsonObj.getString("content");
		JSONObject contentObject = jsonObj.getJSONObject("contentObject");
		//sender
		String sender_name = sender.getString("name");
		JSONObject sender_host = sender.getJSONObject("host");
		JSONObject sender_type = sender.getJSONObject("type");
		String sender_host_address = sender_host.getString("address");
		String sender_host_alias = sender_host.getString("alias");
		String sender_type_module = sender_type.getString("module");
		String sender_type_name = sender_type.getString("name");
		//receivers
		JSONObject receiver = receivers.getJSONObject(0);
		String receiver_name = receiver.getString("name");
		JSONObject receiver_host = receiver.getJSONObject("host");
		JSONObject receiver_type = receiver.getJSONObject("type");
		String receiver_host_address = receiver_host.getString("address");
		String receiver_host_alias = receiver_host.getString("alias");
		String receiver_type_module = receiver_type.getString("module");
		String receiver_type_name = receiver_type.getString("name");
		//replyto
		String replyto_name = replyTo.getString("name");
		JSONObject replyto_host = replyTo.getJSONObject("host");
		JSONObject replyto_type = replyTo.getJSONObject("type");
		String replyto_host_address = replyto_host.getString("address");
		String replyto_host_alias = replyto_host.getString("alias");
		String replyto_type_module = replyto_type.getString("module");
		String replyto_type_name = replyto_type.getString("name");
		
		message.setPerformative(Performative.valueOf(performative));
		message.setSender(new AID(sender_name, new AgentCenter(sender_host_alias, sender_host_address), new AgentType(sender_type_name, sender_type_module)));
		message.addReceiver(new AID(receiver_name, new AgentCenter(receiver_host_alias, receiver_host_address), new AgentType(receiver_type_name, receiver_type_module)));
		message.setReplyTo(new AID(replyto_name, new AgentCenter(replyto_host_alias, replyto_host_address), new AgentType(replyto_type_name, replyto_type_module)));
		message.setReplyBy(replyBy);
		message.setContent(content);
		message.setContentObject(contentObject);
		return message;
	}
	
	

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Closing session: " + session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
	}
	
	
	
	
}
