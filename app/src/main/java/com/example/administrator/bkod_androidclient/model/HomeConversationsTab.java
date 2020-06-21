package com.example.administrator.bkod_androidclient.model;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.databinding.TabHomeConversationsBinding;

import javax.annotation.Nullable;

import Manager.ActivityManager;

public class HomeConversationsTab extends Fragment {
    View view;
    public HomeConversationsTab() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lien ket layout voi activity
        TabHomeConversationsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.tab_home_conversations, container, false);
        // Luu binding
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTabHomeConversationsBinding(dataBinding);
        // Khoi tao conversations fragment
        ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).initConversationsFragment();
        view = dataBinding.getRoot();
        return view;
    }
}