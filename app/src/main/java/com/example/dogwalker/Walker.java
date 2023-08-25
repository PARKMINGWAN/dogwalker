package com.example.dogwalker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Walker {
    private String name;
    private String id;
    private String pwd;
    private String tel;
    private String Addr;
    private String career;
    private String nurture;

    private int img;



    public Walker() {
    }

    public Walker(String name, String id, String pwd, String tel, String addr, String career, String nurture, int img) {
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.tel = tel;
        Addr = addr;
        this.career = career;
        this.nurture = nurture;
        this.img = img;
    }

    public Walker(String name, String tel) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getNurture() {
        return nurture;
    }

    public void setNurture(String nurture) {
        this.nurture = nurture;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
