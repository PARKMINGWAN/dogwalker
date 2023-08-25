package com.example.dogwalker;

public class Owner {
    private String id;
    private String name;
    private String pwd;
    private String tel;
    private String addr;
    private String breed;
    private String dog_age;
    private String dog_walk;
    private int img;

    public Owner(String id, String name, String pwd, String tel, String addr, String breed) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.tel = tel;
        this.addr = addr;
        this.breed = breed;
    }

    public Owner(String id, String name, String pwd, String tel, String addr, String breed, String dog_age, String dog_walk, int img) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.tel = tel;
        this.addr = addr;
        this.breed = breed;
        this.dog_age = dog_age;
        this.dog_walk = dog_walk;
        this.img = img;
    }

    public Owner() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDog_age() {
        return dog_age;
    }

    public void setDog_age(String dog_age) {
        this.dog_age = dog_age;
    }

    public String getDog_walk() {
        return dog_walk;
    }

    public void setDog_walk(String dog_walk) {
        this.dog_walk = dog_walk;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
