package com.example.administrator.bkod_androidclient.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

public class TourTimesheet {
    // Id chang
    private int mTimesheetId;
    // Thoi diem bat dau chang
    private Time mStartTime;
    // Thoi diem ket thuc chang
    private Time mEndTime;
    // Id lop hoc chang se tham quan
    private int mClassroomId;
    // Tang cua lop hoc
    private int mClassroomFloor;
    // Ten cua lop hoc
    private String mClassroomName;
    // Ten day du/ ten khac cua lop hoc
    private String mClassroomSubName;
    // Ghi chu ve lop hoc
    private String mClassroomNote;
    // Ten toa nha chua lop hoc
    private String mBuildingName;
    // Ten day du/ ten khac cua toa nha
    private String mBuildingSubName;
    // Toa do toa nha tren ban do
    private LatLng mBuildingLocation;
    // Ghi chu ve toa nha
    private String mBuildingNote;
    // Danh sach nhung nguoi quan ly lop hoc
    private ArrayList<ClassroomManager> mClassroomManager;

    public TourTimesheet(int timesheetId, Time startTime, Time endTime, int classroomId, int classroomFloor, String classroomName, String classroomSubName, String classroomNote, String buildingName, String buildingSubName, LatLng buildingLocation, String buildingNote, ArrayList<ClassroomManager> classroomManager) {
        this.mTimesheetId = timesheetId;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mClassroomId = classroomId;
        this.mClassroomFloor = classroomFloor;
        this.mClassroomName = classroomName;
        this.mClassroomSubName = classroomSubName;
        this.mClassroomNote = classroomNote;
        this.mBuildingName = buildingName;
        this.mBuildingSubName = buildingSubName;
        this.mBuildingLocation = buildingLocation;
        this.mBuildingNote = buildingNote;
        this.mClassroomManager = classroomManager;
    }

    public int getmTimesheetId() {
        return mTimesheetId;
    }

    public void setmTimesheetId(int mTimesheetId) {
        this.mTimesheetId = mTimesheetId;
    }

    public Time getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(Time mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Time getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(Time mEndTime) {
        this.mEndTime = mEndTime;
    }

    public int getmClassroomId() {
        return mClassroomId;
    }

    public void setmClassroomId(int mClassroomId) {
        this.mClassroomId = mClassroomId;
    }

    public int getmClassroomFloor() {
        return mClassroomFloor;
    }

    public void setmClassroomFloor(int mClassroomFloor) {
        this.mClassroomFloor = mClassroomFloor;
    }

    public String getmClassroomName() {
        return mClassroomName;
    }

    public void setmClassroomName(String mClassroomName) {
        this.mClassroomName = mClassroomName;
    }

    public String getmClassroomSubName() {
        return mClassroomSubName;
    }

    public void setmClassroomSubName(String mClassroomSubName) {
        this.mClassroomSubName = mClassroomSubName;
    }

    public String getmClassroomNote() {
        return mClassroomNote;
    }

    public void setmClassroomNote(String mClassroomNote) {
        this.mClassroomNote = mClassroomNote;
    }

    public String getmBuildingName() {
        return mBuildingName;
    }

    public void setmBuildingName(String mBuildingName) {
        this.mBuildingName = mBuildingName;
    }

    public String getmBuildingSubName() {
        return mBuildingSubName;
    }

    public void setmBuildingSubName(String mBuildingSubName) {
        this.mBuildingSubName = mBuildingSubName;
    }

    public LatLng getmBuildingLocation() {
        return mBuildingLocation;
    }

    public void setmBuildingLocation(LatLng mBuildingLocation) {
        this.mBuildingLocation = mBuildingLocation;
    }

    public String getmBuildingNote() {
        return mBuildingNote;
    }

    public void setmBuildingNote(String mBuildingNote) {
        this.mBuildingNote = mBuildingNote;
    }

    public ArrayList<ClassroomManager> getmClassroomManager() {
        return mClassroomManager;
    }

    public void setmClassroomManager(ArrayList<ClassroomManager> mClassroomManager) {
        this.mClassroomManager = mClassroomManager;
    }
}
