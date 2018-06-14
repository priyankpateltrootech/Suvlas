package com.suvlas;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.facebook.FacebookSdk;

import org.json.JSONObject;

import common.CallingMethod;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_message;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 6/17/2017.
 */

public class ForgatePassActivity extends AppCompatActivity {

    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    EditText edt_forgat_email;
    RelativeLayout rel_submit;
    MCrypt mCrypt;
    VideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgat_password);

        //set videoview for displaying video
        videoview = (VideoView) findViewById(R.id.videoview_login);
        Uri uri = Uri.parse("android.resource://com.suvlas/"+R.raw.suvlassignup);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();

            }
        });

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();

        //initialize component
        init();
    }

    private void findviewID() {
        edt_forgat_email=(EditText)findViewById(R.id.edt_forgat_email);
        rel_submit=(RelativeLayout)findViewById(R.id.rel_submit);

    }
    private void set_listeners() {
        rel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check forgotpassword validation
                 Validation_Email();
            }
        });

    }

    private void Validation_Email() {

        if (edt_forgat_email.getText().toString().trim().equalsIgnoreCase("")) {
            comman_dialog.Show_alert(getResources().getString(R.string.enter_name));
            edt_forgat_email.requestFocus();

        } else {

            //call forgot password api
            if (CommanMethod.isInternetOn(ForgatePassActivity.this)) {

                new ForgatePass().execute(edt_forgat_email.getText().toString());
            } else {
                comman_dialog.Show_alert(Comman_message.Dont_internet);
            }
        }
    }


    private void init() {
        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(ForgatePassActivity.this);
        loader = new Request_loader(ForgatePassActivity.this);
    }


    private class ForgatePass extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //show loader
            loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("email", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("email", params[0]);
                Log.e("Apikey", Comman_url.api_key);

                Log.e("email", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.forgate_pass, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_forgatepass", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_login", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        comman_dialog.Finish_dialog(ForgatePassActivity.this, main_obj.getString("message"));
//                        Intent intent_adv = new Intent(ForgatePassActivity.this, LoginActivity.class);
//                        startActivity(intent_adv);
//                        finish();

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_forgatepass", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}
