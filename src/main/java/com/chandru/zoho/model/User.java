package com.chandru.zoho.model;

public class User {
    private int Id;
    private String userFirstName;
    private String userLastName;
    private String userNumber;
    private String userEmail;
    private String userType;
    private String lastCall;

    public User(int id, String userFirstName, String userLastName, String userNumber, String userEmail, String userType, String lastCall) {
        Id = id;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
        this.userType = userType;
        this.lastCall = lastCall;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLastCall() {
        return lastCall;
    }

    public void setLastCall(String lastCall) {
        this.lastCall = lastCall;
    }
}
