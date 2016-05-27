package session;

import java.util.ArrayList;

import javax.ejb.Remote;

import model.AID;
import model.Agent;
import model.AgentType;

@Remote
public interface AgentBeanRemote {

	/**
	 * Dobavi listu svih tipova agenata
	 * @return
	 */
	public ArrayList<AgentType> getAllAgentTypes();
	/**
	 * Dobavi sve pokrenute agente sa sistema
	 * @return
	 */
	public ArrayList<Agent> getAllRunningAgents();
	/**
	 * Pokreni agenta odredjenog tipa sa zadatim imenom
	 * @param agentType
	 * @param agentName
	 */
	public void runAgent(String agentType, String agentName);
	/**
	 * Zaustavi odredjenog agenta
	 * @param aid
	 */
	//public void stopRunningAgent(AID aid);
}
