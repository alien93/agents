package model;

public class AID {

	private String name;
	private AgentCenter host;
	private AgentType type;
	public AID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AID(String name, AgentCenter host, AgentType type) {
		super();
		this.name = name;
		this.host = host;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AgentCenter getHost() {
		return host;
	}
	public void setHost(AgentCenter host) {
		this.host = host;
	}
	public AgentType getType() {
		return type;
	}
	public void setType(AgentType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AID [name=" + name + ", host=" + host + ", type=" + type + "]";
	}
}
