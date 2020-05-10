package com.example.final_project;

public class Userinformation {

    public String userName;
    public String userType;
    public String phoneNumber;
    public String latitude;
    public String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Userinformation() {
    }

    public Userinformation(String userName, String userType, String phoneNumber, String latitude, String longitude) {
        this.userName = userName;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
