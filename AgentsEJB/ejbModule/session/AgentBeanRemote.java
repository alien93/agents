package session;

import java.util.ArrayList;

import javax.ejb.Remote;
import javax.ws.rs.PathParam;

import model.AID;
import model.Agent;
import model.AgentType;

@Remote
public interface AgentBeanRemote {

	/**
	 * Dobavi listu svih tipova agenata
	 * @return
	 */
	public AgentTypes getAllAgentTypes();
	/**
	 * Dobavi sve pokrenute agente sa sistema
	 * @return
	 */
	public ArrayList<Agent> getAllRunningAgents();
	/**
	 * Pokreni agenta odredjenog tipa sa zadatim imenom
	 * @param agentType
	 * @param agentName
	 * @return 
	 */
	public Agent runAgent(String agentType, String agentName);
	/**
	 * Zaustavi odredjenog agenta
	 * @param aid
	 */
	public void stopRunningAgent(AID aid);
	/**
	 * Proveri da li je node ziv
	 * @return 
	 */
	public void checkNode();
	/**
	 * Ukloni node ukoliko dodje do greske
	 */
	public void deleteNode(String ip);

}
