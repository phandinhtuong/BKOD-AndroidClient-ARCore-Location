package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.databinding.TabHomeContactBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class HomeContactTab extends Fragment {
    View view;
    public HomeContactTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabHomeContactBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_home_contact, container, false);
        // Luu binding
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabHomeContactBinding(dataBinding);
        // Khoi tao contact fragment
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initContactFragment();
        if (OnlineManager.getInstance().mCounselorsList != null) {
            // Hien thi danh sach tu van vien
            ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setCounselorsDataListView();
            // Tat thong bao dang doi danh sach tu van vien
            ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).contactProgressOff();
        }
        view = dataBinding.getRoot();
//        view = inflater.inflate(R.layout.tab_home_tour, container, false);
        return view;
    }
}