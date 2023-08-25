package com.example.dogwalker;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OwnerProfile {
    String addr;
    String bread;
    String dogAge;

    String WalkTime;

    public OwnerProfile(String addr, String bread, String dogAge, String walkTime, String dogName) {
        this.addr = addr;
        this.bread = bread;
        this.dogAge = dogAge;
        WalkTime = walkTime;
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

    String dogName;
}
