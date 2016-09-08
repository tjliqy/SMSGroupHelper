package com.example.tjliqy.smsgrouphelper;

import android.app.Application;
import android.content.Context;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class SMSGroupHelperApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext(){
        return context;
    }
}
