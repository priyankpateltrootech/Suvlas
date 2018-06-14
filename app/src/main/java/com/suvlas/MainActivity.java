package com.suvlas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

import adpter.MainOrderListAdapter;
import bean.MainOrderlistItem;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int REQUEST_LOCATION = 2;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public SwipeRefreshLayout swipe_refresh_layout;
    TextView txt_history, txt_card, txt_balance, txt_stores, txt_gift, txt_order, txt_msg_user, number;
    TextView txt_setting;
    RelativeLayout relative_subtop, relative_top1;
    ImageView img_back, img_msg;
    SharedPrefs sharedPrefs;
    RecyclerView recycler_view_main;
    GridLayoutManager lm;
    MainOrderListAdapter mainOrderListAdapter;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    ArrayList<MainOrderlistItem> order_list = new ArrayList<>();
    MCrypt mCrypt;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int page_count = 0;
    boolean flag_scroll = false;
    String timezoneID;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 0;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    String first_name, menu_name, loyality_status = "", total_rewards_points = "", status_update_date;
    String offer_name,offer_idescription,offer_image,offers_types,offer_code,generated_QR_Code,to_date,product_name,category_name;
    RelativeLayout nodata;
    TextView notxtdata;
    LinearLayout linear_balance,linear_stores,linear_gift,linear_order,linear_card,linear_history,linear_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();

    }

    private void findviewID() {

        linear_balance = (LinearLayout)findViewById(R.id.linear_balance);
        linear_stores = (LinearLayout)findViewById(R.id.linear_stores);
        linear_gift = (LinearLayout)findViewById(R.id.linear_gift);
        linear_order = (LinearLayout)findViewById(R.id.linear_order);
        linear_card = (LinearLayout)findViewById(R.id.linear_card);
        linear_history = (LinearLayout)findViewById(R.id.linear_history);
        linear_setting = (LinearLayout)findViewById(R.id.linear_setting);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_msg = (ImageView) findViewById(R.id.img_header_msg);
        number = (TextView) findViewById(R.id.number);
        txt_msg_user = (TextView) findViewById(R.id.txt_msg_user);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        relative_subtop = (RelativeLayout) findViewById(R.id.relative_subtop);
        relative_top1 = (RelativeLayout) findViewById(R.id.relative_top1);
        recycler_view_main = (RecyclerView) findViewById(R.id.recycler_view_main);
        lm = new GridLayoutManager(MainActivity.this, 1);
        recycler_view_main.setLayoutManager(lm);
        nodata = (RelativeLayout)findViewById(R.id.nodata);
        notxtdata = (TextView)findViewById(R.id.notxtdata);


    }

    private void set_listeners() {

        img_msg.setOnClickListener(this);
        relative_subtop.setOnClickListener(this);
        relative_top1.setOnClickListener(this);
        linear_balance.setOnClickListener(this);
        linear_stores.setOnClickListener(this);
        linear_gift.setOnClickListener(this);
        linear_order.setOnClickListener(this);
        linear_card.setOnClickListener(this);
        linear_history.setOnClickListener(this);
        linear_setting.setOnClickListener(this);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //pagination
                Load_offer_list();
            }
        });

        //pagination
        Scroll_activity_list();
    }

    private void Load_offer_list() {
        if (CommanMethod.isInternetOn(MainActivity.this)) {

            /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else
            {

            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/
            Get_Location();
            previousTotal = 0;
            totalItemCount = 0;
            page_count=0;
            flag_scroll = false;
            order_list = new ArrayList<>();

            //call offer list api
            new Main_offer_list("refresh").execute();
        } else {
            //swipe_refresh_layout.setRefreshing(false);
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        //recyclerview bind with mainnorderlist adapter
        mainOrderListAdapter = new MainOrderListAdapter(MainActivity.this, order_list);
        recycler_view_main.setAdapter(mainOrderListAdapter);
        mainOrderListAdapter.notifyDataSetChanged();
        Scroll_activity_list();
    }

    private void init() {
        mCrypt = new MCrypt();
        loader = new Request_loader(MainActivity.this);
        sharedPrefs = new SharedPrefs(MainActivity.this);
        txt_msg_user.setText(getResources().getString(R.string.welcome) +" " + sharedPrefs.get_User_Name());
        comman_dialog = new Comman_Dialog(MainActivity.this);
        timezoneID = TimeZone.getDefault().getID();
        swipe_refresh_layout.setColorSchemeResources(R.color.loginbtn);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        if (CommanMethod.isInternetOn(this)) {
            //call userstatus api
            new User_Status().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        Log.e("timezone",timezoneID);
    }

    private class User_Status extends AsyncTask<String, Void, String> {

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
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("userID", sharedPrefs.get_User_id());


                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.My_card_userprofiledetail, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_settingpaged", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_settingpage", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);
                    Log.e("mycard", String.valueOf(main_obj));
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {
                        JSONObject data_obj = main_obj.getJSONObject("data");

                        total_rewards_points = data_obj.getString("total_rewards_points");
                        loyality_status = data_obj.getString("loyality_status");
                        status_update_date = data_obj.getString("loyalty_status_updated_date");

                        if (number != null)
                        {
                            number.setText(total_rewards_points +" "+getResources().getString(R.string.point));
                        }
                        else
                        {
                            number.setVisibility(View.GONE);
                        }

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("result_settingpage", e.toString());
            }
            loader.hidepDialog();
        }
    }

    private void Scroll_activity_list() {

        recycler_view_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = lm.getItemCount();
                firstVisibleItem = lm.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    //Log.e("flag-Scroll", flag_scroll + "dfsfsd");
                } else {
                    if (loading) {
                        //Log.e("flag-Loading", loading + "");
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        //Log.e("flag-Loading_second_if", loading + "");
                        if (CommanMethod.isInternetOn(MainActivity.this)) {
                            page_count++;
                         //   order_list = new ArrayList<>();
                            //call offerlist api
                            new Main_offer_list("Init").execute();
                           // mainOrderListAdapter.notifyDataSetChanged();
                        } else {

                            comman_dialog.Show_alert(Comman_message.Dont_internet);
                        }
                        loading = true;
                    }
                   // flag_scroll = false;
                }
            }
        });
    }

    private void Get_Location() {
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.e("Lat_Long = ", latitude + " , " + longitude);

        } else {

            //First_time_loadofferlist();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume","resume");
        if (CommanMethod.isInternetOn(this)) {
            //call userstatus api
            new User_Status().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_header_msg:
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                break;

            case R.id.linear_balance:
                Fragment fragment3 = new PayManageFragment();
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.fram_main, fragment3);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();

                break;
            case R.id.linear_stores:
                Intent intent_store = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(intent_store);

                break;
            case R.id.linear_gift:
                Fragment fragment = new GiftFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.linear_order:
                Intent i = new Intent(MainActivity.this,OrderActivity.class);
                startActivity(i);
                /*Intent i = new Intent(MainActivity.this,MenuOrder.class);
                startActivity(i);*/
                /*Fragment fragment2 = new SearchActivity();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fram_main, fragment2);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();*/
                break;

            case R.id.linear_history:
                Intent intent4 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent4);
                break;

            case R.id.linear_setting:
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                break;

            case R.id.linear_card:
                Intent intent1 = new Intent(MainActivity.this, MyCards.class);
                startActivity(intent1);
                break;
            case R.id.relative_subtop:
                Intent intent3 = new Intent(MainActivity.this, RewardPontsActivity.class);
                startActivity(intent3);

                break;
            case R.id.relative_top1:
                Log.e("status loyalty", loyality_status);
                startActivity(new Intent(MainActivity.this, GoldStatus.class)
                        .putExtra("reward_point", total_rewards_points)
                        .putExtra("status", loyality_status)
                        .putExtra("statusupdate_date", status_update_date));


                break;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        First_time_loadofferlist();

    }

    private void First_time_loadofferlist() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (CommanMethod.isInternetOn(MainActivity.this)) {

                    Log.e("calpl", "call");
                   /* mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/
                    Get_Location();
                    order_list = new ArrayList<>();
                    new Main_offer_list("Init").execute();
                } else {
                    comman_dialog.Show_alert(Comman_message.Dont_internet);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                }
            } else {
                // permission has been granted, continue as usual

                if (CommanMethod.isInternetOn(MainActivity.this)) {
                    /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Log.e("cal4l", "call");
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/
                    Get_Location();
                    order_list = new ArrayList<>();
                    new Main_offer_list("Init").execute();
                } else {
                    comman_dialog.Show_alert(Comman_message.Dont_internet);
                }

            }
        } else {
            if (CommanMethod.isInternetOn(MainActivity.this)) {

                Log.e("call2", "call");
                /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }*/
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Get_Location();
                order_list = new ArrayList<>();
                new Main_offer_list("Init").execute();
            } else {
                comman_dialog.Show_alert(Comman_message.Dont_internet);
            }

        }
        mainOrderListAdapter = new MainOrderListAdapter(MainActivity.this, order_list);
        recycler_view_main.setAdapter(mainOrderListAdapter);
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

    @Override
    protected void onStart() {
        Log.e("start", "start");
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
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
                        //get user current location
                        Get_Location();
                        order_list = new ArrayList<>();
                        //call offer list api
                        new Main_offer_list("Init").execute();


                    } else {
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
                        order_list = new ArrayList<>();
                        //call offer list api
                        new Main_offer_list("Init").execute();
                    }
                }
                break;
        }
    }

    private class Main_offer_list extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String type = "";

        public Main_offer_list(String type) {
            this.type = type;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (type.equalsIgnoreCase("Init")) {
                loader.showpDialog();
            } else {

            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("10")))
                        .add("page_offset", MCrypt.bytesToHex(mCrypt.encrypt(page_count + "")))
                        .add("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude + "")))
                        .add("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude + "")))
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(timezoneID + "")))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("page_limit", "10");
                Log.e("page_offset11", page_count + "");
                Log.e("latitude", latitude + "");
                Log.e("longitude", longitude + "");
                Log.e("userID", sharedPrefs.get_User_id());
                Log.e("timezone", timezoneID + "");
                Log.e("Apikey", Comman_url.api_key);

                Log.e("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("10")));
                Log.e("page_offset11", MCrypt.bytesToHex(mCrypt.encrypt(page_count + "")));
                Log.e("latitude", MCrypt.bytesToHex(mCrypt.encrypt(latitude + "")));
                Log.e("longitude", MCrypt.bytesToHex(mCrypt.encrypt(longitude + "")));
                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("timezone", MCrypt.bytesToHex(mCrypt.encrypt(timezoneID + "")));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Main_offer_list, reqbody);

            } catch (Exception e) {
                Log.e("err_Offer_listcatch", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("err_Offer_listonpost", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                       // JSONArray specialoffer_array = main_obj.getJSONArray("SpecialofferList");
                       // Log.e("specialoffer", String.valueOf(specialoffer_array.length()));

                        JSONArray data_array = main_obj.getJSONArray("offers");
                        Log.e("data", String.valueOf(data_array.length()));
                       // Log.e("special", String.valueOf(specialoffer_array.length()));

                        /*for (int j = 0  ; j  <  specialoffer_array.length();j++)
                        {
                            JSONObject specialofferdata = specialoffer_array.getJSONObject(j);

                            offer_name = specialofferdata.getString("offers_title");
                            offer_idescription = specialofferdata.getString("offers_description");
                            offer_image = specialofferdata.getString("offer_image");
                            offers_types = specialofferdata.getString("offers_types");
                            offer_code = specialofferdata.getString("offer_code");
                            generated_QR_Code = specialofferdata.getString("generated_QR_Code");
                            to_date = specialofferdata.getString("to_date");
                            product_name = specialofferdata.getString("to_time");
                            category_name = specialofferdata.getString("categories_ids");

                            JSONArray jsonArrayspecialoffer_menudetail = specialofferdata.getJSONArray("menu_details");
                            menu_name = "";
                            for (int k = 0; k < jsonArrayspecialoffer_menudetail.length(); k++) {
                                first_name = "";
                                JSONObject jobj_menudetail = jsonArrayspecialoffer_menudetail.getJSONObject(k);

                                first_name = jobj_menudetail.getString("first_name");
                                if (menu_name.equalsIgnoreCase("")) {
                                    menu_name = first_name;
                                } else {
                                    menu_name = menu_name + ", " + first_name;
                                }

                            }
                            //fill specialoffer data in arraylist
                            order_list.add(new MainOrderlistItem(offer_name, offer_image, offer_idescription,
                                    offer_code, generated_QR_Code, to_date, product_name, menu_name, category_name));
                        }*/




                            if (data_array.length() < 10 || data_array.length() == 0) {
                                flag_scroll = true;
                                Log.e("length_array_MYACT", flag_scroll + "" + "<10===OR(0)===" + data_array.length());

                            }


                        for (int i = 0; i < data_array.length(); i++) {

                            JSONObject json_data = data_array.getJSONObject(i);

                            offer_name = json_data.getString("offers_title");
                            offer_idescription = json_data.getString("offers_description");
                            offer_image = json_data.getString("offer_image");
                            offers_types = json_data.getString("offers_types");
                            offer_code = json_data.getString("offer_code");
                            generated_QR_Code = json_data.getString("generated_QR_Code");
                            to_date = json_data.getString("to_date");
                            product_name = json_data.getString("to_time");
                            category_name = json_data.getString("categories_ids");

                            JSONArray jsonArray_menudetail = json_data.getJSONArray("menu_details");
                            menu_name = "";
                            for (int k = 0; k < jsonArray_menudetail.length(); k++) {
                                first_name = "";
                                JSONObject jobj_menudetail = jsonArray_menudetail.getJSONObject(k);

                                first_name = jobj_menudetail.getString("first_name");
                                if (menu_name.equalsIgnoreCase("")) {
                                    menu_name = first_name;
                                } else {
                                    menu_name = menu_name + ", " + first_name;
                                }

                            }

                            //fill offer data in arraylist
                            order_list.add(new MainOrderlistItem(offer_name, offer_image, offer_idescription,
                                    offer_code, generated_QR_Code, to_date, product_name, menu_name, category_name));
                        }


                        Log.e("order_list_size", order_list.size() + "");

                        //getting data null then display no offer text
                        if (order_list.size()==0)
                        {
                            Log.e("nulll","null");
                            recycler_view_main.setVisibility(View.GONE);
                            notxtdata.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.e("notnulll","notnull");
                            notxtdata.setVisibility(View.GONE);
                            recycler_view_main.setVisibility(View.VISIBLE);
                        }
                        if (order_list.size() > 0) {
                            Log.e("ADAPTER", "Notify");
                            mainOrderListAdapter.notifyDataSetChanged();
                        }else {
                            mainOrderListAdapter = new MainOrderListAdapter(MainActivity.this, order_list);
                            recycler_view_main.setAdapter(mainOrderListAdapter);
                        }

                        swipe_refresh_layout.setRefreshing(false);
                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("err_Offer_listtt", e.toString());
            }
            loader.hidepDialog();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}