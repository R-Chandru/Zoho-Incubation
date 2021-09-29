package com.chandru.zoho.model;

public class Message {
    String senderNumber;
    String receiverNumber;
    String message;
    String messageTime;
    String messageStatus;
    String messageReadTime;

    public Message(String senderNumber, String receiverNumber, String message, String messageTime, String messageStatus, String messageReadTime) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.message = message;
        this.messageTime = messageTime;
        this.messageStatus = messageStatus;
        this.messageReadTime = messageReadTime;
    }
}
