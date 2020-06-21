package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.TourTabsActivity;
import com.example.administrator.bkod_androidclient.databinding.TabTourMapBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;

public class TourMapTab extends Fragment {
    View view;
    public TourMapTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabTourMapBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_tour_map, container, false);
        // Luu binding
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabTourMapBinding(dataBinding);
        // Khoi tao map fragment
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initMapFragment();
        view = dataBinding.getRoot();
//        view = inflater.inflate(R.layout.tab_home_tour, container, false);
        return view;
    }
}