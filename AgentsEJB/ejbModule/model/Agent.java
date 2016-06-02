package model;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import session.AgentBean;
import session.AgentBeanRemote;
import session.MessageBean;
import session.MessageBeanRemote;

public class Agent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AID id;

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Agent(AID id) {
		super();
		this.id = id;
	}
	
	public AID getId() {
		return id;
	}
	
	public void setId(AID id) {
		this.id = id;
	}
	
	
	public void handleMessage(ACLMessage message){	}
	
	public MessageBeanRemote findMB(){
		MessageBeanRemote mbr = new MessageBean();
		
		try {
			Context context = new InitialContext();
			String remoteName = "global/Agents/AgentsWeb/MessageBean!session.MessageBeanRemote";
			mbr = (MessageBeanRemote)context.lookup(remoteName);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return mbr;
	}
	
	public AgentBeanRemote findAB(){
		AgentBeanRemote ab = new AgentBean();
		
		try {
			Context context = new InitialContext();
			String remoteName = "global/Agents/AgentsWeb/AgentBean!session.AgentBeanRemote";
			ab = (AgentBeanRemote)context.lookup(remoteName);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ab;
	}
	
	@Override
	public String toString(){
		return "Agent [id=" + id + "]";
	}

	
	
}
