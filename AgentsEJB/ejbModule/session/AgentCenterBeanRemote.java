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
	
	
	/**
	 * Master čvor javlja ostalim ne-master čvorovima da obrišu čvor koji nije uspeo
	 *da izvrši handshake, kao i sve tipove agenata koji su potencijalno dostavljeni ostalim čvorovima. Ova
	 *operacije se takođe treba eksplicitno pokrenuti kada se neki čvor priprema za gašenje i želi da se odjavi iz
	 *klastera. Prilikom gašenja čvora treba pogasiti i sve agente koji trče na datom čvoru
	 * @param alias
	 */
	public void deleteNode(String alias);
	/**
	 * Ukoliko se desi da čvor ne odgovori zahtev se izvršava još jednom i ukoliko
	 *čvor ni tada ne odgovori smatra se da je ugašen i javlja se ostalim čvorovima da izbace zapis o ugašenom
	 *čvoru
	 */
	public void checkIfAlive();
}
