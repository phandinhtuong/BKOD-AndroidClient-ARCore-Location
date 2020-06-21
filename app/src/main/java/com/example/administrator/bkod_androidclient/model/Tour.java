package com.example.administrator.bkod_androidclient.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Tour {
    // Id cua tour
    private int mTourId;
    // Ten cua tour
    private String mName;
    // Duong dan den hinh anh minh hoa tour
    private String mImageUrl;
    // Vai tro cua minh trong tour
    private int mFunction;
    // Ngay tien hanh tour
    private Date mDate;
    // Duong dan den hinh anh ban do tour
    private String mMapImageUrl;
    // Danh sach thanh vien trong tour
    private ArrayList<TourMember> mTourMember;
    // Danh sach chang trong tour
    private ArrayList<TourTimesheet> mTourTimesheet;
    // TODO: Constructor
    public Tour(int tourId, String name, String imageUrl, int function, Date date, String mapImageUrl, ArrayList<TourMember> tourMember, ArrayList<TourTimesheet> tourTimesheet) {
        this.mTourId = tourId;
        this.mName = name;
        this.mImageUrl = imageUrl;
        this.mFunction = function;
        this.mDate = date;
        this.mMapImageUrl = mapImageUrl;
        this.mTourMember = tourMember;
        this.mTourTimesheet = tourTimesheet;
    }

    public int getmTourId() {
        return mTourId;
    }

    public void setmTourId(int mTourId) {
        this.mTourId = mTourId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmFunction() {
        return mFunction;
    }

    public void setmFunction(int mFunction) {
        this.mFunction = mFunction;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public ArrayList<TourMember> getmTourMember() {
        return mTourMember;
    }

    public void setmTourMember(ArrayList<TourMember> mTourMember) {
        this.mTourMember = mTourMember;
    }

    public ArrayList<TourTimesheet> getmTourTimesheet() {
        return mTourTimesheet;
    }

    public void setmTourTimesheet(ArrayList<TourTimesheet> mTourTimesheet) {
        this.mTourTimesheet = mTourTimesheet;
    }

    public String getmMapImageUrl() {
        return mMapImageUrl;
    }

    public void setmMapImageUrl(String mMapImageUrl) {
        this.mMapImageUrl = mMapImageUrl;
    }

    // Kiem tra xem ngay truyen vao truoc hay sau ngay hom nay. -1 la truoc, 1 la sau, 0 la hom nay
    public static int afterToday (Date date) {
        // Lay ngay hom nay
        Date today = new Date();
        // Kiem tra xem ngay truyen vao la truoc hay sau hom nay. 0: hom nay, 1: sau, -1: truoc
        int dateEqual = 0;
        if (today.getYear() < date.getYear()) {
            // Ngay truyen vao dien ra nam sau
            dateEqual = 1;
        } else if (today.getYear() > date.getYear()) {
            // Ngay truyen vao dien ra nam truoc
            dateEqual = -1;
        } else {
            // Ngay truyen vao dien ra nam nay
            if (today.getMonth() < date.getMonth()) {
                // Ngay truyen vao dien ra thang sau
                dateEqual = 1;
            } else if (today.getMonth() > date.getMonth()) {
                // Ngay truyen vao dien ra thang truoc
                dateEqual = -1;
            } else {
                // Ngay truyen vao dien ra thang nay
                if (today.getDate() < date.getDate()) {
                    // Ngay truyen vao dien ra hom sau
                    dateEqual = 1;
                } else if (today.getDate() > date.getDate()) {
                    // Ngay truyen vao dien ra hom truoc
                    dateEqual = -1;
                } else {
                    // Ngay truyen vao dien ra hom nay
                    dateEqual = 0;
                }
            }
        }
        return dateEqual;
    }

    // Kiem tra xem gio truyen vao truoc hay sau gio hien tai. -1 la truoc, 1 la sau, 0 la gio nay
    public static int afterCurrentHour (Time time) {
        // Lay ngay gio hom nay
        Date today = new Date();
        // Kiem tra xem gio truyen vao la truoc hay sau gio nay. 0: gio nay, 1: gio sau, -1: gio truoc
        int timeEqual = 0;
        if (today.getHours() < time.getHours()) {
            // Gio sau
            timeEqual = 1;
        } else if (today.getHours() > time.getHours()) {
            // Gio truoc
            timeEqual = -1;
        } else {
            // Gio nay, kiem tra phut
            if (today.getMinutes() < time.getMinutes()) {
                // Phut sau
                timeEqual = 1;
            } else if (today.getMinutes() >= time.getMinutes()) {
                // Phut truoc
                timeEqual = -1;
            }
        }
        return timeEqual;
    }
}