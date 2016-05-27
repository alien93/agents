package session;

import javax.ejb.Remote;

@Remote
public interface AgentCenterBeanRemote {

	/**
	 * Nov ne-master cvor kontaktira master cvor koji ga registruje
	 */
	public void registerMe();
	
	/**
	 * Master cvor trazi spisak tipova agenata koje
	 * podrzava nov ne-master cvor
	 */
	public void getAllSupportedAgents();
		
	/**
	 * Master cvor dostavlja spisak novih tipova agenata (ukoliko ih ima)
	 * ostalim ne-master cvorovima
	 */
	public void forwardNewAgentTypes();

	/**
	 * Master čvor dostavlja spisak pokrenutih agenata novom ne-master čvoru koji
	 *	se nalaze kod njega ili nekog od preostalih ne-master čvorova
	 */
	public void forwardRunningAgents();
	
}
