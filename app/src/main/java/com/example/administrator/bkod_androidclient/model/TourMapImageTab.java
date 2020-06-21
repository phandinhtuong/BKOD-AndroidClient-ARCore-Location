package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.TourTabsActivity;
import com.example.administrator.bkod_androidclient.databinding.TabTourMapImageBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;

public class TourMapImageTab extends Fragment {
    View view;
    public TourMapImageTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabTourMapImageBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_tour_map_image, container, false);
        // Luu binding
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabTourMapImageBinding(dataBinding);
        // Khoi tao map image fragment
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initMapImageFragment();
        view = dataBinding.getRoot();
        return view;
    }
}