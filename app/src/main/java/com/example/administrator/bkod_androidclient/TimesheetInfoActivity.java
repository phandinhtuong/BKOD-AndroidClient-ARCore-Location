package com.example.administrator.bkod_androidclient;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.bkod_androidclient.model.TourTimesheet;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class TimesheetInfoActivity extends BaseActivity {
    // So thu tu tour
    private int tourOrder;
    // So thu tu timesheet
    private int timesheetOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lien ket layout voi activity
        ActivityManager.getInstance().activityTimesheetInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_timesheet_info);
        // Lay so thu tu tour
        Intent i = getIntent();
        tourOrder = i.getIntExtra("TourOrder", 0);
        // Lay so thu tu chang
        timesheetOrder = i.getIntExtra("TimesheetOrder", 0);
        // Thay doi tieu de
        ActivityManager.getInstance().activityTimesheetInfoBinding.actionBar.actionBarTitle.setText(getString(R.string.timesheet_info));
        // Lay timesheet hien tai
        TourTimesheet timesheet = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet().get(timesheetOrder);
        // Set thoi gian chang
        ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetTimeData.setText(timesheet.getmStartTime() + " - " + timesheet.getmEndTime());
        // Set ten toa  nha
        ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetBuildingNameData.setText(timesheet.getmBuildingName());
        // Set ten phong hoc
        ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetClassroomNameData.setText(timesheet.getmClassroomName());
        // Set tang
        ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetClassroomFloorData.setText(String.valueOf(timesheet.getmClassroomFloor()));
        // Set ten nguoi quan ly
        ActivityManager.getInstance().activityTimesheetInfoBinding.managerNameData.setText(timesheet.getmClassroomManager().get(0).getmName());
        // Set email
        ActivityManager.getInstance().activityTimesheetInfoBinding.managerEmailData.setText(timesheet.getmClassroomManager().get(0).getmEmail());
        // Set so dien thoai
        ActivityManager.getInstance().activityTimesheetInfoBinding.managerPhoneNumberData.setText(timesheet.getmClassroomManager().get(0).getmEmail());
        // Set ghi chu
        if (timesheet.getmClassroomNote() != null && !timesheet.getmClassroomNote().equals("") && !timesheet.getmClassroomNote().equals("null")) {
            ActivityManager.getInstance().activityTimesheetInfoBinding.classroomNoteData.setText(timesheet.getmClassroomNote());
        }
        // Tat progress
        progressOff();
    }

    //TODO: Bat progress
    public void progressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoProgress.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoProgress.setVisibility(View.VISIBLE);
                }
                if (ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoLayout.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat progress
    public void progressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoProgress.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoProgress.setVisibility(View.GONE);
                }
                if (ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoLayout.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityTimesheetInfoBinding.timesheetInfoLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
