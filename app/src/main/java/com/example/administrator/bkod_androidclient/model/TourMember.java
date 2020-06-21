package com.example.administrator.bkod_androidclient.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import Classes.UserInfo;

// Class thanh vien trong tour
public class TourMember {
    public TourMember(UserInfo userInfo, int function, LatLng location, String note) {
        this.userInfo = userInfo;
        this.mFunction = function;
        this.mLocation = location;
        this.mNote = note;
    }

    // Vai tro trong tour
    private int mFunction;
    // Vi tri tren google map
    private LatLng mLocation;
    // Ghi chu ve thanh vien
    private String mNote;
    // Thong tin ca nhan cua thanh vien
    private UserInfo userInfo;

    public int getmFunction() {
        return mFunction;
    }

    public void setmFunction(int mFunction) {
        this.mFunction = mFunction;
    }

    public LatLng getmLocation() {
        return mLocation;
    }

    public void setmLocation(LatLng mLocation) {
        this.mLocation = mLocation;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}