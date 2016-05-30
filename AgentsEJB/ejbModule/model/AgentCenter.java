package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentCenter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String alias;
	private String address;
	
	public AgentCenter(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}
	public AgentCenter() {
		super();
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "AgentCenter [alias=" + alias + ", address=" + address + "]";
	}
}
