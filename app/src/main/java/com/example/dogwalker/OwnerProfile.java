package com.example.dogwalker;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OwnerProfile {
    String addr;
    String bread;
    String dogAge;
    String dogName;
    String WalkTime;

    double longitude; // 위도
    double latitude; // 경도

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public OwnerProfile(String addr, String bread, String dogAge, String walkTime, String dogName) {
        this.addr = addr;
        this.bread = bread;
        this.dogAge = dogAge;
        this.WalkTime = walkTime;
        this.dogName = dogName;
    }

    public OwnerProfile() {
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBread() {
        return bread;
    }

    public void setBread(String bread) {
        this.bread = bread;
    }

    public String getDogAge() {
        return dogAge;
    }

    public void setDogAge(String dogAge) {
        this.dogAge = dogAge;
    }

    public String getWalkTime() {
        return WalkTime;
    }

    public void setWalkTime(String walkTime) {
        WalkTime = walkTime;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }


}
