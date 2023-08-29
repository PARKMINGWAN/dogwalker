package com.example.dogwalker;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OwnerProfile {

    String addr,bread,dogAge,dogName,WalkTime,uid,dogUUID,ownerTel;

    public String getDogUUID() {
        return dogUUID;
    }

    public void setDogUUID(String dogUUID) {
        this.dogUUID = dogUUID;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    String isReservation;

    public String getOwnerUUID() {
        return dogUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.dogUUID = ownerUUID;
    }

    public String getIsReservation() {
        return isReservation;
    }

    public void setIsReservation(String isReservation) {
        this.isReservation = isReservation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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
        this.isReservation="0";
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
