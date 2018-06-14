package com.suvlas;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.util.Locale;

import common.SharedPrefs;

/**
 * Created by hp on 3/8/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        //change language
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("es"));
        res.updateConfiguration(conf, dm);


        //set videoview for displaying video
        VideoView videoview = (VideoView) findViewById(R.id.videoview);
        Uri uri = Uri.parse("android.resource://com.suvlas/"+R.raw.splashvideo);
        videoview.setVideoURI(uri);

//        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoview.getLayoutParams();
//        params.width =  metrics.widthPixels;
//        params.height = metrics.heightPixels;
//        params.leftMargin = 0;
//        videoview.setLayoutParams(params);

        videoview.start();



        sharedPrefs = new SharedPrefs(SplashActivity.this);

        //hold splash activiy sometimes
        Load_thread();

    }

    private void Load_thread() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //if user already login then goes to main activity otherwise goes to login activity
                if (sharedPrefs.get_User_Name().equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
//                    if (!sharedPrefs.get_Check_activity().equalsIgnoreCase("true")) {
//                        startActivity(new Intent(SplashActivity.this, AdvertiseActivity.class));
//                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                                .putExtra("type_check", "")
//                                .putExtra("user_pic", "")
//                                .putExtra("from_user_id", "")
//                                .putExtra("full_name", ""));
//                    }
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}