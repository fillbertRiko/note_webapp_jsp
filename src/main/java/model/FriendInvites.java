package model;

import java.sql.Date;
import java.util.List;

public class FriendInvites {
	private String id;
	private String sender_id;
	private String receiver_id;
	private List<String> status;
	private Date sent_at;
	
	public FriendInvites() {};
	
	public FriendInvites(String id, String sender_id, String receiver_id, List<String> status, Date sent_at) {
		super();
		this.id = id;
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.status = status;
		this.sent_at = sent_at;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSenderId() {
		return sender_id;
	}
	
	public void setSenderId(String sender_id) {
		this.sender_id = sender_id;
	}
	
	public String getReceiverId() {
		return receiver_id;
	}
	
	public void setReceiverId(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	
	public List<String> getStatus() {
		return status;
	}
	
	public void setStatus(List<String> status) {
		this.status = status;
	}
	
	public Date getTimeSent() {
		return sent_at;
	}
	
	public void setTimeSent(Date sent_at) {
		this.sent_at = sent_at;
	}
}
