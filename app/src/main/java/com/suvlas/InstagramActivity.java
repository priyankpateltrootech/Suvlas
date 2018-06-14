package com.suvlas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import common.CallingMethod;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class InstagramActivity extends AppCompatActivity {

    WebView insta_webview;
    private String mAuthUrl;
    private static final String TAG = "Instagram-WebView";
    Request_loader loader;
    MCrypt mCrypt;
    String device_token,GCM_regId;
    SharedPrefs sharedPrefs;
    private ProgressDialog mSpinner;
    Comman_Dialog comman_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        findviewid();

        init();

        set_listener();
    }

    private void findviewid()
    {
        insta_webview = (WebView)findViewById(R.id.insta_webview);
    }
    private void init()
    {
        loader = new Request_loader(this);
        mCrypt = new MCrypt();
        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);

        mSpinner = new ProgressDialog(this);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        if (CommanMethod.isInternetOn(InstagramActivity.this)) {
            mAuthUrl = Comman_url.INSTAGRAM_AUTH_URL
                    + "?client_id="
                    + Comman_url.INSTAGRAM_CLIENT_ID
                    + "&redirect_uri="
                    + Comman_url.INSTAGRAM_CALLBACK_URL
                    + "&response_type=code&display=touch&scope=likes+comments+relationships";


            Log.e("mAuthurl", mAuthUrl);
        }
        else
        {
            comman_dialog.Show_alert(getResources().getString(R.string.dont_internet));
        }
        device_token = getIntent().getStringExtra("device_token");
        GCM_regId = getIntent().getStringExtra("gcm_id");
    }
    private void set_listener()
    {
        insta_webview.setVerticalScrollBarEnabled(false);
        insta_webview.setHorizontalScrollBarEnabled(false);
        insta_webview.setWebViewClient(new OAuthWebViewClient());
        insta_webview.getSettings().setJavaScriptEnabled(true);
        insta_webview.loadUrl(mAuthUrl);
    }

    private void getAccessToken(final String code) {
        //mProgress.setMessage("Getting access token ...");
        //mProgress.show();

        new InstagramLogin(code).execute();

    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(Comman_url.INSTAGRAM_CALLBACK_URL)) {
                String urls[] = url.split("=");
                getAccessToken(urls[1]);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);

            super.onReceivedError(view, errorCode, description, failingUrl);
            /*mListener.onError(description);
            InstagramDialog.this.dismiss();*/
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);

            super.onPageStarted(view, url, favicon);
            //mSpinner.show();
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = insta_webview.getTitle();
            if (title != null && title.length() > 0) {
                //mTitle.setText(title);
            }
            Log.d(TAG, "onPageFinished URL: " + url);
            mSpinner.dismiss();
        }

    }

    private class InstagramLogin extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        String code;

        public InstagramLogin(String code) {
            this.code = code;
        }

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
                        .add("client_id", Comman_url.INSTAGRAM_CLIENT_ID)
                        .add("client_secret", Comman_url.INSTAGRAM_CLIENT_SECRET)
                        .add("grant_type","authorization_code")
                        .add("redirect_uri",Comman_url.INSTAGRAM_CALLBACK_URL)
                        .add("code",code)
                        .build();

                Responce_jason = CallingMethod.POST_instagram(client, Comman_url.INSTAGRAM_TOKEN_URL, reqbody);


            } catch (Exception e) {
                Log.e("errrrrr_instagram_login", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null)
            {
                Log.e("result_instagram", result + "");

                try {
                    JSONObject instajsonobjtct = new JSONObject(result);

                    JSONObject instadatajsonobject = instajsonobjtct.getJSONObject("user");

                    String instaid = instadatajsonobject.getString("id");

                    String instausername = instadatajsonobject.getString("username");

                    if(instaid != null && instausername != null)
                    {
                        Log.e("instaid",instaid);
                        Log.e("instauser",instausername);

                         new CheckInstagramLogin(instaid,instausername).execute();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.hidepDialog();
                }

            }
            //loader.hidepDialog();
        }
    }

    private class CheckInstagramLogin extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        String instaid,instausername;

        public CheckInstagramLogin(String instaid, String instausername) {
            this.instaid = instaid;
            this.instausername = instausername;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //show loader
            //loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("email", "")
                        .add("social_id", MCrypt.bytesToHex(mCrypt.encrypt(instaid)))
                        .add("Apikey", Comman_url.api_key)
                        .add("os_type","android")
                        .add("device_token",device_token)
                        .add("gcm_key",GCM_regId)
                        .build();


                Responce_jason = CallingMethod.POST(client, Comman_api_name.Check_fb_Login, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_l_instagram", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_Checkfbusers", result + "");

            if (result != null)
            {
                try {
                    JSONObject fbcheckjson = new JSONObject(result);

                    String fbcheckmessage = fbcheckjson.getString("message");
                    Log.e("fbcheckcode",fbcheckmessage);

                    //user successfully login then goes to main activity otherewise goes to register activity
                    if (fbcheckmessage.equalsIgnoreCase("success"))
                    {
                        JSONObject fbuserdata = fbcheckjson.getJSONObject("data");

                        sharedPrefs.save_User_id(fbuserdata.getString("id"));
                        sharedPrefs.save_User_name(fbuserdata.getString("name"));
                        sharedPrefs.save_Gender(fbuserdata.getString("gender"));
                        sharedPrefs.save_Email_id(fbuserdata.getString("email"));

                        Log.e("SharedPrefs_User_Id", fbuserdata.getString("id"));
                        Log.e("SharedPrefs_User_Name", fbuserdata.getString("username"));
                        Log.e("SharedPrefs_Gender", fbuserdata.getString("gender"));
                        Log.e("SharedPrefs_Email", fbuserdata.getString("email"));

                        Intent i = new Intent(InstagramActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else
                    {
                        Intent i = new Intent(InstagramActivity.this,RegisterActivity.class);
                        i.putExtra("screen","withfb");
                        i.putExtra("fbusernamne",instausername);
                        i.putExtra("fbemailid","");
                        i.putExtra("fbgender","");
                        i.putExtra("socialid",instaid);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.hidepDialog();
                }
                //hide loader
                loader.hidepDialog();
            }

        }
    }
}
