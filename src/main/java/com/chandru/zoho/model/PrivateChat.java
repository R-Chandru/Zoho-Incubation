package com.chandru.zoho.model;

public class PrivateChat {
    int Id;
    String userName;
    String userNumber;
    String userProfilePic;
    String userEmail;
    String userDescription;
    String userType;
    String lastMessage;
    String messageTime;
    String messageStatus;

    public PrivateChat(int id, String userName, String userNumber, String userProfilePic,
                       String userEmail, String userDescription, String userType,
                       String messageTime, String lastMessage, String messageStatus) {
        Id = id;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userProfilePic = userProfilePic;
        this.userEmail = userEmail;
        this.userDescription = userDescription;
        this.userType = userType;
        this.messageTime = messageTime;
        this.lastMessage = lastMessage;
        this.messageStatus = messageStatus;
    }
}