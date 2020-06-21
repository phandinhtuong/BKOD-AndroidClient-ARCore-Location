package com.example.administrator.bkod_androidclient;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.administrator.bkod_androidclient.ar.ArActivity;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.administrator.bkod_androidclient.adapter.TourMemberAdapter;
import com.example.administrator.bkod_androidclient.databinding.MapMemberPositionLayoutBinding;
import com.example.administrator.bkod_androidclient.model.TourMember;
import com.example.administrator.bkod_androidclient.model.TourTimesheet;
import com.example.administrator.bkod_androidclient.adapter.TourTimesheetAdapter;
import com.example.administrator.bkod_androidclient.databinding.MapNumberMarkerLayoutBinding;
import com.example.administrator.bkod_androidclient.databinding.TabTourInfoBinding;
import com.example.administrator.bkod_androidclient.databinding.TabTourMapBinding;
import com.example.administrator.bkod_androidclient.databinding.TabTourMapImageBinding;
import com.example.administrator.bkod_androidclient.model.Tour;
import com.example.administrator.bkod_androidclient.model.TourInfoTab;
import com.example.administrator.bkod_androidclient.model.TourMapImageTab;
import com.example.administrator.bkod_androidclient.model.TourMapTab;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Classes.UserInfo;
import Manager.ActivityManager;
import Manager.ConversationManager;
import Manager.OnlineManager;
import Manager.TourInfoManager;

public class TourTabsActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    // Lop rang buoc tab map voi binding la TabTourMapBinding
    private TabTourMapBinding tabTourMapBinding;
    // Lop rang buoc tab info binding la TabTourInfoBinding
    private TabTourInfoBinding tabTourInfoBinding;
    // Lop rang buoc tab map image binding la TabTourMapImageBinding
    private TabTourMapImageBinding tabTourMapImageBinding;
    // Ban do
    @Nullable
    private GoogleMap mMap;
    // Quan ly gps
    private LocationManager locationManager;
    // So thu tu tour
    private int tourOrder = 0;
    // Vi tri gps
    private LatLng myLocation = null;
    // Ve danh sach marker va route 1 lan khi khoi dong
    private boolean firstDrawComponents = true;
    // Yeu cau bat gps 1 lan
    private boolean askGps = true;
    // Trang thai gps
    private boolean GpsStatus;
    // Lan dau co thong tin vi tri
    private boolean firstLocation = true;
    // Danh sach thanh vien va chang
    private TourMemberAdapter tourMemberAdapter;
    private TourTimesheetAdapter tourTimesheetAdapter;

    //Array list of markers of the tour to display on AR Camera
    public static ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lien ket layout voi activity
        ActivityManager.getInstance().activityTourTabsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tour_tabs);
        // Gan activity hien tai
        ActivityManager.getInstance().setCurrentActivity(this);
        // Lay so thu tu tour
        Intent i = getIntent();
        tourOrder = i.getIntExtra("TourOrder", 0);
        // Them cac tab vao man hinh
        addControl();
    }
    public void arActivity(View v){
        startActivity(new Intent(TourTabsActivity.this, ArActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Gan activity hien tai
        ActivityManager.getInstance().setCurrentActivity(this);
    }

    // TODO: Kiem tra trang thai gps
    public void GPSStatus() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // TODO: Gan danh sach cac tab
    private void addControl() {
        // Gan adapter va danh sach tab
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        // Them noi dung cac tab
        adapter.AddFragment(new TourMapTab());
        adapter.AddFragment(new TourInfoTab());
        adapter.AddFragment(new TourMapImageTab());
        // Set adapter danh sach tab
        ViewPager viewPager = ActivityManager.getInstance().activityTourTabsBinding.viewpager;
        TabLayout tabLayout = ActivityManager.getInstance().activityTourTabsBinding.tablayout;
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        // Them vach ngan cach cac tab
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorPrimaryDark));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        // Icon cac tab
        tabLayout.getTabAt(0).setIcon(R.drawable.tour_map_icon_select);
        tabLayout.getTabAt(1).setIcon(R.mipmap.tour_info_icon);
        tabLayout.getTabAt(2).setIcon(R.mipmap.tour_map_image_icon);

        // Doi mau tab icon khi click
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.drawable.tour_map_icon_select);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.tour_info_icon_select);
                            break;
                        case 2:
                            tab.setIcon(R.mipmap.tour_map_image_icon_select);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    super.onTabUnselected(tab);
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.mipmap.tour_map_icon);
                            break;
                        case 1:
                            tab.setIcon(R.mipmap.tour_info_icon);
                            break;
                        case 2:
                            tab.setIcon(R.mipmap.tour_map_image_icon);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    super.onTabReselected(tab);
                }
            }
        );
    }

    // TODO: Duoc goi khi khoi tao xong ban do
    private static final int ACCESS_LOCATION_PERMISSION_CODE = 1;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Chuyen camera ve dai hoc bach khoa
        LatLng hust = new LatLng(21.0060556, 105.8426507);
        // Them marker tai bach khoa
//        mMap.addMarker(new MarkerOptions().draggable(false).position(hust).title("Đại học Bách khoa Hà Nội"));
        // Di chuyen camera ve dai hoc bach khoa
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hust, 15));
        // Bat nut vi tri
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Neu khong co quyen truy cap gps thi yeu cau
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION_PERMISSION_CODE);
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        if (GpsStatus == true) {
            // Neu day la lan dau co thong tin vi tri thi chuyen camera ve vi tri nay
            if (firstLocation == true) {
                // Neu bat gps roi thi thong bao
                ActivityManager.getInstance().makeLongToast("Đang định vị...");
                // Lay vi tri hien tai
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (currentLocation != null) {
                    myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    // Di chuyen camera den vi tri cua minh
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                    // Khong di chuyen them 1 lan nao nua
                    firstLocation = false;
                    // Gui vi tri cua minh len server
                    String myLocationMessage = String.format("{\"COMMAND\":\"MEMBER_CURRENT_LOCATION\", \"LATITUDE\": \"%s\", \"LONGITUDE\": \"%s\"}", myLocation.latitude, myLocation.longitude);
                    OnlineManager.getInstance().sendMessage(myLocationMessage);
                    // Refresh danh sach timesheet
                    if(tourTimesheetAdapter != null){
                        tourTimesheetAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else if (askGps == true){
            // Neu chua bat gps thi hien thi thong bao 1 lan duy nhat
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        new AlertDialog.Builder(TourTabsActivity.this)
                                .setTitle(getString(R.string.alert_title))
                                .setMessage(getString(R.string.enable_gps))
                                .setCancelable(false)
                                .setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Mo cai dat GPS
                                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent1);
                                    }
                                }).show();
                    }
                }
            });
            // Khong hoi bat gps nua
            askGps = false;
        }
        // Ve marker va route lan dau
        if (firstDrawComponents == true) {
            drawComponentsHandler.post(drawComponentsRunnable);
            // Khong ve lai lan nao nua
            firstDrawComponents = false;
        }
        // Tat progress ban do
        mapProgressOff();
    }

    // Handler ve cac thanh phan trong ban do
    private Handler drawComponentsHandler = new Handler();
    // TODO: Ve lai cac thanh phan giao dien sau moi 1p
    private Runnable drawComponentsRunnable = new Runnable() {
        @Override
        public void run() {
            // Ve marker va route
            drawComponents();
            // Ve lai sau 1p
            drawComponentsHandler.postDelayed(drawComponentsRunnable, 60000);
        }
    };

    // Ve marker va route
    private void drawComponents () {
        // Xoa het nhung gi duoc ve truoc do
        mMap.clear();
        // Ve danh sach vi tri cac thanh vien
        drawMembersLocation();
        // Ve danh sach cac marker
        drawMarkers();
        // Ve danh sach cac tuyen duong
        drawRoutes();
    }

    // Convert view to bitmap
    private Bitmap getMemberLocationBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    // Convert view to bitmap
    private Bitmap getMarkerBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // Goi su kien khi nhan vao vi tri cua minh
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Goi su kien khi nhan vao nut lay vi tri
        return false;
    }

    // Ve danh sach cac marker
    private void drawMarkers () {
        // Danh dau chang chua di qua
        boolean danhDauChangChuaDiQua = false;
        // Ve tat ca cac vi tri chang tren ban do
        for (int i = 0; i < OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet().size(); i++) {

            TourTimesheet timesheet = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet().get(i);
            // Khoi tao vi tri chang
            LatLng position = new LatLng(timesheet.getmBuildingLocation().latitude, timesheet.getmBuildingLocation().longitude);
            // Thay doi icon vi tri chang
            MapNumberMarkerLayoutBinding markerLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.map_number_marker_layout, null, false);
            // Thay doi so thu tu timesheet
            markerLayoutBinding.number.setText(String.valueOf(i + 1));
            // Thay doi trang thai markup
            // Lay ngay cua tour
            Date tourDate = OnlineManager.getInstance().mTourList.get(tourOrder).getmDate();
            // Kiem tra xem ngay dien ra tour la truoc hay sau hom nay. 0: hom nay, 1: sau, -1: truoc
            int dateEqual = Tour.afterToday(tourDate);
            // Kiem tra xem tour da dien ra chua
            if (dateEqual == -1) {
                // Tour da dien ra, thay doi mau sac markup thanh xam
                markerLayoutBinding.markerImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            } else if (dateEqual == 1) {
                // Tour chua dien ra, thay doi mau sac markup thanh vang
                markerLayoutBinding.markerImage.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            } else {
                // Kiem tra xem chang hien tai la truoc hay sau gio nay. 0: gio nay, 1: gio sau, -1: gio truoc
                int srcStartEqual = Tour.afterCurrentHour(timesheet.getmStartTime());
                int srcEndEqual = Tour.afterCurrentHour(timesheet.getmEndTime());
                if(srcStartEqual == 1){
                    // Chang chua di qua
                    if (danhDauChangChuaDiQua == true) {
                        // Neu la chang sau chang sap toi thi chuyen thanh mau mau vang
                        markerLayoutBinding.markerImage.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                    } else {
                        // Neu la chang ke tiep thi giu nguyen mau do
                        danhDauChangChuaDiQua = true;
                    }
                } else if(srcStartEqual == -1 && srcEndEqual == 1){
                    // Chang dang di qua
                    markerLayoutBinding.markerImage.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                } else{
                    // Chang da di qua
                    markerLayoutBinding.markerImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                }
            }
            // Marker google map
            View markerView = markerLayoutBinding.getRoot();
            // Khoi tao marker
            MarkerOptions markerOptions = new MarkerOptions()
                    .draggable(false)
                    .title(timesheet.getmBuildingName() + '-' + timesheet.getmClassroomName())
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerView)));
            if (timesheet.getmClassroomNote() != null && !timesheet.getmClassroomNote().equals("") && !timesheet.getmClassroomNote().equals("null")) {
                markerOptions.snippet(timesheet.getmClassroomNote());
            }
            mMap.addMarker(markerOptions);
            // add marker to the array list to display on AR Camera
            markerList.add(markerOptions);
            // Goi su kien khi nhan vao tieu de marker
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    // TODO: Chuyen sang man hinh thong tin chang khi nhan vao tieu de chang
                    openTimesheetInfo(marker.getTitle());
                }
            });
        }
    }

    // Ve danh sach vi tri cac thanh vien
    private void drawMembersLocation () {
        // Ve tat ca cac vi tri thanh vien tren ban do
        ArrayList<TourMember> tourMembers = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourMember();
        for (int i = 0; i < tourMembers.size(); i++) {
            TourMember tourMember = tourMembers.get(i);
            // Neu khong co vi tri thi khong ve
            if (tourMember.getmLocation() == null) {
                break;
            }
            // Thay doi mau vi tri thanh vien
            MapMemberPositionLayoutBinding memberLocationLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.map_member_position_layout, null, false);
            // Thay doi trang thai markup
            // Kiem tra xem vai tro cua thanh vien trong tour
            int function = tourMember.getmFunction();
            // Kiem tra xem tour da dien ra chua
            if (function == 1) {
                // Truong doan, thay doi mau sac markup thanh do
                memberLocationLayoutBinding.memberLocationImage.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            } else {
                // Cac thanh vien con lai, thay doi mau sac markup thanh xanh
                memberLocationLayoutBinding.memberLocationImage.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
            }
            // Marker google map
            View markerView = memberLocationLayoutBinding.getRoot();
            // Khoi tao marker
            MarkerOptions markerOptions = new MarkerOptions()
                    .draggable(false)
                    .title(tourMember.getUserInfo().getFullName())
                    .position(tourMember.getmLocation())
                    .icon(BitmapDescriptorFactory.fromBitmap(getMemberLocationBitmapFromView(markerView)));
            if (tourMember.getmFunction() == 0) {
                // Thanh vien
                markerOptions.snippet(getString(R.string.tour_function_member));
            } else if (tourMember.getmFunction() == 1) {
                // Truong doan
                markerOptions.snippet(getString(R.string.tour_function_leader));
            } else if (tourMember.getmFunction() == 2) {
                // Pho doan
                markerOptions.snippet(getString(R.string.tour_function_vice_leader));
            } else {
                // Phu huynh
                markerOptions.snippet(getString(R.string.tour_function_parent));
            }
            mMap.addMarker(markerOptions);

            // Goi su kien khi nhan vao tieu de thanh vien
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    // TODO: Chuyen sang man hinh nhan tin khi nhan vao tieu de thanh vien
                    return;
//                    openTimesheetInfo(marker.getTitle());
                }
            });
        }
    }

    // Ve danh sach cac tuyen duong
    private void drawRoutes () {
        // Lay ngay cua tour
        Date tourDate = OnlineManager.getInstance().mTourList.get(tourOrder).getmDate();
        // Kiem tra xem ngay dien ra tour la truoc hay sau hom nay. 0: hom nay, 1: sau, -1: truoc
        int dateEqual = Tour.afterToday(tourDate);
        // Kiem tra xem tour da dien ra chua
        if (dateEqual == -1) {
            // Tour da dien ra, khong ve gi ca
        } else if (dateEqual == 1) {
            // Tour chua dien ra, khong ve gi ca
        } else {
            // Tour dang dien ra thi kiem tra gio
            /* Chang sap toi mau do, today nam trong khoang src start va src end
               Chang dang di chuyen mau xanh, today nam trong khoang src end va dst start
               Chang vua di qua mau xam, today nam trong khoang dst start va dst end */
            ArrayList<TourTimesheet> timesheets = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet();
            // Danh dau chang dang di qua
            int changDangDiQuaOrder = -1;
            // Danh dau chang sap di qua
            int changSapDiQuaOrder = -1;
            if(Tour.afterCurrentHour(timesheets.get(0).getmStartTime()) == 1){
                // Chang sap di qua mau do
                // Neu chua ve chang sap di, ve duong mau do
                if (timesheets.size() >= 2) {
                    drawRoute(Color.RED, timesheets.get(0).getmBuildingLocation(), timesheets.get(1).getmBuildingLocation());
                }
            } else {
                for (int i = 0; i < timesheets.size() - 1; i++) {
                    // Kiem tra xem chang hien tai la truoc hay sau gio nay. 0: gio nay, 1: gio sau, -1: gio truoc
                    int srcStartEqual = Tour.afterCurrentHour(timesheets.get(i).getmStartTime());
                    int srcEndEqual = Tour.afterCurrentHour(timesheets.get(i).getmEndTime());
                    int dstStartEqual = Tour.afterCurrentHour(timesheets.get(i + 1).getmStartTime());
                    // 2 vi tri chang
                    LatLng srcLocation = timesheets.get(i).getmBuildingLocation();
                    LatLng dstLocation = timesheets.get(i + 1).getmBuildingLocation();
                    if (srcStartEqual == -1 && srcEndEqual == 1) {
                        // Chang dang di qua mau xanh
                        // Neu chua ve chang dang di, ve duong mau xanh
                        drawRoute(Color.BLUE, srcLocation, dstLocation);
                        // Danh dau chang dang di qua
                        changDangDiQuaOrder = i;
                        // Thoat khoi vong lap
                        break;
                    } else if (srcEndEqual == -1 && dstStartEqual == 1) {
                        // Chang sap toi mau do
                        // Neu chua ve chang sap toi, ve duong mau do
                        drawRoute(Color.RED, srcLocation, dstLocation);
                        // Danh dau chang sap di qua
                        changSapDiQuaOrder = i;
                        // Thoat khoi vong lap
                        break;
                    }
                }
            }
            // Kiem tra xem da ve duoc nhung duong nao roi
            if (changDangDiQuaOrder != - 1) {
                // Neu ve duoc chang dang di qua roi thi ve tiep 2 chang con lai
                if (changDangDiQuaOrder < timesheets.size() - 2) {
                    // Neu khong phai chang ket thuc thi ve tiep chang sap di qua
                    // 2 vi tri chang
                    LatLng srcLocation =  timesheets.get(changDangDiQuaOrder + 1).getmBuildingLocation();
                    LatLng dstLocation =  timesheets.get(changDangDiQuaOrder + 2).getmBuildingLocation();
                    drawRoute(Color.RED, srcLocation, dstLocation);
                }
                if (changDangDiQuaOrder > 0) {
                    // Neu khong phai chang bat dau thi ve tiep chang da di qua
                    // 2 vi tri chang
                    LatLng srcLocation =  timesheets.get(changDangDiQuaOrder - 1).getmBuildingLocation();
                    LatLng dstLocation =  timesheets.get(changDangDiQuaOrder).getmBuildingLocation();
                    drawRoute(Color.GRAY, srcLocation, dstLocation);
                }
            }
            if (changSapDiQuaOrder != - 1) {
                // Neu ve duoc chang sap di qua roi thi ve tiep 2 chang con lai
                if (changSapDiQuaOrder > 0) {
                    // Neu khong phai chang bat dau thi ve tiep chang dang di qua
                    // 2 vi tri chang
                    LatLng srcLocation =  timesheets.get(changSapDiQuaOrder - 1).getmBuildingLocation();
                    LatLng dstLocation =  timesheets.get(changSapDiQuaOrder).getmBuildingLocation();
                    drawRoute(Color.BLUE, srcLocation, dstLocation);
                }
                if (changSapDiQuaOrder > 1) {
                    // Neu khong phai chang bat dau thi ve tiep chang da di qua
                    // 2 vi tri chang
                    LatLng srcLocation =  timesheets.get(changSapDiQuaOrder - 2).getmBuildingLocation();
                    LatLng dstLocation =  timesheets.get(changSapDiQuaOrder - 1).getmBuildingLocation();
                    drawRoute(Color.GRAY, srcLocation, dstLocation);
                }
            }
        }
    }

    // Ve 1 duong
    private void drawRoute(int color, LatLng src, LatLng dst){
        // Neu cung 1 toa nha thi ve duong net dut
        double delta = Math.abs(dst.latitude  - src.latitude) + Math.abs(dst.longitude  - src.longitude);
        if (delta  <= 0.00015) {
            // Cung 1 toa nha
            double lat = src.latitude;
            double lng = src.longitude;
            if (color == Color.GRAY){
                // Neu la chang da di qua thi ve thut xuong duoi, sang trai
                lat -= 0.00001f;
                lng -= 0.00001f;
            } else if (color == Color.RED){
                // Neu la chang sap di qua thi ve lui len tren, sang phai
                lat += 0.00001f;
                lng += 0.00001f;
            }
            LatLng position = new LatLng(lat, lng);
            // Ve duong net dut
            drawDashedLeg (color, position, dst);
        } else {
            // Khac toa nha
            // Ve lo trinh tren google map
            String url = getDirectionsUrl(src, dst);
            DownloadTask downloadTask = new DownloadTask();
            // Khoi tao cac thong so cho route
            downloadTask.init(color, src, dst);
            downloadTask.execute(url);
        }
    }

    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DOT);
    // Ve duong net dut
    private void drawDashedLeg(int color, LatLng src, LatLng dst) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(color);
        polyOptions.add(src, dst);
        polyOptions.pattern(PATTERN_POLYGON_ALPHA);
        mMap.addPolyline(polyOptions);
    }

    // TODO: Khoi tao fragment map
    public void initMapFragment (){
        // Thay doi tieu de
        tabTourMapBinding.actionBar.actionBarTitle.setText(R.string.tour_map);
        // Khoi tao ban do
        SupportMapFragment mapFragment = null;
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().getFragments().get(i).getChildFragmentManager().findFragmentById(R.id.tour_map);
            if (mapFragment != null) {
                break;
            }
        }
//        (SupportMapFragment)(((PagerAdapter)ActivityManager.getInstance().activityTourTabsBinding.viewpager.getAdapter()).getFragment(0).getActivity().getSupportFragmentManager().findFragmentById(R.id.tour_map));
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tour_map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener mLocationListener = new LocationListener() {
            // TODO: Duoc goi khi thay doi vi tri
            @Override
            public void onLocationChanged(final Location location) {
                // Lay vi tri hien tai cua minh
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (firstLocation == true) {
                    // Neu day la lan dau co thong tin vi tri thi chuyen camera ve vi tri nay
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                    firstLocation = false;
                }
                // Gui vi tri cua minh len server
                String myLocationMessage = String.format("{\"COMMAND\":\"MEMBER_CURRENT_LOCATION\", \"LATITUDE\": \"%s\", \"LONGITUDE\": \"%s\"}", myLocation.latitude, myLocation.longitude);
                OnlineManager.getInstance().sendMessage(myLocationMessage);
                // Refresh danh sach timesheet
                if (tourTimesheetAdapter != null) {
                    tourTimesheetAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
//                ActivityManager.getInstance().makeLongToast("onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
//                ActivityManager.getInstance().makeLongToast("onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
//                ActivityManager.getInstance().makeLongToast("onProviderDisabled");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Cu 5s thi kiem tra su thay doi vi tri 1 lan
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
        }
        // Kiem tra thong tin vi tri
        GPSStatus();
    }

    // TODO: Khoi tao fragment info
    public void initInfoFragment () {
        // Thay doi tieu de
        tabTourInfoBinding.actionBar.actionBarTitle.setText(R.string.tour_info);
        // Sap xep danh sach thanh vien va chang
        TourInfoManager.sortTour(tourOrder);
        Tour tour = OnlineManager.getInstance().mTourList.get(tourOrder);
        // Gan cac thanh phan giao dien
        // Tour image
        if (!tour.getmImageUrl().equals(null) && !tour.getmImageUrl().equals("")) {
            Picasso.get().load(tour.getmImageUrl()) .into(tabTourInfoBinding.tourIcon);
        }
        // Tour name
        tabTourInfoBinding.tourNameData.setText(tour.getmName());
        // Trang thai
        // Kiem tra xem ngay dien ra tour la truoc hay sau hom nay. 0: hom nay, 1: sau, -1: truoc
        int dateEqual = Tour.afterToday(tour.getmDate());
        if (dateEqual == -1) {
            // Ngung hoat dong
            tabTourInfoBinding.tourStateData.setText(R.string.tour_state_inactive);
            tabTourInfoBinding.imgTourState.setImageResource(R.drawable.offline_icon);
        } else if (dateEqual == 1) {
            // Dang cho
            tabTourInfoBinding.tourStateData.setText(R.string.tour_state_waiting);
            tabTourInfoBinding.imgTourState.setImageResource(R.drawable.waiting_icon);
        } else {
            // Tour dang hoat dong
            tabTourInfoBinding.tourStateData.setText(R.string.tour_state_active);
            tabTourInfoBinding.imgTourState.setImageResource(R.drawable.online_icon);
        }
        // Gan vai tro
        int mFunction = tour.getmFunction();
        // Set vai tro trong tour
        if (mFunction == 0) {
            // Thanh vien
            tabTourInfoBinding.tourFunctionData.setText(getString(R.string.tour_function_member));
        } else if (mFunction == 1) {
            // Truong doan
            tabTourInfoBinding.tourFunctionData.setText(getString(R.string.tour_function_leader));
        } else if (mFunction == 2) {
            // Pho doan
            tabTourInfoBinding.tourFunctionData.setText(getString(R.string.tour_function_vice_leader));
        } else {
            // Phu huynh
            tabTourInfoBinding.tourFunctionData.setText(getString(R.string.tour_function_parent));
        }
        // Ngay dien ra tour
        tabTourInfoBinding.tourDateData.setText(String.format("%02d-%02d-%d", tour.getmDate().getDate(), tour.getmDate().getMonth() + 1, (1900 + tour.getmDate().getYear())));
        // Gan member adapter
        tourMemberAdapter = new TourMemberAdapter(this);
        // Gan tour order
        tourMemberAdapter.setTourOrder(tourOrder);
        // Gan timesheet adapter
        tourTimesheetAdapter = new TourTimesheetAdapter(this);
        // Gan tour order
        tourTimesheetAdapter.setTourOrder(tourOrder);
        // Gan danh sach thanh vien va danh sach chang
        setListView();
        // Tat thong bao dang lay thong tin tour
        infoProgressOff();
    }

    // TODO: Khoi tao fragment info
    public void initMapImageFragment () {
        // Thay doi tieu de
        tabTourMapImageBinding.actionBar.actionBarTitle.setText(R.string.tour_map_image);
        Tour tour = OnlineManager.getInstance().mTourList.get(tourOrder);
        // Gan tour image
        if (!tour.getmMapImageUrl().equals(null) && !tour.getmMapImageUrl().equals("")) {
            Picasso.get()
                .load(tour.getmMapImageUrl())
                .into(tabTourMapImageBinding.tourMapImage);
//            Picasso.with(this).load(tour.getmMapImageUrl()).into(tabTourMapImageBinding.tourMapImage);
        }
        // Tat map image progress
        mapImageProgressOff();
    }

    // TODO: Gan danh sach thanh vien va danh sach chang
    private boolean istourMemberListviewInitial = true;
    private boolean istourTimesheetListviewInitial = true;
    public void setListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tabTourInfoBinding.tourMemberListview.setAdapter(tourMemberAdapter);
                tabTourInfoBinding.tourTimesheetListview.setAdapter(tourTimesheetAdapter);
                // Goi su kien khi nhan vao thanh vien
                tabTourInfoBinding.tourMemberListview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(istourMemberListviewInitial) {
                            this.onNothingSelected(parent);
                            istourMemberListviewInitial = false;
                        } else {
                            // Chuyen sang man hinh tin nhan
                            openMemberMessage(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                // Goi su kien khi nhan vao chang
                tabTourInfoBinding.tourTimesheetListview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(istourTimesheetListviewInitial) {
                            this.onNothingSelected(parent);
                            istourTimesheetListviewInitial = false;
                        } else {
                            // Chuyen sang man hinh timesheet info
                            openTimesheetInfo(position);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
    }

    // TODO: Chuyen sang man hinh nhan tin
    private void openMemberMessage (int position) {
        // Chuyen sang man hinh message neu khong click vao minh
        UserInfo selectedUserInfo = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourMember().get(position).getUserInfo();
        if (OnlineManager.getInstance().userInfo.getUserId() != selectedUserInfo.getUserId()) {
            Intent intent = new Intent(TourTabsActivity.this, MessageActivity.class);
            // Gan so thu tu hoi thoai
            intent.putExtra("ConversationOrder", ConversationManager.findConversationOrderByMemberInfo(selectedUserInfo));
            // Chuyen activity
            TourTabsActivity.this.startActivity(intent);
        }
    }

    // TODO: Chuyen sang man hinh thong tin chang
    private void openTimesheetInfo (int position) {
        // Chuyen sang man hinh timesheet info
        Intent intent = new Intent(TourTabsActivity.this, TimesheetInfoActivity.class);
        // Gan so thu tu tour
        intent.putExtra("TourOrder", tourOrder);
        // Gan so thu tu chang
        intent.putExtra("TimesheetOrder", position);
        // Chuyen activity
        TourTabsActivity.this.startActivity(intent);
    }

    // TODO: Chuyen sang man hinh thong tin chang
    private void openTimesheetInfo (String markerTitle) {
        // Chuyen sang man hinh timesheet info
        Intent intent = new Intent(TourTabsActivity.this, TimesheetInfoActivity.class);
        // Gan so thu tu tour
        intent.putExtra("TourOrder", tourOrder);
        // Gan so thu tu timesheet
        ArrayList<TourTimesheet> currentTimesheets = OnlineManager.getInstance().mTourList.get(tourOrder).getmTourTimesheet();
        for (int i = 0; i < currentTimesheets.size(); i++) {
            String title = currentTimesheets.get(i).getmBuildingName() + "-" + currentTimesheets.get(i).getmClassroomName();
            if (title.equals(markerTitle)) {
                // Neu trung marker title thi gan timesheet order
                intent.putExtra("TimesheetOrder", i);
                // Chuyen activity
                TourTabsActivity.this.startActivity(intent);
                break;
            }
        }
    }

    // Refresh danh sach thanh vien
    public void refreshMemberList () {
        if (tourMemberAdapter != null) {
            tourMemberAdapter.notifyDataSetChanged();
        }
    }

    //TODO: Bat map progress
    public void mapProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourMapBinding.mapProgress.getVisibility() != View.VISIBLE) {
                    tabTourMapBinding.mapProgress.setVisibility(View.VISIBLE);
                }
                if (tabTourMapBinding.mapLayout.getVisibility() != View.GONE) {
                    tabTourMapBinding.mapLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat map progress
    public void mapProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourMapBinding.mapProgress.getVisibility() != View.GONE) {
                    tabTourMapBinding.mapProgress.setVisibility(View.GONE);
                }
                if (tabTourMapBinding.mapLayout.getVisibility() != View.VISIBLE) {
                    tabTourMapBinding.mapLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat info progress
    public void infoProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourInfoBinding.tourInfoProgress.getVisibility() != View.VISIBLE) {
                    tabTourInfoBinding.tourInfoProgress.setVisibility(View.VISIBLE);
                }
                if (tabTourInfoBinding.tourInfoLayout.getVisibility() != View.GONE) {
                    tabTourInfoBinding.tourInfoLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat info progress
    public void infoProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourInfoBinding.tourInfoProgress.getVisibility() != View.GONE) {
                    tabTourInfoBinding.tourInfoProgress.setVisibility(View.GONE);
                }
                if (tabTourInfoBinding.tourInfoLayout.getVisibility() != View.VISIBLE) {
                    tabTourInfoBinding.tourInfoLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat map image progress
    public void mapImageProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourMapImageBinding.tourMapImageProgress.getVisibility() != View.VISIBLE) {
                    tabTourMapImageBinding.tourMapImageProgress.setVisibility(View.VISIBLE);
                }
                if (tabTourMapImageBinding.tourMapImageLayout.getVisibility() != View.GONE) {
                    tabTourMapImageBinding.tourMapImageLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat map image progress
    public void mapImageProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabTourMapImageBinding.tourMapImageProgress.getVisibility() != View.GONE) {
                    tabTourMapImageBinding.tourMapImageProgress.setVisibility(View.GONE);
                }
                if (tabTourMapImageBinding.tourMapImageLayout.getVisibility() != View.VISIBLE) {
                    tabTourMapImageBinding.tourMapImageLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public TabTourMapBinding getTabTourMapBinding() {
        return tabTourMapBinding;
    }

    public void setTabTourMapBinding(TabTourMapBinding tabTourMapBinding) {
        this.tabTourMapBinding = tabTourMapBinding;
    }

    public TabTourInfoBinding getTabTourInfoBinding() {
        return tabTourInfoBinding;
    }

    public void setTabTourInfoBinding(TabTourInfoBinding tabTourInfoBinding) {
        this.tabTourInfoBinding = tabTourInfoBinding;
    }

    public TabTourMapImageBinding getTabTourMapImageBinding() {
        return tabTourMapImageBinding;
    }

    public void setTabTourMapImageBinding(TabTourMapImageBinding tabTourMapImageBinding) {
        this.tabTourMapImageBinding = tabTourMapImageBinding;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Duoc goi khi thay doi vi tri
        if (ActivityCompat.checkSelfPermission(TourTabsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TourTabsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Bat gps thi di chuyen camera den vi tri cua minh
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (firstLocation == true) {
                // Neu day la lan dau co thong tin vi tri thi chuyen camera ve vi tri nay
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                firstLocation = false;
            }
            // Gui vi tri cua minh len server
            String myLocationMessage = String.format("{\"COMMAND\":\"MEMBER_CURRENT_LOCATION\", \"LATITUDE\": \"%s\", \"LONGITUDE\": \"%s\"}", myLocation.latitude, myLocation.longitude);
            OnlineManager.getInstance().sendMessage(myLocationMessage);
            // Refresh danh sach timesheet
            if(tourTimesheetAdapter != null){
                tourTimesheetAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Mau cua route
        private int color;
        // Diem bat dau va diem ket thuc
        private LatLng src, dst;
        // Khoi tao cac thong so cho route
        public void init (int mColor, LatLng mSrc, LatLng mDst) {
            color = mColor;
            src = mSrc;
            dst = mDst;
        }
        // TODO: Tai ve danh sach cac duong di tu dia diem nay den dia diem kia
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                // Gui request len
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Sau khi nhan duoc ket qua duong di thi phan tich
            ParserTask parserTask = new ParserTask();
            // Set mau cua route
            parserTask.init (color, src, dst);
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // TODO: Phan tich ket qua duong di tra ve sau khi request
        // Mau cua route
        private int color = Color.RED;
        // Diem bat dau va diem ket thuc
        private LatLng src, dst;
        // Khoi tao cac thong so cho route
        public void init (int mColor, LatLng mSrc, LatLng mDst) {
            color = mColor;
            src = mSrc;
            dst = mDst;
        }
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            // Khoi tao cac doi tuong json
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                // Phan tich cac doi tuong json
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Phan tich cac tuyen duong
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            // Khoi tao danh sach cac diem
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            if (result.size() == 0) {
                // Neu khong co diem nao thi khong phai phan tich
                return;
            }
            for (int i = 0; i < result.size(); i++) {
                // Duyet danh sach tat ca cac diem
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Khoi tao duong di
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    // Them toa do vao dien
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    if (color == Color.GRAY){
                        // Neu la chang da di qua thi ve thut xuong duoi, sang trai
                        lat -= 0.00001f;
                        lng -= 0.00001f;
                    } else if (color == Color.RED){
                        // Neu la chang sap di qua thi ve lui len tren, sang phai
                        lat += 0.00001f;
                        lng += 0.00001f;
                    }
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    // Ve duong net dut diem bat dau
                    if (j == 0) {
                        drawDashedLeg (color, src, position);
                    }
                    // Ve duong net dut diem ket thuc
                    if (j == path.size() - 1) {
                        drawDashedLeg (color, position, dst);
                    }
                }
                // Chinh cac thong so duong di
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(color);
                lineOptions.geodesic(true);
                // Ve duong di tren ban do
                mMap.addPolyline(lineOptions);
            }
        }
    }

    // Sinh ra duong link chi duong
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Parameter toa do nguon
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Parameter toa do dich
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Parameter sensor
        String sensor = "sensor=false";
        // Parameter di bo
        String mode = "mode=walking";
        // Parameter api key
        String key = "key=" + getString(R.string.map_api_key);
        // Gop tat ca parameter lai
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        // Parameter ket qua tra ve dang json
        String output = "json";
        // Duong link sinh ra
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    // Download json tu url
    private String downloadUrl(String strUrl) throws IOException {
        // Khoi tao du lieu tra ve
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            // Khoi tao url
            URL url = new URL(strUrl);
            // Mo ket noi url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Ket noi url
            urlConnection.connect();
            // Lay ket qua tra ve
            iStream = urlConnection.getInputStream();
            // Doc ket qua tra ve
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            // Khoi tao buffer luu ket qua tra ve
            StringBuffer sb = new StringBuffer();
            // Doc tung dong tra ve
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            // Luu du lieu sau khi doc
            data = sb.toString();
            // Dong buffer
            br.close();
        } catch (Exception e) {
            // Neu bi loi thi log ra loi
            e.printStackTrace();
        } finally {
            // Dong stream va ket noi url
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    class DirectionsJSONParser {
        // TODO: Phan tich json de tra ve danh sach tuyen duong
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){
            // Khoi tao cac mang
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                // Truy cap vao phan tu chua danh sach tuyen duong
                jRoutes = jObject.getJSONArray("routes");
                for(int i = 0; i < jRoutes.length(); i++){
                    jLegs = ((JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();
                    // Phan tich cac buoc
                    for(int j = 0; j < jLegs.length(); j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
                        // Phan tich cac diem
                        for(int k = 0; k < jSteps.length(); k++){
                            // Danh sach cac duong thang
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            // Giai ma cac duong thang duoc ve tren ban do
                            List list = decodePoly(polyline);
                            // Gan danh sach cac diem
                            for(int l=0;l <list.size();l++){
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
            return routes;
        }

        // Giai ma cac duong thang duoc ve tren ban do
        private List decodePoly(String encoded) {
            // Danh sach cac duong thang duoc giai ma
            List poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                // Duyet tung duong thang
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;
                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

    public TourMemberAdapter getTourMemberAdapter() {
        return tourMemberAdapter;
    }

    public void setTourMemberAdapter(TourMemberAdapter tourMemberAdapter) {
        this.tourMemberAdapter = tourMemberAdapter;
    }

    // Class quan ly danh sach fragment cua tour
    public class PagerAdapter extends FragmentPagerAdapter {
        // Danh sach fragment
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        // Lay fragment
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        // Them fragment
        public void AddFragment (Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}