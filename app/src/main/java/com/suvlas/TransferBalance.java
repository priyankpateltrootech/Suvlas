package com.suvlas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.OrderHistoryRecyclerviewAdapter;
import adpter.TransferbalanceAdapter;
import bean.ItemOrderHistory;
import bean.TransferBalanceBean;
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
 * Created by hp on 4/26/2017.
 */

public class TransferBalance extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recyclerview_transferbalance;
    Request_loader loader;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    List<TransferBalanceBean> transferBalanceBeanList;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int page_count = 0;
    boolean flag_scroll = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 0;
    GridLayoutManager gridLayoutManager;
    TransferbalanceAdapter  transferbalanceAdapter;
    TextView txt_notext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.transferbalance);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();

    }

    private void Load_transaction_history_list() {
        if (CommanMethod.isInternetOn(TransferBalance.this)) {

            previousTotal = 0;
            totalItemCount = 0;
            page_count=0;
            flag_scroll = false;
            transferBalanceBeanList = new ArrayList<>();

            //call offer list api
            new get_transaction_history().execute();
        } else {
            //swipe_refresh_layout.setRefreshing(false);
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        //recyclerview bind with mainnorderlist adapter
        transferbalanceAdapter = new TransferbalanceAdapter(TransferBalance.this, transferBalanceBeanList);
        recyclerview_transferbalance.setAdapter(transferbalanceAdapter);
        //adapter.notifyDataSetChanged();
        //Scroll_activity_list();
    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        recyclerview_transferbalance = (RecyclerView)findViewById(R.id.recyclerview_transferbalance);
        txt_notext = (TextView)findViewById(R.id.txt_notext);
    }

    private void init() {

        loader = new Request_loader(this);
        mCrypt = new MCrypt();
        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);

        gridLayoutManager = new GridLayoutManager(TransferBalance.this,1);
        recyclerview_transferbalance.setLayoutManager(gridLayoutManager);

        Load_transaction_history_list();
    }

    private void set_listeners() {

        img_back.setOnClickListener(this);

        Scroll_activity_list();

    }




    private void Scroll_activity_list() {

        recyclerview_transferbalance.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

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

                        if (CommanMethod.isInternetOn(TransferBalance.this)) {
                            page_count++;

                            new get_transaction_history().execute();
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

    private class get_transaction_history extends AsyncTask<String, Void, String> {

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

                Log.e("user_id", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("20")))
                        .add("page_offset",MCrypt.bytesToHex(mCrypt.encrypt(page_count+"")))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.get_transaction_history, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_transaction_history", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_transaction_history", result + "");

            try {
                if (result != null) {

                    JSONObject jsonmainObject = new JSONObject(result);

                    String code = jsonmainObject.getString("code");

                    if (code.equalsIgnoreCase("200"))
                    {
                        JSONArray jsondataArray = jsonmainObject.getJSONArray("Transaction");

                        if (jsondataArray.length() < 20 || jsondataArray.length() == 0) {
                            flag_scroll = true;
                            Log.e("length_array_MYACT", flag_scroll + "" + "<10===OR(0)===" + jsondataArray.length());

                        }

                        for (int i = 0 ; i < jsondataArray.length() ; i++)
                        {

                            JSONObject jsondataObject = jsondataArray.getJSONObject(i);
                            transferBalanceBeanList.add(new TransferBalanceBean(jsondataObject.getString("transaction_id"),jsondataObject.getString("transfer_date"),jsondataObject.getString("amount")));

                        }

                        if (transferBalanceBeanList.size() == 0)
                        {
                            recyclerview_transferbalance.setVisibility(View.GONE);
                            txt_notext.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            txt_notext.setVisibility(View.GONE);
                            recyclerview_transferbalance.setVisibility(View.VISIBLE);
                        }

                        if (transferBalanceBeanList.size() > 0) {
                            Log.e("ADAPTER", "Notify");
                            transferbalanceAdapter.notifyDataSetChanged();
                        }else {
                            transferbalanceAdapter = new TransferbalanceAdapter(TransferBalance.this, transferBalanceBeanList);
                            recyclerview_transferbalance.setAdapter(transferbalanceAdapter);
                        }



                    }

                }
            } catch (Exception e) {
                Log.e("exception_transaction_history", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}
