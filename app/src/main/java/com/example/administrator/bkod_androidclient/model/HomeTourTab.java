package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.databinding.TabHomeTourBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;

public class HomeTourTab extends Fragment {
    private View view;
    public HomeTourTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabHomeTourBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_home_tour, container, false);
        // Luu binding
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabHomeTourBinding(dataBinding);
        // Khoi tao tour fragment
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initTourFragment();
        view = dataBinding.getRoot();
//        view = inflater.inflate(R.layout.tab_home_tour, container, false);
        return view;
    }
}
