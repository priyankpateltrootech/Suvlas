package com.suvlas;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.devs.acr.AutoErrorReporter;

/**
 * Created by hp on 11/10/2017.
 */

public class Suvlas extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*AutoErrorReporter.get(this)
                .setEmailAddresses("vipul.parmar@trootech.com")
                .setEmailSubject("Auto Crash Report")
                .start();*/
    }
}
