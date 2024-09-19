package com.zebra.criticalpermissionshelpersample;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(Environment.isExternalStorageManager())
        {

        }
    }
}

