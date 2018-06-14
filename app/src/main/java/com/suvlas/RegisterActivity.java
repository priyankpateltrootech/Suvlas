package com.suvlas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import bean.Restaurant_list;
import common.CallingMethod;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 3/3/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LOCATION = 2;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    TextView signin;
    RelativeLayout rel_registration;
    EditText edt_username, edt_email, edt_password, edt_dob, edt_login_gender, edt_confirm_login_password, edt_login_favouritelocation;
    SharedPrefs sharedPrefs;
    Typeface tf;
    int cal_year, cal_month, cal_day;
    Comman_Dialog comman_dialog;
    Dialog gender_dialog;
    CommanMethod commanMethod;
    Request_loader loader;
    MCrypt mCrypt;
    VideoView videoview;
    String gender;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private String valid_email;
    String GCM_regId = "", device_token;
    GoogleCloudMessaging gcm;
    String screenname;
    String fbusername;
    String fbemailid;
    String fbgender;

    String fbsocialid;
    Geocoder geocoder;
    List<Address> addresses;
    Spinner spinner_signup_favouritelocation;
    ArrayList<Restaurant_list> restaurant_name_list;
    String address, city;
    String spinnerselecteditemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);

        //set videoview for displaying video
        videoview = (VideoView) findViewById(R.id.videoview_sign);
        Uri uri = Uri.parse("android.resource://com.suvlas/" + R.raw.appios);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();

            }
        });

        LocationManager lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }


        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Por favor, ubicación habilitada");
            dialog.setPositiveButton("Ir a Configuración", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }


        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
    }

    private void findviewID() {
//        String str_email = edt_email.getText().toString().trim();
        rel_registration = (RelativeLayout) findViewById(R.id.rel_registration);
        signin = (TextView) findViewById(R.id.txt_back_to_signin);
        edt_username = (EditText) findViewById(R.id.edt_login_name);
        edt_username.requestFocus();
        edt_email = (EditText) findViewById(R.id.edt_login_email);
        edt_password = (EditText) findViewById(R.id.edt_login_password);
        edt_dob = (EditText) findViewById(R.id.edt_login_birthdate);
        edt_login_gender = (EditText) findViewById(R.id.edt_login_gender);
        edt_confirm_login_password = (EditText) findViewById(R.id.edt_confirm_login_password);
        //edt_login_favouritelocation = (EditText)findViewById(R.id.edt_login_favouritelocation);
        spinner_signup_favouritelocation = (Spinner) findViewById(R.id.spinner_signup_favouritelocation);
    }

    private void set_listeners() {
        rel_registration.setOnClickListener(this);
        signin.setOnClickListener(this);
        edt_dob.setOnClickListener(this);
        edt_login_gender.setOnClickListener(this);
        edt_username.setOnClickListener(this);
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Is_Valid_Email(edt_email);
            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 8) {
                    edt_password.setError("Insertar contraseña 8 caracteres como mínimo");
                }
                //Log.e("passwordstring", String.valueOf(s));
                //Log.e("passwordlength", String.valueOf(s.length()));
            }
        });

        /*rel_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "CLICKED_REGISTER", Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= 23) {
                    Log.e("IF_Build_Version_1", ">23");
                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        Log.e("IF_PERMISSION_2", "Not_Granted");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION);
                            Log.e("IF_ROTATIONAL_PERMISSION_3", "Request_Permission");
                        } else {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION);
                            Log.e("ELSE_ROTATIONAL_PERMISSION_3", "Request_Permission");
                        }
                    } else {
                        Log.e("ELSE_PERMISSION_2", "GRANTED");
                        // permission has been granted, continue as usual
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        Get_Location();

                        if (screenname.equalsIgnoreCase("withoutfb")) {
                            //check validation without facebook data
                            Check_validation();

                        } else {
                            //check validation with facebook data
                            Checkwithfb_validation();
                        }
                    }
                } else {
                    Log.e("ELSE_Build_Version_1", ">23");
                    //Log.e("login latitude3", "");
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    //Log.e("login latitude", "");
                    Get_Location();

                    if (screenname.equalsIgnoreCase("withoutfb")) {
                        //check validation without facebook data
                        Log.e("IF_withoutfb", " = " + screenname);
                        Check_validation();

                    } else {
                        //check validation with facebook data
                        Log.e("ELSE_withoutfb", " = " + screenname);
                        Checkwithfb_validation();
                    }
                }

            }
        });*/

    }


    //email validation
    public void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Dirección de correo electrónico no válida");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Dirección de correo electrónico no válida");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    private void init() {

        geocoder = new Geocoder(this, Locale.getDefault());

        restaurant_name_list = new ArrayList<>();

        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(RegisterActivity.this);
        loader = new Request_loader(RegisterActivity.this);
        sharedPrefs = new SharedPrefs(RegisterActivity.this);
        edt_dob.setTypeface(tf);
        Calendar date = Calendar.getInstance();
        cal_year = date.get(Calendar.YEAR);
        cal_month = date.get(Calendar.MONTH) + 1;
        cal_day = date.get(Calendar.DAY_OF_MONTH);


        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        //get device token and gcmttoken
        if (CommanMethod.isInternetOn(RegisterActivity.this)) {

            new Device().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new Store_Location().execute();
        } else {
            comman_dialog.Show_alert(getResources().getString(R.string.dont_internet));
        }

        //get screenname by login activity
        screenname = getIntent().getStringExtra("screen");
        //Log.e("screenneme", screenname);

        //either activity start withfb data or without data
        if (screenname.equalsIgnoreCase("withoutfb")) {

        } else {
            edt_password.setVisibility(View.GONE);
            edt_confirm_login_password.setVisibility(View.GONE);
            fbusername = getIntent().getStringExtra("fbusernamne");
            fbemailid = getIntent().getStringExtra("fbemailid");
            fbgender = getIntent().getStringExtra("fbgender");
            fbsocialid = getIntent().getStringExtra("socialid");

            //Log.e("fbusername", fbusername);
            //Log.e("fbemailid", fbemailid);
            //Log.e("fbgender", fbgender);
            //Log.e("fbsocialid", fbsocialid);

            edt_username.setText(fbusername);
            edt_email.setText(fbemailid);
            edt_login_gender.setText(fbgender);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_registration:

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION);
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION);
                        }
                    } else {
                        // permission has been granted, continue as usual
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        Get_Location();

                        if (screenname.equalsIgnoreCase("withoutfb")) {
                            //check validation without facebook data
                            Check_validation();

                        } else {
                            //check validation with facebook data
                            Checkwithfb_validation();
                        }
                    }
                } else {
                    //Log.e("login latitude3", "");
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    //Log.e("login latitude", "");
                    Get_Location();

                    if (screenname.equalsIgnoreCase("withoutfb")) {
                        //check validation without facebook data
                        Check_validation();

                    } else {
                        //check validation with facebook data
                        Checkwithfb_validation();
                    }
                }

                break;
            case R.id.txt_back_to_signin:

                videoview.stopPlayback();
                Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.edt_login_birthdate:
                Select_birth_date();
                break;
            case R.id.edt_login_gender:
                Call_gender_popup();
                break;

        }

    }

    //get user current location
    private void Get_Location() {

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //Log.e("Lat_Long = ", latitude + " , " + longitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                //Log.e("ADDRESS_CITY", address + "" + city);
            } catch (IOException e) {
                //Log.e("IOException_Get_Location", " = " + e);
            }

        } else {
            //Log.e("ELSE_Get_Location", " ELSE_Get_Location ");
        }
    }

    private void Select_gender() {
        gender_dialog = new Dialog(RegisterActivity.this);
        gender_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gender_dialog.setCancelable(false);
        gender_dialog.setContentView(R.layout.select_gender);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(gender_dialog.getWindow().getAttributes());

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;

        lp.width = (display_width * 90) / 100;
//        lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        gender_dialog.getWindow().setAttributes(lp);

        RadioGroup radio_group_time_lock = (RadioGroup) gender_dialog.findViewById(R.id.radio_group_gender);
        RadioButton rb_male = (RadioButton) gender_dialog.findViewById(R.id.rb_male);
        RadioButton rb_female = (RadioButton) gender_dialog.findViewById(R.id.rb_female);

        if (edt_login_gender.getText().toString().equalsIgnoreCase("Male")) {
            rb_male.setChecked(true);
        } else if (edt_login_gender.getText().toString().equalsIgnoreCase("Female")) {
            rb_female.setChecked(true);
        }

        radio_group_time_lock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.rb_male:
                        edt_login_gender.setText(R.string.male);
                        gender = "Male";
                        gender_dialog.dismiss();
                        break;
                    case R.id.rb_female:
                        edt_login_gender.setText(R.string.female);
                        gender = "Female";
                        gender_dialog.dismiss();
                        break;
                }
                //Log.e("gender", String.valueOf(edt_login_gender));
            }
        });


        Button btn_cancel = (Button) gender_dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_dialog.dismiss();
            }
        });

        gender_dialog.show();
    }


    //displ;ay user gender popup
    private void Call_gender_popup() {

        gender_dialog = new Dialog(RegisterActivity.this);
        gender_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gender_dialog.setCancelable(false);
        gender_dialog.setContentView(R.layout.gender_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(gender_dialog.getWindow().getAttributes());

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;

        lp.width = (display_width * 90) / 100;
//        lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        gender_dialog.getWindow().setAttributes(lp);

        RadioGroup radio_group_time_lock = (RadioGroup) gender_dialog.findViewById(R.id.radio_group_gender);
        RadioButton rb_male = (RadioButton) gender_dialog.findViewById(R.id.rb_male);
        RadioButton rb_female = (RadioButton) gender_dialog.findViewById(R.id.rb_female);

        if (edt_login_gender.getText().toString().equalsIgnoreCase(getResources().getString(R.string.male))) {
            rb_male.setChecked(true);
        } else if (edt_login_gender.getText().toString().equalsIgnoreCase(getResources().getString(R.string.female))) {
            rb_female.setChecked(true);
        }

        if (rb_male.isChecked()) {
            edt_login_gender.setText(getResources().getString(R.string.male));
        } else if (rb_female.isChecked()) {
            edt_login_gender.setText(getResources().getString(R.string.female));
        }

        radio_group_time_lock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.rb_male:
                        edt_login_gender.setText(getResources().getString(R.string.male));
                        gender = "Male";
                        gender_dialog.dismiss();
                        break;
                    case R.id.rb_female:
                        edt_login_gender.setText(getResources().getString(R.string.female));
                        gender = "Female";
                        gender_dialog.dismiss();
                        break;
                }
                //Log.e("gender", String.valueOf(edt_login_gender));
            }
        });

        Button btn_cancel = (Button) gender_dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_dialog.dismiss();
            }
        });

        gender_dialog.show();
    }

    //display datapicker
    private void Select_birth_date() {

        final com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog();
        dpd.setOnDateSetListener(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                int month = monthOfYear + 1;

                String Sele_month = "";
                String Sele_day = "";

                if (month < 10) {
                    Sele_month = "0" + month;
                } else {
                    Sele_month = String.valueOf(month);
                }

                if (dayOfMonth < 10) {
                    Sele_day = "0" + dayOfMonth;
                } else {
                    Sele_day = String.valueOf(dayOfMonth);
                }

                String date = Sele_day + "-" + Sele_month + "-" + year;
//                String date = Sele_month + "/" + Sele_day + "/" + year;

                if (year < cal_year) {
                    edt_dob.setText(date);
                    dpd.dismiss();

                } else if (year == cal_year) {
                    if (month < cal_month) {
                        edt_dob.setText(date);
                        dpd.dismiss();
                    } else if (month == cal_month) {
                        if (dayOfMonth < cal_day) {
                            edt_dob.setText(date);
                            dpd.dismiss();
                        } else {
                            comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                        }
                    } else {
                        comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                    }
                } else {
                    comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                }
            }
        });
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    private void Check_validation() {

        boolean passwordmatches = edt_password.getText().toString().trim().matches(edt_confirm_login_password.getText().toString());

        if (spinnerselecteditemid != null) {
            if (edt_username.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_name));
                edt_username.requestFocus();
            } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_email));
                edt_email.requestFocus();
            } else if (edt_password.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_pass));
                edt_password.requestFocus();
            } else if (edt_password.getText().toString().length() < 8) {
                comman_dialog.Show_alert("Insertar contraseña 8 caracteres como mínimo");
                edt_password.requestFocus();
            } else if (edt_confirm_login_password.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_confirm_pass));
                edt_confirm_login_password.requestFocus();
            } else if (passwordmatches != true) {
                comman_dialog.Show_alert(getResources().getString(R.string.match_confirm_pass));
                edt_confirm_login_password.requestFocus();
            } else if (edt_dob.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_date));
            } else if (edt_login_gender.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_gender));
            } else if (spinnerselecteditemid.equalsIgnoreCase("0")) {
                comman_dialog.Show_alert(getResources().getString(R.string.favouritestore_error));
            } else {
                if (CommanMethod.isInternetOn(RegisterActivity.this)) {

                    /*if (latitude != 0.0 && longitude != 0.0) {*/
                        //call user register api
                    //Toast.makeText(getBaseContext(),"Store Found",Toast.LENGTH_SHORT).show();
                        new User_Register(edt_username.getText().toString(), edt_password.getText().toString(),
                                edt_email.getText().toString(), edt_dob.getText().toString(),
                                edt_login_gender.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    /*} else {
                        comman_dialog.Show_alert(getResources().getString(R.string.allow_location));
                    }*/
                } else {
                    comman_dialog.Show_alert(getResources().getString(R.string.dont_internet));
                }
            }
        } else {
            comman_dialog.Show_alert("Por favor inténtalo de nuevo !! Tienda no encontrada ...");
        }

    }

    private void Checkwithfb_validation() {

        if (spinnerselecteditemid != null) {
            if (edt_username.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_name));
                edt_username.requestFocus();
            } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_email));
                edt_email.requestFocus();
            } else if (edt_dob.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_date));
            } else if (edt_login_gender.getText().toString().equalsIgnoreCase("")) {
                comman_dialog.Show_alert(getResources().getString(R.string.enter_gender));
            } else if (spinnerselecteditemid.equalsIgnoreCase("0")) {
                comman_dialog.Show_alert(getResources().getString(R.string.favouritestore_error));
            } else {
                if (CommanMethod.isInternetOn(RegisterActivity.this)) {

                /*if (latitude != 0.0 && longitude != 0.0) {*/
          /*          new User_Register().execute(edt_username.getText().toString(), edt_password.getText().toString(),
                            edt_email.getText().toString(), edt_dob.getText().toString(),
                            gender);*/
                    //Toast.makeText(getBaseContext(),"Store Found with Fb",Toast.LENGTH_SHORT).show();
                    //call login with fb api
                    new Signin_Withfb(edt_username.getText().toString(), edt_email.getText().toString(), edt_dob.getText().toString(), edt_login_gender.getText().toString()).execute();
                    //Log.e("usernameregister", edt_username.getText().toString());
               /* } else {
                    comman_dialog.Show_alert(getResources().getString(R.string.allow_location));
                }*/
                } else {
                    comman_dialog.Show_alert(getResources().getString(R.string.dont_internet));
                }
            }
        }
        else
        {
            comman_dialog.Show_alert("Por favor inténtalo de nuevo !! Tienda no encontrada ...");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        //Log.e("conectado", "conectado");
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.e("OnConnectionFailed", "Error conectado= " + connectionResult);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0) {
                    boolean F_locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (F_locationAccepted) {
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        //Log.e("login latitude", "");
                        //Log.e("onRequestPermissionsResult", "PERMISSION_RESULT");
                        Get_Location();
                        if (screenname.equalsIgnoreCase("withoutfb")) {
                            //check validation without facebook data
                            Check_validation();

                        } else {
                            //check validation with facebook data
                            Checkwithfb_validation();
                        }
                    } else {

                        if (screenname.equalsIgnoreCase("withoutfb")) {
                            //check validation without facebook data
                            Check_validation();

                        } else {
                            //check validation with facebook data
                            Checkwithfb_validation();
                        }
                        /*new MaterialDialog.Builder(this)
                                .title(R.string.location_permission)
                                .positiveText("Ok")
                                .cancelable(false)
                                .positiveColor(Color.parseColor("#E43889"))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Go_to_Setting();
                                    }
                                }).show();*/
                    }
                }
                break;
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
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class Store_Location extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .build();

                //Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Get_store_location, reqbody);

            } catch (Exception e) {
                Log.e("err_store_list", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Log.e("err_store_list2", result + "");

            //Toast.makeText(getBaseContext(),"store location response",Toast.LENGTH_SHORT).show();
            if (result != null) {
                try {
                    JSONObject main_obj = new JSONObject(result);
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Stores");
                        //Log.e("data_array", String.valueOf(data_array));

                        restaurant_name_list.add(0, new Restaurant_list(getResources().getString(R.string.favourite_location), "0"));
                        for (int i = 0; i < data_array.length(); i++) {

                            JSONObject json_data = data_array.getJSONObject(i);

                            String restaurent_name = json_data.getString("name");
                            String restaurant_id = json_data.getString("id");
                            restaurant_name_list.add(new Restaurant_list(restaurent_name, restaurant_id));
                        }

                        //Log.e("size", String.valueOf(restaurant_name_list.size()));

                        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(RegisterActivity.this, restaurant_name_list);
                        spinner_signup_favouritelocation.setAdapter(customSpinnerAdapter);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loader.hidepDialog();
            }
        }
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
                gcm = GoogleCloudMessaging.getInstance(RegisterActivity.this);
            }
            InstanceID instanceID = InstanceID.getInstance(RegisterActivity.this);
            try {
                GCM_regId = instanceID.getToken(Comman_url.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                //Log.e("IOException", e.toString());
                //Toast.makeText(getBaseContext(),"device token do in exception",Toast.LENGTH_SHORT).show();
            }
            //Log.e("GCM_Key == ", GCM_regId);
            // Persist the regID - no need to register again.
            return GCM_regId;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.e("Responce_device", result);
            device_token = android.provider.Settings.Secure.getString(RegisterActivity.this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID); //get android device id
            //Log.e("device_token", "" + device_token);
            //Toast.makeText(getBaseContext(),"device token response",Toast.LENGTH_SHORT).show();
            if (!result.equalsIgnoreCase("")) {
                try {
                } catch (Exception e) {
                    //Toast.makeText(getBaseContext(),"device token on post exception",Toast.LENGTH_SHORT).show();
                    //Log.e("Error_device", e.toString());
                }
            }
//            loader.hidepDialog();
        }
    }

    private class User_Register extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String username,password,email,dob,gender;

        public User_Register(String username, String password, String email, String dob, String gender) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.dob = dob;
            this.gender = gender;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                multipartBuilder.setType(MultipartBody.FORM);

                OkHttpClient client = new OkHttpClient();

                multipartBuilder.addFormDataPart("Apikey", Comman_url.api_key);
                multipartBuilder.addFormDataPart("os_type", "android");

                if (username.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("name", "");
                    multipartBuilder.addFormDataPart("username", "");
                }
                else
                {
                    String name_escape = StringEscapeUtils.escapeHtml4(username);
                    multipartBuilder.addFormDataPart("name", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)));
                    multipartBuilder.addFormDataPart("username", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)));
                    Log.e("name",MCrypt.bytesToHex(mCrypt.encrypt(name_escape)));
                    Log.e("username",MCrypt.bytesToHex(mCrypt.encrypt(name_escape)));
                }

                if (email.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("email", "");
                }
                else
                {
                    String email_escape = StringEscapeUtils.escapeHtml4(email);
                    multipartBuilder.addFormDataPart("email", MCrypt.bytesToHex(mCrypt.encrypt(email_escape)));
                    Log.e("email",MCrypt.bytesToHex(mCrypt.encrypt(email_escape)));
                }

                if (password.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("password", "");
                }
                else
                {
                    String password_escape = StringEscapeUtils.escapeHtml4(password);
                    multipartBuilder.addFormDataPart("password", MCrypt.bytesToHex(mCrypt.encrypt(password_escape)));
                    Log.e("password",MCrypt.bytesToHex(mCrypt.encrypt(password_escape)));
                }

                if (dob.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("dateofbirth", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("dateofbirth", MCrypt.bytesToHex(mCrypt.encrypt(dob)));
                    Log.e("dateofbirth",MCrypt.bytesToHex(mCrypt.encrypt(dob)));
                }

                if (gender.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("gender", "");
                }
                else
                {
                    String gender_escape = StringEscapeUtils.escapeHtml4(gender);
                    multipartBuilder.addFormDataPart("gender", MCrypt.bytesToHex(mCrypt.encrypt(gender_escape)));
                    Log.e("gender",MCrypt.bytesToHex(mCrypt.encrypt(gender_escape)));
                }

                if (latitude == 0.0 && longitude == 0.0)
                {
                    multipartBuilder.addFormDataPart("latitude", "");
                    multipartBuilder.addFormDataPart("longitude", "");
                    multipartBuilder.addFormDataPart("location", "");
                }
                else
                {
                    String location_escape = StringEscapeUtils.escapeHtml4(city);
                    multipartBuilder.addFormDataPart("latitude", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(latitude))));
                    multipartBuilder.addFormDataPart("longitude", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(longitude))));
                    multipartBuilder.addFormDataPart("location", MCrypt.bytesToHex(mCrypt.encrypt(location_escape)));
                    Log.e("latitude",MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(latitude))));
                    Log.e("longitude",MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(longitude))));
                    Log.e("location",MCrypt.bytesToHex(mCrypt.encrypt(location_escape)));
                }

                if (device_token.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("device_token", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("device_token", MCrypt.bytesToHex(mCrypt.encrypt(device_token)));
                    Log.e("device_token",MCrypt.bytesToHex(mCrypt.encrypt(device_token)));
                }

                if (GCM_regId.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("gcm_key", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("gcm_key", MCrypt.bytesToHex(mCrypt.encrypt(GCM_regId)));
                    Log.e("gcm_key",MCrypt.bytesToHex(mCrypt.encrypt(GCM_regId)));
                }

                if (spinnerselecteditemid.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("favorite_location", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)));
                    Log.e("favorite_location",MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)));
                }

                Log.e("Apikey",Comman_url.api_key);

                RequestBody requestBody = multipartBuilder.build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Register_user, requestBody);
               /* if (latitude != 0.0 && longitude != 0.0) {

                    RequestBody reqbody = new FormBody.Builder()
                            .add("name", MCrypt.bytesToHex(mCrypt.encrypt(username)))
                            .add("username", MCrypt.bytesToHex(mCrypt.encrypt(username)))
                            .add("password", MCrypt.bytesToHex(mCrypt.encrypt(password)))
                            .add("email", MCrypt.bytesToHex(mCrypt.encrypt(email)))
                            .add("dateofbirth", MCrypt.bytesToHex(mCrypt.encrypt(dob)))
                            .add("gender", MCrypt.bytesToHex(mCrypt.encrypt(gender)))
                            .add("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude+"")))
                            .add("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude+"")))
                            .add("Apikey", Comman_url.api_key)
                            .add("os_type", "android")
                            .add("device_token", device_token)
                            .add("gcm_key", GCM_regId)
                            .add("location", MCrypt.bytesToHex(mCrypt.encrypt(city)))
                            .add("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)))
                            .build();

                    Responce_jason = CallingMethod.POST(client, Comman_api_name.Register_user, reqbody);
                }
                else
                {
                    RequestBody reqbody = new FormBody.Builder()
                            .add("name", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                            .add("username", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                            .add("password", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                            .add("email", MCrypt.bytesToHex(mCrypt.encrypt(params[2])))
                            .add("dateofbirth", MCrypt.bytesToHex(mCrypt.encrypt(params[3])))
                            .add("gender", MCrypt.bytesToHex(mCrypt.encrypt(params[4])))
                            .add("latitude", "")
                            .add("longitude", "")
                            .add("Apikey", Comman_url.api_key)
                            .add("os_type", "android")
                            .add("device_token", device_token)
                            .add("gcm_key", GCM_regId)
                            .add("location", "")
                            .add("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)))
                            .build();

                    Responce_jason = CallingMethod.POST(client, Comman_api_name.Register_user, reqbody);
                }*/

                /*Log.e("name", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                Log.e("username", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                Log.e("password", MCrypt.bytesToHex(mCrypt.encrypt(params[1])));
                Log.e("email", MCrypt.bytesToHex(mCrypt.encrypt(params[2])));
                Log.e("dateofbirth", MCrypt.bytesToHex(mCrypt.encrypt(params[3])));
                Log.e("gender", MCrypt.bytesToHex(mCrypt.encrypt(params[4])));
                Log.e("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude + "")));
                Log.e("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude + "")));*/
                /*Log.e("Apikey", Comman_url.api_key);

                Log.e("username", params[0]);
                Log.e("password", params[1]);
                Log.e("email", params[2]);
                Log.e("dateofbirth", params[3]);
                Log.e("gender", params[4]);
                Log.e("latitude", latitude + "");
                Log.e("longitude", longitude + "");
                Log.e("Apikey", Comman_url.api_key);*/

            } catch (Exception e) {
                Log.e("errrrrr_register", e.toString());
                Responce_jason=e.toString();
                try {
                    File myFile = new File("/sdcard/log_suvlas.txt");
                    if (myFile.exists()) {
                        myFile.delete();
                    }
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(e.toString());
                    myOutWriter.close();
                    fOut.close();

                } catch (Exception e1) {
                    Log.e("Exception===", e + "");
                }

            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Toast.makeText(getBaseContext(),""+result,Toast.LENGTH_SHORT).show();
            //Log.e("result_register", result + "");

            //Toast.makeText(getBaseContext(),"get Register Response",Toast.LENGTH_SHORT).show();
            //user successfully register then goes to main activity otherwise show error dialog

            Log.e("resultsigmup",result);
            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONObject data_obj = main_obj.getJSONObject("data");

                        sharedPrefs.save_User_id(data_obj.getString("id"));
                        sharedPrefs.save_User_name(data_obj.getString("username"));
                        sharedPrefs.save_Gender(data_obj.getString("gender"));
                        sharedPrefs.save_DOB(data_obj.getString("date_of_birth"));
                        sharedPrefs.save_Email_id(data_obj.getString("email"));

                        Log.e("SharedPrefs_User_Id", data_obj.getString("id"));
                        Log.e("SharedPrefs_User_Name", data_obj.getString("username"));
                        Log.e("SharedPrefs_Gender", data_obj.getString("gender"));
                        Log.e("SharedPrefs_DOB", data_obj.getString("date_of_birth"));
                        Log.e("SharedPrefs_Email", data_obj.getString("email"));

                        Intent intent_adv = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent_adv);
                        finish();

//                    if (main_obj.getString("code").equalsIgnoreCase("200")) {
//
//                        new MaterialDialog.Builder(RegisterActivity.this)
//                                //.title("Thank you for registering with us.")
//                                .title(main_obj.getString("message"))
//                                .positiveText("Ok")
//                                .cancelable(false)
//                                .positiveColor(Color.parseColor("#E43889"))
//                                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                        dialog.cancel();
//                                        Intent intent_adv = new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(intent_adv);
//                                        finish();
//                                    }
//                                })
//                                .show();
                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                //Log.e("exception_register", e.toString());
                //Toast.makeText(getBaseContext(),"register exception onpost",Toast.LENGTH_SHORT).show();
                loader.hidepDialog();
            }
            loader.hidepDialog();
        }
    }

    private class Signin_Withfb extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        String username,email,dob,gender;

        public Signin_Withfb(String username, String email, String dob, String gender) {

            this.username = username;
            this.email = email;
            this.dob = dob;
            this.gender = gender;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                multipartBuilder.setType(MultipartBody.FORM);

                OkHttpClient client = new OkHttpClient();

                multipartBuilder.addFormDataPart("Apikey", Comman_url.api_key);
                multipartBuilder.addFormDataPart("os_type", "android");

                if (username.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("name", "");
                }
                else
                {
                    String name_escape = StringEscapeUtils.escapeHtml4(username);
                    multipartBuilder.addFormDataPart("name", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)));
                }

                if (email.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("email", "");
                }
                else
                {
                    String email_escape = StringEscapeUtils.escapeHtml4(email);
                    multipartBuilder.addFormDataPart("email", MCrypt.bytesToHex(mCrypt.encrypt(email_escape)));
                }

                if (dob.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("date_of_birth", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(dob)));
                }

                if (gender.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("gender", "");
                }
                else
                {
                    String gender_escape = StringEscapeUtils.escapeHtml4(gender);
                    multipartBuilder.addFormDataPart("gender", MCrypt.bytesToHex(mCrypt.encrypt(gender_escape)));
                }

                if (latitude == 0.0 && longitude == 0.0)
                {
                    multipartBuilder.addFormDataPart("latitude", "");
                    multipartBuilder.addFormDataPart("longitude", "");
                    multipartBuilder.addFormDataPart("location", "");
                }
                else
                {
                    String location_escape = StringEscapeUtils.escapeHtml4(city);
                    multipartBuilder.addFormDataPart("latitude", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(latitude))));
                    multipartBuilder.addFormDataPart("longitude", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(longitude))));
                    multipartBuilder.addFormDataPart("location", MCrypt.bytesToHex(mCrypt.encrypt(location_escape)));
                }

                if (fbsocialid.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("social_id", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("social_id", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(fbsocialid))));
                }

                if (device_token.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("device_token", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("device_token", MCrypt.bytesToHex(mCrypt.encrypt(device_token)));
                }

                if (GCM_regId.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("gcm_key", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("gcm_key", MCrypt.bytesToHex(mCrypt.encrypt(GCM_regId)));
                }

                if (spinnerselecteditemid.equalsIgnoreCase(""))
                {
                    multipartBuilder.addFormDataPart("favorite_location", "");
                }
                else
                {
                    multipartBuilder.addFormDataPart("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)));
                }

                RequestBody requestBody = multipartBuilder.build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Signin_withfb, requestBody);

                /*if (latitude != 0.0 && longitude != 0.0) {

                    RequestBody reqbody = new FormBody.Builder()
                            .add("name", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                            .add("email", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                            .add("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(params[2])))
                            .add("gender", MCrypt.bytesToHex(mCrypt.encrypt(params[3])))
                            .add("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude + "")))
                            .add("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude + "")))
                            .add("social_id", MCrypt.bytesToHex(mCrypt.encrypt(fbsocialid)))
                            .add("Apikey", Comman_url.api_key)
                            .add("os_type", "android")
                            .add("device_token", device_token)
                            .add("gcm_key", GCM_regId)
                            .add("location", MCrypt.bytesToHex(mCrypt.encrypt(city)))
                            .add("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)))
                            .build();

                    Responce_jason = CallingMethod.POST(client, Comman_api_name.Signin_withfb, reqbody);
                }
                else
                {
                    RequestBody reqbody = new FormBody.Builder()
                            .add("name", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                            .add("email", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                            .add("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(params[2])))
                            .add("gender", MCrypt.bytesToHex(mCrypt.encrypt(params[3])))
                            .add("latitude", "")
                            .add("longitude", "")
                            .add("social_id", MCrypt.bytesToHex(mCrypt.encrypt(fbsocialid)))
                            .add("Apikey", Comman_url.api_key)
                            .add("os_type", "android")
                            .add("device_token", device_token)
                            .add("gcm_key", GCM_regId)
                            .add("location", "")
                            .add("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselecteditemid)))
                            .build();

                    Responce_jason = CallingMethod.POST(client, Comman_api_name.Signin_withfb, reqbody);
                }*/


                /*Log.e("name", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                Log.e("email", MCrypt.bytesToHex(mCrypt.encrypt(params[1])));
                Log.e("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(params[2])));
                Log.e("gender", MCrypt.bytesToHex(mCrypt.encrypt(params[3])));
                Log.e("social_id", MCrypt.bytesToHex(mCrypt.encrypt(fbsocialid)));
                Log.e("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude + "")));
                Log.e("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude + "")));
                Log.e("Apikey", Comman_url.api_key);

                Log.e("name", params[0]);
                Log.e("email", params[1]);
                Log.e("gender", params[2]);
                Log.e("social_id", params[3]);
//                Log.e("latitude", latitude + "");
//                Log.e("longitude", longitude + "");
                Log.e("Apikey", Comman_url.api_key);*/


            } catch (Exception e) {
                //Log.e("errrrrr_login", e.toString());
                //Toast.makeText(getBaseContext(),"register exception with fb do in background",Toast.LENGTH_SHORT).show();
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //Log.e("result_login", result + "");
            //Toast.makeText(getBaseContext(),"register response with fb",Toast.LENGTH_SHORT).show();
            //user successfully register then goes to main activity otherwise show error dialog
            if (result != null) {
                try {

                    JSONObject registerwithfbjson = new JSONObject(result);

                    if (registerwithfbjson.getString("code").equalsIgnoreCase("200")) {

                        JSONObject registerwithfbjsondata = registerwithfbjson.getJSONObject("data");

                        sharedPrefs.save_User_id(registerwithfbjsondata.getString("id"));
                        sharedPrefs.save_User_name(registerwithfbjsondata.getString("username"));
                        sharedPrefs.save_Gender(registerwithfbjsondata.getString("gender"));
                        sharedPrefs.save_DOB(registerwithfbjsondata.getString("date_of_birth"));
                        sharedPrefs.save_Email_id(registerwithfbjsondata.getString("email"));

                        Log.e("SharedPrefs_User_Id", registerwithfbjsondata.getString("id"));
                        Log.e("SharedPrefs_User_Name", registerwithfbjsondata.getString("username"));
                        Log.e("SharedPrefs_Gender", registerwithfbjsondata.getString("gender"));
                        Log.e("SharedPrefs_DOB", registerwithfbjsondata.getString("date_of_birth"));
                        Log.e("SharedPrefs_Email", registerwithfbjsondata.getString("email"));

                        Intent intent_adv = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent_adv);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getBaseContext(),"register exception with fb onpost",Toast.LENGTH_SHORT).show();
                    loader.hidepDialog();
                }


            }
            loader.hidepDialog();

        }
    }

    public class CustomSpinnerAdapter extends BaseAdapter {

        Context context;
        ArrayList<Restaurant_list> restaurant_lists;

        public CustomSpinnerAdapter(Context context, ArrayList<Restaurant_list> restaurant_lists) {
            this.context = context;
            this.restaurant_lists = restaurant_lists;
        }

        @Override
        public int getCount() {
            return restaurant_lists.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.register_spinner_item, parent, false);

            TextView store_name = (TextView) itemView.findViewById(R.id.store_name);
            store_name.setText(restaurant_lists.get(position).getName());

            spinner_signup_favouritelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    spinnerselecteditemid = restaurant_lists.get(position).getId();
                    Log.e("spinnserid", restaurant_lists.get(position).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return itemView;
        }
    }

}
