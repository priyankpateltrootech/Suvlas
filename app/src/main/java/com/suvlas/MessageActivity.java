package com.suvlas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adpter.MainOrderListAdapter;
import adpter.MessageAdapter;
import bean.MainOrderlistItem;
import bean.MessageItem;
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
 * Created by hp on 3/10/2017.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recycler_view_main;
    GridLayoutManager lm;
    MessageAdapter messsageadapter;
    public SwipeRefreshLayout swipe_refresh_layout;
    ArrayList<MessageItem> message_list = new ArrayList<>();
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int page_count = 0;
    boolean flag_scroll = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 0;
    String createdate = "";
    public static TextView txt_remainmsg;
    public static int j = 0;
    String menu_nombre, menu_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.messgaepage);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_remainmsg = (TextView) findViewById(R.id.txt_remainmsg);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler_view_main = (RecyclerView) findViewById(R.id.recycler_view_message);

    }

    private void set_listeners() {
        img_back.setOnClickListener(this);


        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_message_list();
            }
        });

        Scroll_activity_list();
        Log.e("helo", "");
    }


    public void Call_Notification_List() {
        if (CommanMethod.isInternetOn(MessageActivity.this)) {
            message_list = new ArrayList<>();
            //call messagelist api
            new MessageDetail("Init").execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        lm = new GridLayoutManager(MessageActivity.this, 1);
        recycler_view_main.setLayoutManager(lm);
        messsageadapter = new MessageAdapter(MessageActivity.this, message_list);
        recycler_view_main.setAdapter(messsageadapter);
    }

    //load message list
    private void Load_message_list() {
        if (CommanMethod.isInternetOn(MessageActivity.this)) {
            flag_scroll = false;
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //call messagelist api
            message_list = new ArrayList<>();
            new MessageDetail("refresh").execute();
        } else {
            swipe_refresh_layout.setRefreshing(false);
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        //bind recyclerview with messageadapter
        lm = new GridLayoutManager(MessageActivity.this, 1);
        recycler_view_main.setLayoutManager(lm);
        messsageadapter = new MessageAdapter(MessageActivity.this, message_list);
        recycler_view_main.setAdapter(messsageadapter);

    }

    private void init() {

        mCrypt = new MCrypt();
        loader = new Request_loader(MessageActivity.this);
        sharedPrefs = new SharedPrefs(MessageActivity.this);
        comman_dialog = new Comman_Dialog(MessageActivity.this);
        swipe_refresh_layout.setColorSchemeResources(R.color.loginbtn);
        if (CommanMethod.isInternetOn(MessageActivity.this)) {
            message_list = new ArrayList<>();
            //call messagelist api
            new MessageDetail("Init").execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        //bind recyclerview with messageadapter
        lm = new GridLayoutManager(MessageActivity.this, 1);
        recycler_view_main.setLayoutManager(lm);
        messsageadapter = new MessageAdapter(MessageActivity.this, message_list);
        recycler_view_main.setAdapter(messsageadapter);
    }

    //scroll using pagination
    private void Scroll_activity_list() {

        recycler_view_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = lm.getItemCount();
                firstVisibleItem = lm.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    //Log.e("flag-Scroll", flag_scroll + "");
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
                        if (CommanMethod.isInternetOn(MessageActivity.this)) {
                            page_count++;

                        } else {
                            //call messagelist api
                            new MessageDetail("Init").execute();
                            comman_dialog.Show_alert(Comman_message.Dont_internet);
                        }
                        loading = true;
                    }
                    //flag_scroll = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    private class MessageDetail extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String type = "";

        public MessageDetail(String type) {
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
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("page_limit", "10");
                Log.e("page_offset11", page_count + "");
                Log.e("userID", sharedPrefs.get_User_id());

                Log.e("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("10")));
                Log.e("page_offset11", MCrypt.bytesToHex(mCrypt.encrypt(page_count + "")));
                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.message_detail, reqbody);

            } catch (Exception e) {
                Log.e("err_message", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("message", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Notification");
                        if (data_array.length() < 10 || data_array.length() == 0) {
                            flag_scroll = true;
                            Log.e("length_array_MYACT", flag_scroll + "" + "<10===OR(0)===" + data_array.length());
                        }
                        j = 0;
                        for (int i = 0; i < data_array.length(); i++) {

                            JSONObject json_data = data_array.getJSONObject(i);

                            createdate = json_data.getString("created_date");
                            String message_title = json_data.getString("title");
                            String message_description = json_data.getString("description");
                            String message_status = json_data.getString("message_status");
                            String message_id = json_data.getString("id");
                            String offers_type = json_data.getString("offers_type");
                            String message_url = json_data.getString("MessageImage_Url");

                            String offer_title = "",offer_description="",expire_date="",expire_time="",offer_image_detail="",offer_code="",offer_qr_code="";
                            JSONObject json_doffer_detail = json_data.getJSONObject("offers_details");

                            if (json_doffer_detail.has("offers_title")) {
                                offer_title = json_doffer_detail.getString("offers_title");
                                offer_description = json_doffer_detail.getString("offers_description");
                                expire_date = json_doffer_detail.getString("to_date");
                                offer_image_detail=json_doffer_detail.getString("offer_image");
                                offer_code=json_doffer_detail.getString("offer_code");
                                offer_qr_code=json_doffer_detail.getString("generated_QR_Code");
//                                 expire_time = json_doffer_detail.getString("to_time");

                                if (json_doffer_detail.has("menu_details")) {
                                    JSONArray jsonArray_menudetail = json_doffer_detail.getJSONArray("menu_details");
                                    menu_name = "";
                                    for (int k = 0; k < jsonArray_menudetail.length(); k++) {
                                        menu_nombre = "";
                                        JSONObject jobj_menudetail = jsonArray_menudetail.getJSONObject(k);

                                        menu_nombre = jobj_menudetail.getString("nombre");
                                        if (menu_name.equalsIgnoreCase("")) {
                                            menu_name = menu_nombre;
                                        } else {
                                            menu_name = menu_name + ", " + menu_nombre;
                                        }
                                    }
                                }
                            }
                            if (message_status.equalsIgnoreCase("Unread")) {
                                j++;
                                if (j < 10) {
                                    txt_remainmsg.setText("Usted tiene " + "0" + j + " mensajes nuevos");
                                } else {
                                    txt_remainmsg.setText("Usted tiene " + j + " mensajes nuevos");
                                }
                            }

                            String[] parts = createdate.split(" ");
                            String date = parts[0];
                            SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
                            Date newDate = null;
                            try {
                                newDate = df_date.parse(date);
                                df_date = new SimpleDateFormat("dd/MM/yyyy");
                                String final_date = df_date.format(newDate);
                                Log.e("final_date", final_date);
                                message_list.add(new MessageItem(final_date, message_title, message_description,
                                        message_status, message_id,offers_type,message_url,offer_title,offer_description,menu_name,
                                        expire_date,expire_time,offer_image_detail,offer_code,offer_qr_code));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (j == 0) {
                            txt_remainmsg.setText("Usted tiene " + "0" + " mensajes nuevos");
                        }
                        Log.e("message", String.valueOf(j));
                        if (message_list.size() > 0) {
                            Log.e("ADAPTER", "Notify");

                            messsageadapter.notifyDataSetChanged();
                        }
                        swipe_refresh_layout.setRefreshing(false);
                    } else {
                        swipe_refresh_layout.setRefreshing(false);
                        txt_remainmsg.setText(main_obj.getString("message"));
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("err_message", e.toString());
            }
            loader.hidepDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
