package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import javax.jms.JMSSessionMode;

import org.jboss.resteasy.annotations.providers.jaxb.IgnoreMediaTypes;
import org.json.JSONArray;
import org.json.JSONObject;

public class ACLMessage implements Serializable{


	private static final long serialVersionUID = 1L;
	private Performative performative;
	private AID sender;
	private AID[] receivers;
	private AID replyTo;
	private String content;
	private Object contentObject;
	private HashMap<String, Object> userArgs;
	private String language;
	private String encoding;
	private String ontology;
	private String protocol;
	private String conversationId;
	private String replyWith;
	private String inReplyTo;
	private Long replyBy;
	
	public ACLMessage(){
		
	}
	
	public ACLMessage(Performative performative) {
		this.performative = performative;
		userArgs = new HashMap<>();		
	}
	public Performative getPerformative() {
		return performative;
	}
	public void setPerformative(Performative performative) {
		this.performative = performative;
	}
	public AID getSender() {
		return sender;
	}
	public void setSender(AID sender) {
		this.sender = sender;
	}
	public AID[] getReceivers() {
		return receivers;
	}
	public void setReceivers(AID[] receivers) {
		this.receivers = receivers;
	}
	public AID getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(AID replyTo) {
		this.replyTo = replyTo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Object getContentObject() {
		return contentObject;
	}
	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
	}
	public HashMap<String, Object> getUserArgs() {
		return userArgs;
	}
	public void setUserArgs(HashMap<String, Object> userArgs) {
		this.userArgs = userArgs;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getOntology() {
		return ontology;
	}
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getReplyWith() {
		return replyWith;
	}
	public void setReplyWith(String replyWith) {
		this.replyWith = replyWith;
	}
	public String getInReplyTo() {
		return inReplyTo;
	}
	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
	}
	public Long getReplyBy() {
		return replyBy;
	}
	public void setReplyBy(Long replyBy) {
		this.replyBy = replyBy;
	}
	public void addReceiver(AID receiver){
		if(receivers == null)
			receivers = new AID[1];
		receivers[receivers.length-1] = receiver;
	}
	
	@Override
	public String toString() {
		return "ACLMessage [performative=" + performative + ", sender=" + sender + ", receivers="
				+ Arrays.toString(receivers) + ", replyTo=" + replyTo + ", content=" + content + ", contentObject="
				+ contentObject + ", userArgs=" + userArgs + ", language=" + language + ", encoding=" + encoding
				+ ", ontology=" + ontology + ", protocol=" + protocol + ", conversationId=" + conversationId
				+ ", replyWith=" + replyWith + ", inReplyTo=" + inReplyTo + ", replyBy=" + replyBy + "]";
	}
}
