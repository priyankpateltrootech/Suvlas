package com.suvlas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

import adpter.MainOrderListAdapter;
import adpter.OrderHistoryRecyclerviewAdapter;
import bean.HistoryOrderModifier;
import bean.ItemOrderHistory;
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
 * Created by hp on 5/4/2017.
 */

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back,img_msg;
    RecyclerView rView;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    List<ItemOrderHistory> rowListItem;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int page_count = 0;
    boolean flag_scroll = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 0;
    GridLayoutManager manager;
    OrderHistoryRecyclerviewAdapter adapter;
    TextView txt_notext;
    ArrayList<String> orderitemarraylsit;
    ArrayList<HistoryOrderModifier> historyOrderModifierArrayList;
    String order_image;
    ImageView img_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistoryfragment);

        // initialize all component
        findviewID();

        init();

        set_listeners();
        //set_font();

    }

    private void Load_history_list() {
        if (CommanMethod.isInternetOn(HistoryActivity.this)) {

            previousTotal = 0;
            totalItemCount = 0;
            page_count=0;
            flag_scroll = false;
            rowListItem = new ArrayList<>();

            //call offer list api
            new get_history().execute();
        } else {
            //swipe_refresh_layout.setRefreshing(false);
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        //recyclerview bind with mainnorderlist adapter
        adapter = new OrderHistoryRecyclerviewAdapter(HistoryActivity.this, rowListItem);
        rView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        //Scroll_activity_list();
    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_msg=(ImageView)findViewById(R.id.img_msg);
        rView = (RecyclerView)findViewById(R.id.recycler_view);
        txt_notext = (TextView)findViewById(R.id.txt_notext);
        img_history = (ImageView)findViewById(R.id.img_history);
    }

    private void init() {

        sharedPrefs = new SharedPrefs(this);
        loader = new Request_loader(this);
        comman_dialog = new Comman_Dialog(this);
        mCrypt = new MCrypt();

        manager = new GridLayoutManager(HistoryActivity.this, 2);
        rView.setLayoutManager(manager);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });

        Load_history_list();
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);

        Scroll_activity_list();

    }

    private void Scroll_activity_list() {

        rView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.e("childcount",recyclerView.getChildCount()+"");
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    //Log.e("flag-Scroll", flag_scroll + "dfsfsd");
                } else {
                    if (loading) {
                        //Log.e("flag-Loading", loading + "");
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

                        if (CommanMethod.isInternetOn(HistoryActivity.this)) {
                            page_count++;

                            new get_history().execute();
                        } else {

                            comman_dialog.Show_alert(Comman_message.Dont_internet);
                        }
                        loading = true;
                    }
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

    private class get_history extends AsyncTask<String, Void, String> {

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

                Log.e("user_id",MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("20")))
                        .add("page_offset",MCrypt.bytesToHex(mCrypt.encrypt(page_count+"")))
                        .build();

                /*Log.e("userid",MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("page_limit",MCrypt.bytesToHex(mCrypt.encrypt("5")));
                Log.e("page_offset",MCrypt.bytesToHex(mCrypt.encrypt(page_count+"")));
                Log.e("pagecount",page_count+"");*/
                Responce_jason = CallingMethod.POST(client, Comman_api_name.get_order_history, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_history", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_history", result + "");

            try {
                if (result != null) {

                    JSONObject jsonmainObject = new JSONObject(result);

                    String code = jsonmainObject.getString("code");


                    if (code.equalsIgnoreCase("200"))
                    {
                        JSONArray jsondataArray = jsonmainObject.getJSONArray("Orders");

                        if (jsondataArray.length() < 20 || jsondataArray.length() == 0) {
                            flag_scroll = true;
                            Log.e("length_array_MYACT", flag_scroll + "" + "<10===OR(0)===" + jsondataArray.length());

                        }

                        for (int i = 0 ; i < jsondataArray.length() ; i++)
                        {

                            JSONObject jsondataObject = jsondataArray.getJSONObject(i);

                            JSONArray jsonorderdataArray = jsondataObject.getJSONArray("order_details");

                            orderitemarraylsit = new ArrayList<>();

                            historyOrderModifierArrayList = new ArrayList<>();

                            for (int j = 0 ; j < jsonorderdataArray.length();j++)
                            {
                                JSONObject jsonorderdataobject = jsonorderdataArray.getJSONObject(j);
                                orderitemarraylsit.add(jsonorderdataobject.getString("order_item_name"));

                                Log.e("order_item_name",jsonorderdataobject.getString("order_item_name"));

                                JSONArray jsonArray = jsonorderdataobject.getJSONArray("modi");

                                for (int k = 0 ; k< jsonArray.length();k++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(k);

                                    historyOrderModifierArrayList.add(new HistoryOrderModifier(jsonObject.getString("modifier_name"),jsonObject.getString("modifier_price"),jsonObject.getString("modifier_quantity")));
                                }

                            }

                            for (int k = 0 ; k < jsonorderdataArray.length();k++)
                            {

                                JSONObject jsonorderdataobject = jsonorderdataArray.getJSONObject(k);


                                ArrayList<String> orderimage = new ArrayList<>();

                                if (orderimage.size()>0)
                                {

                                }
                                else
                                {
                                    orderimage.add(jsonorderdataobject.getString("order_item_image"));
                                    Log.e("orderimage",orderimage.size()+"");
                                    order_image = jsonorderdataobject.getString("order_item_image");
                                }

                            }
                            Log.e("historysize",historyOrderModifierArrayList.size()+"");

                            String citiesCommaSeparated = TextUtils.join(",",orderitemarraylsit);
                            Log.e("orderitemsize",orderitemarraylsit.size()+"");
                            Log.e("citiesCommaSeparated",citiesCommaSeparated);
                            rowListItem.add(new ItemOrderHistory(citiesCommaSeparated,jsondataObject.getString("order_date"),order_image,historyOrderModifierArrayList));

                        }

                        if (rowListItem.size() == 0)
                        {
                            rView.setVisibility(View.GONE);
                            txt_notext.setVisibility(View.VISIBLE);
                            img_history.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            img_history.setVisibility(View.GONE);
                            txt_notext.setVisibility(View.GONE);
                            rView.setVisibility(View.VISIBLE);
                        }
                        Log.e("rowListItem",rowListItem.size()+"");
                        if (rowListItem.size() > 0) {
                            Log.e("ADAPTER", "Notify");
                            adapter.notifyDataSetChanged();
                        }else {
                            adapter = new OrderHistoryRecyclerviewAdapter(HistoryActivity.this, rowListItem);
                            rView.setAdapter(adapter);
                        }




                    }
                }
            } catch (Exception e) {
                Log.e("exception_history", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
