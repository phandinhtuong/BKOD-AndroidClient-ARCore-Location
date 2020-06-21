package com.example.administrator.bkod_androidclient.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.TourTimesheet;
import com.google.android.gms.maps.model.LatLng;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class TourTimesheetAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    // Thu tu tour
    private int tourOrder;
    // Quan ly gps
    private LocationManager locationManager;
    public TourTimesheetAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet().size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getTourOrder() {
        return tourOrder;
    }

    public void setTourOrder(int tourOrder) {
        this.tourOrder = tourOrder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TourTimesheetAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new TourTimesheetAdapter.ViewHolder();
//            DataBindingUtil.inflate(mLayoutInflater, R.layout.tour_timesheet_item_layout,null,false);
            convertView = mLayoutInflater.inflate(R.layout.tour_timesheet_item_layout, null);
            holder.mTime = (TextView) convertView.findViewById(R.id.txt_timesheet_time);
            holder.mClassroom = (TextView) convertView.findViewById(R.id.txt_timesheet_classroom);
            holder.mDistance = (TextView) convertView.findViewById(R.id.txt_timesheet_distance);
            convertView.setTag(holder);
        } else {
            holder = (TourTimesheetAdapter.ViewHolder) convertView.getTag();
        }
        TourTimesheet timesheet = (TourTimesheet) getItem(position);
        // Set thoi gian chang
        holder.mTime.setText(timesheet.getmStartTime() + " - " + timesheet.getmEndTime());
        // Set ten phong hoc trong chang
        holder.mClassroom.setText(timesheet.getmBuildingName() + "-" + timesheet.getmClassroomName());
        // Set khoang cach toi phong ho
        if (ActivityCompat.checkSelfPermission(ActivityManager.getInstance().getCurrentActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityManager.getInstance().getCurrentActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Kiem tra xem co bat gps khong
            locationManager = (LocationManager) ActivityManager.getInstance().getCurrentActivity().getSystemService(ActivityManager.getInstance().getCurrentActivity().LOCATION_SERVICE);
            boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (gpsStatus == true) {
                // Vi tri hien tai cua minh
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (currentLocation != null) {
                    LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    // Vi tri cua toa nha
                    float[] results = new float[1];
                    Location.distanceBetween(myLocation.latitude, myLocation.longitude,
                            timesheet.getmBuildingLocation().latitude, timesheet.getmBuildingLocation().longitude, results);
                    // Set khoang cach toi toa nha
                    holder.mDistance.setText((int)results[0] + "m");
                }
            } else {
                // Neu chua bat gps thi set text la chua bat gps
                holder.mDistance.setTextSize(15);
                holder.mDistance.setText(ActivityManager.getInstance().getCurrentActivity().getString(R.string.disabled_gps));
            }
        } else {
            // Neu khong cho truy cap gps thi set text la kiem tra quyen gps
            holder.mDistance.setTextSize(12);
            holder.mDistance.setText(ActivityManager.getInstance().getCurrentActivity().getString(R.string.check_gps_permission));
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView mTime;
        private TextView mClassroom;
        private TextView mDistance;
    }
}