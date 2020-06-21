package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.TourTabsActivity;
import com.example.administrator.bkod_androidclient.databinding.TabTourInfoBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;

public class TourInfoTab extends Fragment {
    View view;
    public TourInfoTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabTourInfoBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_tour_info, container, false);
        // Luu binding
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabTourInfoBinding(dataBinding);
        // Khoi tao info fragment
        ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initInfoFragment();
        view = dataBinding.getRoot();
//        view = inflater.inflate(R.layout.tab_home_tour, container, false);
        return view;
    }
}