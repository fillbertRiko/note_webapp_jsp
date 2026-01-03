package model;

import java.util.Date;

public class FriendInvites {
    private String id;
    private String senderId;
    private String receiverId;
    private String status;
    private Date createdAt;
    private User userDetails; 

    public FriendInvites() {
    }

    public FriendInvites(String id, String senderId, String receiverId, String status, Date createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public FriendInvites(String senderId, String receiverId, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = new Date();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getTimeSent() { return createdAt; }
    public void setTimeSent(Date date) { this.createdAt = date; }
    public User getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }
}