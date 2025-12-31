package model;

import java.util.Date;

public class FriendInvites {
	private String id;
	private String senderId;
	private String receiverId;
	private String status;
	private Date sentAt;

	public FriendInvites() {
	}

	public FriendInvites(String id, String senderId, String receiverId, String status, Date sentAt) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.status = status;
		this.sentAt = sentAt;
	}

	public FriendInvites(String senderId, String receiverId, String status) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.status = status;
		this.sentAt = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimeSent() {
		return sentAt;
	}

	public void setTimeSent(Date sentAt) {
		this.sentAt = sentAt;
	}
}
