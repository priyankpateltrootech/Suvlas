package com.suvlas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brandongogetap.stickyheaders.StickyLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adpter.OrderAdapter;
import adpter.RecyclerAdapter;
import bean.MenuOrderItem;
import bean.Restaurant_list;
import bean.Storelistorder;
import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.HeaderItem;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class OrderActivity extends AppCompatActivity {

    RecyclerView store_recyclerview;
    LinearLayout linear_pickup,linear_delivery,linear_order;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    MCrypt mCrypt;
    ArrayList<Restaurant_list> restaurant_name_list;
    ArrayList<Storelistorder> storelistorderArrayList;
    OrderAdapter orderAdapter;
    LinearLayoutManager linearLayoutManager;
    ImageView img_back_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();

    }

    private void findviewID()
    {
        store_recyclerview = (RecyclerView)findViewById(R.id.store_recyclerview);
        linear_pickup = (LinearLayout)findViewById(R.id.linear_pickup);
        linear_delivery = (LinearLayout)findViewById(R.id.linear_delivery);
        linear_order = (LinearLayout)findViewById(R.id.linear_order);
        img_back_order = (ImageView)findViewById(R.id.img_back_order);
    }
    private void set_listeners()
    {
        linear_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_order.setVisibility(View.GONE);
                store_recyclerview.setVisibility(View.VISIBLE);
                /*Fragment fragment2 = new SearchActivity();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fram_main, fragment2);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();*/
            }
        });
        linear_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm = OrderActivity.this.getPackageManager();
                boolean app_installed;
                try {
                    pm.getPackageInfo("com.mobmedianet.appetito24", PackageManager.GET_ACTIVITIES);
                    app_installed = true;
                } catch (PackageManager.NameNotFoundException e) {
                    app_installed = false;
                }
                if (app_installed)
                {
                    Intent intent = pm.getLaunchIntentForPackage("com.mobmedianet.appetito24");
                    startActivity(intent);
                }
                else
                {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.mobmedianet.appetito24")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.mobmedianet.appetito24")));
                    }
                }

            }
        });
        img_back_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void init()
    {
        comman_dialog = new Comman_Dialog(this);
        mCrypt = new MCrypt();
        loader = new Request_loader(this);
        sharedPrefs = new SharedPrefs(this);

        linearLayoutManager = new LinearLayoutManager(this);
        restaurant_name_list = new ArrayList<>();
        store_recyclerview.setLayoutManager(linearLayoutManager);
        //new Store_Location().execute();

        storelistorderArrayList = new ArrayList<>();
        new Get_Store().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {

        Log.e("backpress","backpress");
        if (linear_order.getVisibility() == View.VISIBLE)
        {
           /* Intent i = new Intent(OrderActivity.this,MainActivity.class);
            startActivity(i);*/
            super.onBackPressed();
        }
        else
        {
            linear_order.setVisibility(View.VISIBLE);
        }
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

                Log.e("Apikey", Comman_url.api_key);

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
            Log.e("err_store_list2", result + "");

            if (result != null)
            {
                try {
                    JSONObject main_obj = new JSONObject(result);
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Stores");
                        Log.e("data_array", String.valueOf(data_array));

                        //restaurant_name_list.add(0,new Restaurant_list(getResources().getString(R.string.favourite_location),"0"));
                        for (int i = 0; i < data_array.length(); i++) {

                            JSONObject json_data = data_array.getJSONObject(i);

                            String restaurent_name = json_data.getString("name");
                            //Log.e("restaurantname",restaurent_name);
                            String restaurant_id = json_data.getString("id");
                            //Log.e("restaurantid",restaurant_id);

                            restaurant_name_list.add(new Restaurant_list(restaurent_name,restaurant_id));
                        }

                        /*orderAdapter = new OrderAdapter(OrderActivity.this, restaurant_name_list, new OnClickListener() {
                            @Override
                            public void onItemClicked(int position) {
                                Intent i = new Intent(OrderActivity.this,MenuOrder.class);
                                i.putExtra("restaurant_name",restaurant_name_list.get(position).getName());
                                i.putExtra("restaurant_id",restaurant_name_list.get(position).getId());
                                startActivity(i);
                            }
                        });*/
                        store_recyclerview.setAdapter(orderAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loader.hidepDialog();
            }
        }
    }

    public class Get_Store extends AsyncTask<String, Void, String> {

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
//
                Responce_jason = CallingMethod.GET_Store(client, Comman_api_name.Get_store_list, OrderActivity.this);

            } catch (Exception e) {
                Log.e("errrrrr_menu", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("rsulttt",result);

            try {
                JSONObject jsonstoreObject = new JSONObject(result);

                JSONArray jsonstoreArray = jsonstoreObject.getJSONArray("franquicias");

                for (int i = 0 ; i < jsonstoreArray.length();i++)
                {
                    JSONObject jsonstoredataObject = jsonstoreArray.getJSONObject(i);

                    storelistorderArrayList.add(new Storelistorder(jsonstoredataObject.getString("APIKEY"),jsonstoredataObject.getString("tokenInvu"),jsonstoredataObject.getString("negocio"),
                            jsonstoredataObject.getString("principal"), jsonstoredataObject.getString("id_franquicia"), jsonstoredataObject.getString("franquicia"),jsonstoredataObject.getString("horaCierreLocal")));

                }
                orderAdapter = new OrderAdapter(OrderActivity.this, storelistorderArrayList, new OnClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        Intent i = new Intent(OrderActivity.this,MenuOrderActivity.class);
                        i.putExtra("restaurant_name",storelistorderArrayList.get(position).getName());
                        i.putExtra("restaurant_id",storelistorderArrayList.get(position).getId_frenchies());
                        i.putExtra("restaurant_apikey",storelistorderArrayList.get(position).getApikey());
                        startActivity(i);
                    }
                });
                store_recyclerview.setAdapter(orderAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            loader.hidepDialog();
        }

    }
}
