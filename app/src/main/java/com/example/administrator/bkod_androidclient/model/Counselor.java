package com.example.administrator.bkod_androidclient.model;

import java.util.ArrayList;

public class Counselor {
    // Id cua tu van vien
    private int userId;
    // Ten dang nhap cua tu van vien
    private String username;
    // Ten day du cua tu van vien
    private String fullname;
    // Gioi tinh cua tu van vien
    private int gender;
    // Trang thai dang nhap cua tu van vien
    private int state;

    public Counselor(int mUserId, String mUsername, String mFullname, int mGender, int mState) {
        this.userId = mUserId;
        this.username = mUsername;
        this.fullname = mFullname;
        this.gender = mGender;
        this.state = mState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}