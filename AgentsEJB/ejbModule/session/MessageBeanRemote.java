package session;

import java.util.ArrayList;

import javax.ejb.Remote;

import model.ACLMessage;

@Remote
public interface MessageBeanRemote {

	/**
	 * Posalji ACL poruku
	 */
	public void sendMessage(ACLMessage message);
	
	/**
	 * Dobavi listu performativa
	 * @return
	 */
	public ArrayList<String> getPerformatives();
	
	/**
	 * Dobavi listu poruka za konzolu
	 */
	public ArrayList<String> getLoggerMessages();
	
	/**
	 * Obrisi poruke
	 */
	public void deleteLoggerMessages();
}
