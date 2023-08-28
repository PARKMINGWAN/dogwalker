package com.example.dogwalker;

public class ApplicationWalkerProfile {
    String walkerName;
    String walkerAddr;
    String walkerNurture;
    String walkerCareer;
    String walkerTel;
    String Uid;
    boolean reservation =false;

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getWalkerUUID() {
        return WalkerUUID;
    }

    public void setWalkerUUID(String walkerUUID) {
        this.WalkerUUID = walkerUUID;
    }

    String WalkerUUID;
    public ApplicationWalkerProfile(String walkerName, String walkerAddr, String walkerNurture, String walkerCareer, String walkerTel) {
        this.walkerName = walkerName;
        this.walkerAddr = walkerAddr;
        this.walkerNurture = walkerNurture;
        this.walkerCareer = walkerCareer;
        this.walkerTel = walkerTel;
    }

    public ApplicationWalkerProfile() {
    }

    public String getWalkerName() {
        return walkerName;
    }

    public void setWalkerName(String walkerName) {
        this.walkerName = walkerName;
    }

    public String getWalkerAddr() {
        return walkerAddr;
    }

    public void setWalkerAddr(String walkerAddr) {
        this.walkerAddr = walkerAddr;
    }

    public String getWalkerNurture() {
        return walkerNurture;
    }

    public void setWalkerNurture(String walkerNurture) {
        this.walkerNurture = walkerNurture;
    }

    public String getWalkerCareer() {
        return walkerCareer;
    }

    public void setWalkerCareer(String walkerCareer) {
        this.walkerCareer = walkerCareer;
    }

    public String getWalkerTel() {
        return walkerTel;
    }

    public void setWalkerTel(String walkerTel) {
        this.walkerTel = walkerTel;
    }
}
