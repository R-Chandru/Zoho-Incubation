package com.chandru.zoho.model;

public class PrivateMessage {
    int Id;
    String userName;
    String userNumber;
    String userProfilePic;
    String userEmail;
    String userDescription;
    String userType;
    String lastSeen;
    String lastMessage;

    public PrivateMessage(int id, String userName, String userNumber, String userProfilePic,
                          String userEmail, String userDescription, String userType,
                          String lastSeen, String lastMessage) {
        Id = id;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userProfilePic = userProfilePic;
        this.userEmail = userEmail;
        this.userDescription = userDescription;
        this.userType = userType;
        this.lastSeen = lastSeen;
        this.lastMessage = lastMessage;
    }
}


