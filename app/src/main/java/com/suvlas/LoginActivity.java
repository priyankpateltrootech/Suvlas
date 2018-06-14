package com.suvlas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

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
 * Created by hp on 3/3/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    RelativeLayout rel_login;
    EditText edt_login_email, edt_login_password, edt_password, edt_dob;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    CallbackManager callbackManager;
    String str_username, str_password, str_email, str_dob, str_gender, username, emaild_social, firstname, lastname, social_id, gender;
    MCrypt mCrypt;
    TextView txt_signup, txt_forgate_pass;
    ImageView img_signin_fb;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_LOCATION = 2;
    private Location mLastLocation;
    VideoView videoview;
    LinearLayout linear_facebook,linear_instagram;

    String GCM_regId = "",device_token;
    GoogleCloudMessaging gcm;
    private String mAuthUrl;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //change language
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("es"));
        res.updateConfiguration(conf, dm);

        setContentView(R.layout.login);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();





    }

    private void init() {

        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(LoginActivity.this);
        loader = new Request_loader(LoginActivity.this);
        sharedPrefs = new SharedPrefs(LoginActivity.this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        if (CommanMethod.isInternetOn(LoginActivity.this)) {

            //get device token and gcmttoken
            new Device().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        //get KeyHash for facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.suvlas",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        //set videoview for displaying video
        videoview = (VideoView) findViewById(R.id.videoview_login);
        final Uri uri = Uri.parse("android.resource://com.suvlas/" + R.raw.appios);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();

            }
        });

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                videoview.setVideoURI(uri);
                videoview.requestFocus();
                videoview.start();
                return true;
            }
        });
        //get KeyHash for facebook
        get_key_hash();

        //setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes"));


        //facebook integration
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //boolean isPermissionAvailable = false;
                //LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList("user_birthday","email"));
                if (loginResult.getAccessToken() != null) {
                    Log.e("TOKEN =", loginResult.getAccessToken() + "");
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    if (response.getError() != null) {
                                        // handle error
                                        Log.e("TAG", "facebook response failed error");
                                    } else {

                                        Bundle bFacebookData = getFacebookData(me);
                                        //Log.e("TAG", "facebook login " + me);
                                        //Log.e("TAG", "facebook response " + bFacebookData);
                                        social_id = me.optString("id");

                                        emaild_social = bFacebookData.getString("email");
                                        firstname = bFacebookData.getString("first_name");
                                        lastname = bFacebookData.getString("last_name");
                                        gender = bFacebookData.getString("gender");

                                        Log.e("emaild_social", emaild_social);
                                        Log.e("firstname", firstname);
                                        Log.e("lastname", lastname);
                                        //Log.e("gender", gender);

                                        username = firstname;

                                        //Log.e("PERMISSIONS", AccessToken.getCurrentAccessToken().getPermissions().toString());
                                        // send email and id to your web server
                                        //check internet connection availability
                                        if (CommanMethod.isInternetOn(LoginActivity.this)) {

                                           /* if (latitude != 0.0 && longitude != 0.0) {*/
                                            //new Signin_Withfb().execute(username, emaild_social, gender, social_id);
                                            //call checkfblogin api
                                            new CheckFbLogin().execute(emaild_social,social_id,username,gender);
                                            /*} else {
                                                comman_dialog.Show_alert(getResources().getString(R.string.allow_location));
                                            }*/

                                        } else {
                                            comman_dialog.Show_alert(getResources().getString(R.string.dont_internet));
                                        }

                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                /*if (!isPermissionAvailable)
                    getPermissionFromFacebook();*/


                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email","user_birthday"));
            }

            @Override
            public void onCancel() {
                Log.e("TAG", "facebook login failed error");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("TAG", "facebook login failed error_error123" + e.toString());
            }
        });


    }

    private void getPermissionFromFacebook() {
        String[] permissions = { "basic_info", "user_friends", "email","user_birthday" };
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays
                .asList(permissions));
    }

    private class Device extends AsyncTask<String, Void, String> {

        String Responce_json = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
            }
            InstanceID instanceID = InstanceID.getInstance(LoginActivity.this);
            try {
                GCM_regId = instanceID.getToken(Comman_url.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
            Log.e("GCM_Key == ", GCM_regId);
            // Persist the regID - no need to register again.
            return GCM_regId;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.e("Responce_device", result);
            device_token = android.provider.Settings.Secure.getString(LoginActivity.this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID); //get android device id
            Log.e("device_token", "" + device_token);
            if (!result.equalsIgnoreCase("")) {
                try {
                } catch (Exception e) {
                    Log.e("Error_device", e.toString());
                }
            }
//            loader.hidepDialog();
        }
    }

    //get facebook data
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.e("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e + "");
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void get_key_hash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.suvlas", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void findviewID() {
        rel_login = (RelativeLayout) findViewById(R.id.rel_login);
        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        txt_forgate_pass = (TextView) findViewById(R.id.txt_forgate_pass);
        img_signin_fb = (ImageView) findViewById(R.id.img_signin_fb);
        linear_facebook = (LinearLayout)findViewById(R.id.linear_facebook);
        linear_instagram = (LinearLayout)findViewById(R.id.linear_instagram);

    }

    private void set_listeners() {
        rel_login.setOnClickListener(this);
        txt_signup.setOnClickListener(this);
        //img_signin_fb.setOnClickListener(this);
        txt_forgate_pass.setOnClickListener(this);
        linear_facebook.setOnClickListener(this);
        linear_instagram.setOnClickListener(this);

    }

    private void login_validation() {

        if (edt_login_email.getText().toString().trim().equalsIgnoreCase("")) {
            comman_dialog.Show_alert(getResources().getString(R.string.enter_name));
            edt_login_email.requestFocus();
        } else if (edt_login_password.getText().toString().trim().equalsIgnoreCase("")) {
            comman_dialog.Show_alert(getResources().getString(R.string.enter_pass));
            edt_login_password.requestFocus();
        } else {
            // call login api
            if (CommanMethod.isInternetOn(LoginActivity.this)) {
                new User_Login().execute(edt_login_email.getText().toString().replace(" ", ""),
                        edt_login_password.getText().toString().replace(" ", ""));


            } else {
                comman_dialog.Show_alert(Comman_message.Dont_internet);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("conectado", "conectado");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("OnConnectionFailed", "Error conectado= " + connectionResult);
    }


    private class User_Login extends AsyncTask<String, Void, String> {

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

                String email_escape = StringEscapeUtils.escapeHtml4(params[0]);
                String password_escape = StringEscapeUtils.escapeHtml4(params[1]);

                RequestBody reqbody = new FormBody.Builder()
                        .add("email", MCrypt.bytesToHex(mCrypt.encrypt(email_escape)))
                        .add("password", MCrypt.bytesToHex(mCrypt.encrypt(password_escape)))
                        .add("Apikey", Comman_url.api_key)
                        .add("os_type","android")
                        .add("device_token",device_token)
                        .add("gcm_key",GCM_regId)
                        .build();

                //Log.e("email", params[0]);
                //Log.e("Apikey", Comman_url.api_key);

                //Log.e("email", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                //Log.e("password", MCrypt.bytesToHex(mCrypt.encrypt(params[1])));
                //Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Login_user, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_login", e.toString());
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

                        JSONObject data_obj = main_obj.getJSONObject("data");

                        //store data in shared preferences
                        sharedPrefs.save_User_id(data_obj.getString("id"));
                        sharedPrefs.save_User_name(data_obj.getString("username"));
                        sharedPrefs.save_Gender(data_obj.getString("gender"));
                        sharedPrefs.save_DOB(data_obj.getString("date_of_birth"));
                        sharedPrefs.save_Email_id(data_obj.getString("email"));
                        sharedPrefs.save_Barcode_id(data_obj.getString("barcode"));

                        Log.e("SharedPrefs_User_Id", data_obj.getString("id"));
                        Log.e("SharedPrefs_User_Name", data_obj.getString("username"));
                        Log.e("SharedPrefs_Gender", data_obj.getString("gender"));
                        Log.e("SharedPrefs_DOB", data_obj.getString("date_of_birth"));
                        Log.e("SharedPrefs_Email", data_obj.getString("email"));
                        Log.e("SharedPrefs_Barcode", data_obj.getString("barcode"));

                        // user successfully login then go to main activity
                        Intent intent_adv = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent_adv);
                        finish();

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_login", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_login:

                //check login validation
                login_validation();

                break;
            case R.id.txt_signup:

                // user goes to register activity
                videoview.stopPlayback();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("screen","withoutfb");
                startActivity(intent);
                break;

            case R.id.txt_forgate_pass:
                //user goes to forgot password activity
                Intent intent2 = new Intent(LoginActivity.this, ForgatePassActivity.class);
                startActivity(intent2);

                break;
            case R.id.linear_facebook:
                if (CommanMethod.isInternetOn(LoginActivity.this)) {
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        } else {

                            // permission has been granted, continue as usual
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            Log.e("login latitude", "");
                            Get_Location();
                            LoginManager.getInstance().logOut();
                            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_birthday", "email"));
//                        Check_validation();
                        }
                    }else {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        Log.e("login latitude", "");
                        Get_Location();
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_birthday", "email"));
                    }

                } else {
                    comman_dialog.Show_alert(Comman_message.Dont_internet);
                }
                break;
            case R.id.linear_instagram:
                //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                /*mDialog.show();*/
                Intent i = new Intent(LoginActivity.this,InstagramActivity.class);
                i.putExtra("device_token",device_token);
                i.putExtra("gcm_id",GCM_regId);
                startActivity(i);
        }
    }

    //get user currenbt location
    private void Get_Location() {

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.e("Lat_Long = ", latitude + " , " + longitude);

        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0) {

                    boolean F_locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (F_locationAccepted) {
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        Log.e("login latitude", "");
                        Get_Location();
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
//                        slideUp.show();
//                        Get_Location();
                    } else {
                        new MaterialDialog.Builder(this)
                                .title(R.string.location_permission)
                                .positiveText("Ok")
                                .cancelable(false)
                                .positiveColor(Color.parseColor("#E43889"))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Go_to_Setting();
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    private class CheckFbLogin extends AsyncTask<String, Void, String> {

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
                        .add("social_id", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                        .add("Apikey", Comman_url.api_key)
                        .add("os_type","android")
                        .add("device_token",device_token)
                        .add("gcm_key",GCM_regId)
                        .build();

                Log.e("emailcheckfb", params[0]);
                Log.e("Apikey", Comman_url.api_key);
                Log.e("emailidcheckfgb", params[1]);
                Log.e("devicetokenfb",device_token);
                Log.e("gcmkeyfb",GCM_regId);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Check_fb_Login, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_login", e.toString());
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

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else
                    {
                        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                        i.putExtra("screen","withfb");
                        i.putExtra("fbusernamne",username);
                        i.putExtra("fbemailid",emaild_social);
                        i.putExtra("fbgender",gender);
                        i.putExtra("socialid",social_id);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hide loader
                loader.hidepDialog();
            }

        }
    }

    private void Go_to_Setting() {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void instagramlogin()
    {
        mAuthUrl = Comman_url.INSTAGRAM_AUTH_URL
                + "?client_id="
                + Comman_url.INSTAGRAM_CLIENT_ID
                + "&redirect_uri="
                + Comman_url.INSTAGRAM_CALLBACK_URL
                + "&response_type=code&display=touch&scope=likes+comments+relationships";


        Log.e("mAuthurl",mAuthUrl);

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
                Log.e("instagram_code",code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(this, mAuthUrl, listener);
        mProgress = new ProgressDialog(this);
        mProgress.setCancelable(false);
    }


    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new InstagramLogin(code).execute();

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
            //loader.showpDialog();
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
                }
                mProgress.dismiss();
            }


            mProgress.dismiss();
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
            loader.showpDialog();
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

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else
                    {
                        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                        i.putExtra("screen","withfb");
                        i.putExtra("fbusernamne",instausername);
                        i.putExtra("fbemailid","");
                        i.putExtra("fbgender","");
                        i.putExtra("socialid",instaid);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hide loader
                loader.hidepDialog();
            }

        }
    }
}