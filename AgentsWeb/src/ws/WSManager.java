package ws;

import java.io.IOException;
import java.util.ArrayList;

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

import org.json.JSONObject;

import model.AgentCenter;
import session.MessageBean;
import session.MessageBeanRemote;
import session.RunningAgents;
import utils.Container;

/**
 * WebSocket manager
 * @author nina
 *
 */

@ServerEndpoint(value="/websocket", encoders={RunningAgentsEncoder.class})
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
		System.out.println("Message from " + session.getId() + ":" + message);
		try{
			if(session.isOpen()){
				switch(message){
				
				case("RUNNING_AGENTS"):
						String text = "{\"type\":\"" + message +  "\", \"sessionID\":\"" + session.getId()+ "\"}";
						//sender.sendMessage(text);
						RunningAgents ra = new RunningAgents();
						ra.setRunningAgents(Container.getInstance().getRunningAgents());
						session.getBasicRemote().sendObject(ra);
				}
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Closing session: " + session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
	}
	
	
	
	
}
