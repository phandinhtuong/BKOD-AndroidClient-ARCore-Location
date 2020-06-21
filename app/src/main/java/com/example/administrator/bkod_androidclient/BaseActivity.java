package com.example.administrator.bkod_androidclient;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import Manager.ActivityManager;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gan activity hien tai
        ActivityManager.getInstance().setCurrentActivity(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Gan activity hien tai
        ActivityManager.getInstance().setCurrentActivity(this);
    }
}
