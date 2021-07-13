package com.example.employeetrackerapp;

public class ChatMessage
{
    private String senderId;
    private String receiverId;
    private String  message;
    private String messagetime;
    private String messageStatus;
    private String messageId;
    private String senderName,senderProfile;
    private String receiverName,receiverProfile;


    public ChatMessage(){}

    public ChatMessage(String senderId, String receiverId, String message, String messagetime, String messageStatus,
                       String messageId, String senderName, String senderProfile, String receiverName, String receiverProfile) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messagetime = messagetime;
        this.messageStatus = messageStatus;
        this.messageId = messageId;
        this.senderName = senderName;
        this.senderProfile = senderProfile;
        this.receiverName = receiverName;
        this.receiverProfile = receiverProfile;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(String senderProfile) {
        this.senderProfile = senderProfile;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverProfile() {
        return receiverProfile;
    }

    public void setReceiverProfile(String receiverProfile) {
        this.receiverProfile = receiverProfile;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSeceiverId() {
        return receiverId;
    }

    public void setReceiverId(String seceiverId) {
        this.receiverId = seceiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
