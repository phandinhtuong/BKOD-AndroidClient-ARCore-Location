package com.example.administrator.bkod_androidclient.model;

import java.util.Date;

public class ClassroomManager {
    // Id cua nguoi quan ly
    private int mManagerId;
    // Ten nguoi quan ly
    private String mName;
    // Gioi tinh nguoi quan ly
    private int mGender;
    // Ngay sinh cua nguoi quan ly
    private Date mBirthday;
    // Email cua nguoi quan ly
    private String mEmail;
    // So dien thoai cua nguoi quan ly
    private String mPhoneNumber;

    public ClassroomManager(int managerId, String name, int gender, Date birthday, String email, String phoneNumber) {
        this.mManagerId = managerId;
        this.mName = name;
        this.mGender = gender;
        this.mBirthday = birthday;
        this.mEmail = email;
        this.mPhoneNumber = phoneNumber;
    }

    public int getmManagerId() {
        return mManagerId;
    }

    public void setmManagerId(int mManagerId) {
        this.mManagerId = mManagerId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public Date getmBirthday() {
        return mBirthday;
    }

    public void setmBirthday(Date mBirthday) {
        this.mBirthday = mBirthday;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}
