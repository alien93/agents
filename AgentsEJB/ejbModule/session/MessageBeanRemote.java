package session;

import java.util.ArrayList;

import javax.ejb.Remote;

@Remote
public interface MessageBeanRemote {

	/**
	 * Posalji ACL poruku
	 */
	public void sendMessage();
	
	/**
	 * Dobavi listu performativa
	 * @return
	 */
	public ArrayList<String> getPerformatives();
}
