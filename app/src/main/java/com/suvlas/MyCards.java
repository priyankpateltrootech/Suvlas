package com.suvlas;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
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
 * Created by hp on 4/19/2017.
 */

public class MyCards extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back,img_card,imj_barcode,img_bluebarcode;
    RelativeLayout rel_img_card;
    Button btn_pay;
    TextView text_user,text_barcode_num,txt_barcodenum,number_point;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    TextView txt_loyality_points;
    Button btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mycard);

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
        img_card=(ImageView)findViewById(R.id.img_card);
        imj_barcode=(ImageView)findViewById(R.id.imj_barcode);
//        rel_img_card=(RelativeLayout)findViewById(R.id.img_card);
        btn_pay=(Button)findViewById(R.id.btn_pay);
        text_user=(TextView)findViewById(R.id.text_user);
        text_barcode_num=(TextView)findViewById(R.id.text_barcode_num);
        txt_barcodenum=(TextView)findViewById(R.id.txt_barcodenum);
        number_point=(TextView)findViewById(R.id.number_point);
        txt_loyality_points = (TextView)findViewById(R.id.txt_loyality_points);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isInternetOn(MyCards.this)) {

                    //call edit profile api
                    new reload_card().execute();


                } else {
                    comman_dialog.Show_alert(Comman_message.Dont_internet);
                }
            }
        });
    }

    private void init() {
        mCrypt = new MCrypt();
        loader = new Request_loader(this);
        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);
        if (CommanMethod.isInternetOn(this)) {

            //call edit profile api
            new reload_card().execute();
            //new My_card_detail().execute();

        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
//        Point size = new Point();
//        getWindowManager().getDefaultDisplay().getSize(size);
//        int display_width = size.x;
//        int display_height = size.y;
//        Log.e("Width", display_width + "==" + display_width);
//        Log.e("Height", display_height + "==" + display_height);
//        img_card.getLayoutParams().height = (int) (display_height /3.2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_pay:
                Fragment fragment = new PayManageFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_card, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
        }

    }


    private class My_card_detail extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //loader.showpDialog();
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
                Log.e("errrrrr_mycard", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_mycard", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);
                    Log.e("mycard", String.valueOf(main_obj));
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {
                        JSONObject data_array = main_obj.getJSONObject("data");

                        String name = data_array.getString("name");
                        String barcode = data_array.getString("barcode");
                        String barcode_url = data_array.getString("barcode_url");
                        String hightest_points = data_array.getString("hightest_loyaltystatus_points");
                        String total_reward_points = data_array.getString("total_rewards_points");
                        Log.e("hightest_points", hightest_points);
                        Log.e("preview", name);

                        txt_loyality_points.setText(total_reward_points);
                        text_user.setText(name);
                        text_barcode_num.setText(barcode);
                        txt_barcodenum.setText(barcode);
                        number_point.setText(total_reward_points);
                        Glide.with(MyCards.this).load(barcode_url).into(imj_barcode);

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("result_giftcard", e.toString());
            }
            //loader.hidepDialog();
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyCards.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private class reload_card extends AsyncTask<String, Void, String> {

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

                Log.e("Apikey", Comman_url.api_key);
                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));


                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.reload_card, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_reloadcard", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_reload_card", result + "");

            try {
                if (result != null) {

                    JSONObject reload_card_object = new JSONObject(result);

                    if (reload_card_object.getString("code").equalsIgnoreCase("200"))
                    {
                        /*Glide.with(MyCards.this).load(reload_card_object.getString("card")).placeholder(R.drawable.loading).error(R.drawable.loading).into(img_card);*/
                        Glide.with(MyCards.this).load(reload_card_object.getString("card")).into(img_card);

                        JSONObject wallet_object = reload_card_object.getJSONObject("data");

                        txt_loyality_points.setText(wallet_object.getString("total_rewards_points"));
                        text_barcode_num.setText(wallet_object.getString("barcode"));
                        txt_barcodenum.setText(wallet_object.getString("barcode"));
                        number_point.setText(wallet_object.getString("total_rewards_points"));

                        Glide.with(MyCards.this).load(wallet_object.getString("barcode_url")).into(imj_barcode);
                        //txt_wallet_balance.setText(wallet_object.getString("wallet_amount"));
                    }
                    else
                    {
                        Toast.makeText(MyCards.this,reload_card_object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("exception_reload_card", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}