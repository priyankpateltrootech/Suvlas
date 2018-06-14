package com.suvlas;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import common.CallingMethod;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 6/2/2017.
 */

public class OfferDetailsActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    Request_loader loader;
    MCrypt mCrypt;
    String notificationofferid, notificationtype;
    String menu_nombre, menu_name;

    static {
        System.loadLibrary("iconv");
    }

    ImageView img_back, img_back2, img_offer, img_barcode;
    LinearLayout lin_scn_btn, lin_scn_code, lin_top;
    TextView txt_offer_des, txt_offer, txt_expiredate, txt_offercode, txt_expiretime, txt_item, txtnodata;
    ViewGroup contentFrame;
    RelativeLayout rel_lay, relative_top, rel_scan, rel_des, rel_nodata;
    private ZXingScannerView mScannerView;
    String offer_name = "", offer_description = "", offer_image = "", offer_code = "", offer_qrcode = "", offer_exp_date = "", productname = "", item_name = "", category_name = "";
    LinearLayout linear_nodata, linear_data;
    String timezoneID;

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offerdetail);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();

        //initialize component
        init();
    }

    private void set_listeners() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lin_scn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(OfferDetailsActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(OfferDetailsActivity.this, android.Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(OfferDetailsActivity.this, new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(OfferDetailsActivity.this, new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else if (relative_top.getVisibility() == View.VISIBLE) {
                    mScannerView.startCamera();
                    lin_top.setVisibility(View.VISIBLE);
                    relative_top.setVisibility(View.GONE);
                    img_barcode.setVisibility(View.GONE);
                    rel_scan.setVisibility(View.VISIBLE);
                } else if (relative_top.getVisibility() == View.GONE) {
                    lin_top.setVisibility(View.GONE);
                    relative_top.setVisibility(View.VISIBLE);
                    img_barcode.setVisibility(View.VISIBLE);
                    rel_scan.setVisibility(View.GONE);
                }
//                else {
//                    lin_top.setVisibility(View.VISIBLE);
//                    relative_top.setVisibility(View.GONE);
//                }
            }
        });
    }

    private void findviewID() {

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back2 = (ImageView) findViewById(R.id.img_back2);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);
        lin_scn_btn = (LinearLayout) findViewById(R.id.lin_scan);
        lin_top = (LinearLayout) findViewById(R.id.lin_top_child);
        lin_scn_code = (LinearLayout) findViewById(R.id.lin_top_child);
        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        relative_top = (RelativeLayout) findViewById(R.id.relative_top);
        txt_offer = (TextView) findViewById(R.id.txt_offer);
        txt_offer_des = (TextView) findViewById(R.id.txt_offer_des);
        img_offer = (ImageView) findViewById(R.id.img_offer);
        txt_expiredate = (TextView) findViewById(R.id.txt_expiredate);
        txt_offercode = (TextView) findViewById(R.id.txt_offercode);
        txt_expiretime = (TextView) findViewById(R.id.txt_expiretime);
        txt_item = (TextView) findViewById(R.id.txt_item);
//        rel_scan=(RelativeLayout)findViewById(R.id.rel_scan);
        rel_des = (RelativeLayout) findViewById(R.id.rel_des);
        txtnodata = (TextView) findViewById(R.id.txtnodata);
        rel_nodata = (RelativeLayout) findViewById(R.id.rel_nodata);
    }

    private void init() {
        Intent intent = getIntent();
        loader = new Request_loader(this);
        mCrypt = new MCrypt();

        //get screen name from gcmintentservice(notification) or offer activity
        String screenname = intent.getStringExtra("screen");
        Log.e("screenname", screenname);
        timezoneID = TimeZone.getDefault().getID();
        Log.e("timezoneid", timezoneID);

        if (screenname.equalsIgnoreCase("notification")) {
            //get dat from notification
            notificationofferid = intent.getStringExtra("offer_id");
  //          Log.e("notification", "notification");
//            Log.e("noiitificationofferid", notificationofferid);
            notificationtype = intent.getStringExtra("notificationtype");
    //        Log.e("notificationtype", notificationtype);

            if (notificationtype.equalsIgnoreCase("Special Offer")) {
                //call special offer detail api
                new offer_detail().execute("specialoffer", notificationofferid, timezoneID);
            } else {
                //call offer detail api
                new offer_detail().execute("offer", notificationofferid, timezoneID);
            }
        } else {
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int display_width1 = size.x;
            int display_height1 = size.y;
            //get data from offer activity and set data
            img_offer.getLayoutParams().width = display_width1;
            img_offer.getLayoutParams().height = display_width1;
            img_offer.requestLayout();
            offer_name = intent.getStringExtra("offer_name");
            txt_offer.setText(offer_name);
            offer_image = intent.getStringExtra("offer_image");
            Log.e("offer_image", offer_image);
            if (!offer_image.equalsIgnoreCase("")) {
                /*Glide.with(this)
                        .load(offer_image)
                        .placeholder(R.drawable.dashboard_placeholder_img)
                        .error(R.drawable.dashboard_placeholder_img)
                        .into(img_offer);*/
                Glide.with(this)
                        .load(offer_image)
                        .into(img_offer);
            }



            offer_description = intent.getStringExtra("offer_description");
            txt_offer_des.setText(offer_description);

            offer_code = intent.getStringExtra("offer_code");
            txt_offercode.setText(offer_code);

            offer_qrcode = intent.getStringExtra("offer_qrcode");
            Log.e("offer_qrcode", offer_qrcode);
            Glide.with(this)
                    .load(offer_qrcode)
                    .into(img_barcode);
            offer_exp_date = intent.getStringExtra("offer_exp_date");
            txt_expiredate.setText(offer_exp_date);
            productname = intent.getStringExtra("productname");
            item_name = intent.getStringExtra("itemname");
            category_name = intent.getStringExtra("category_name");

            if (category_name.isEmpty()) {
                txt_item.setText(item_name);
                Log.e("itemoffer", item_name);
            } else {
                txt_item.setText(category_name);
                Log.e("categoryoffer", category_name);
            }
            // Log.e("item_name_of", ""+item_name);
            txt_expiretime.setText(productname);
        }
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;
        Log.e("Width", display_width + "==" + display_width);
        Log.e("Height", display_height + "==" + display_height);
        mScannerView.getLayoutParams().height = (int) (display_height / 2);
        mScannerView.getLayoutParams().width = (int) (display_width / 1);

    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(OfferDetailsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        OfferDetailsActivity.this.finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(OfferDetailsActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();

    }

    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("On Resume", "");
        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(OfferDetailsActivity.this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            saveToPreferences(OfferDetailsActivity.this, ALLOW_KEY, true);
                        }
                    }
//                    else if (grantResults.length > 0) {
                    else if (relative_top.getVisibility() == View.VISIBLE) {
                        lin_top.setVisibility(View.VISIBLE);
                        relative_top.setVisibility(View.GONE);
                        img_barcode.setVisibility(View.GONE);
                        rel_scan.setVisibility(View.VISIBLE);

                    } else if (relative_top.getVisibility() == View.GONE) {
                        lin_top.setVisibility(View.GONE);
                        relative_top.setVisibility(View.VISIBLE);
                        img_barcode.setVisibility(View.VISIBLE);
                        rel_scan.setVisibility(View.GONE);
                    }
//                    }
                }
            }
        }
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), "Contents = " + result.getText() + ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(OfferDetailsActivity.this);
            }
        }, 2000);
    }

    private class offer_detail extends AsyncTask<String, Void, String> {
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
                        .add("offer_id", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                        .add("Apikey", Comman_url.api_key)
                        .add("offer_type", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                        .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(params[2])))
                        .build();

                Log.e("offeridwebservicecall", params[1]);
                Log.e("Apikeywebservicecall", Comman_url.api_key);
                Log.e("notificationtypewebservicecall", params[0]);
                Log.e("timezoneidwebservicecall", params[2]);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.offer_details, reqbody);

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
                JSONObject jsonObject = new JSONObject(result);


                if (jsonObject.getString("code").equalsIgnoreCase("200")) {
                    rel_nodata.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    String offername = jsonObject1.getString("offers_title");
                    String offerdescription = jsonObject1.getString("offers_description");
                    String todate = jsonObject1.getString("to_date");
                    String totime = jsonObject1.getString("to_time");
                    String offercode = jsonObject1.getString("offer_code");
                    String offerimage = jsonObject1.getString("offer_image");
                    String offerqrcode = jsonObject1.getString("generated_QR_Code");

                    txt_offer.setText(offername);
                    txt_offer_des.setText(offerdescription);
                    txt_expiredate.setText(todate);
                    txt_expiretime.setText(totime);
                    txt_offercode.setText(offercode);

                    if (!offerimage.equalsIgnoreCase("")) {
                        /*Glide.with(OfferDetailsActivity.this)
                                .load(offerimage)
                                .placeholder(R.drawable.dashboard_placeholder_img)
                                .error(R.drawable.dashboard_placeholder_img)
                                .into(img_offer);*/
                        Glide.with(OfferDetailsActivity.this)
                                .load(offerimage)
                                .into(img_offer);
                    }
                    if (!offerqrcode.equalsIgnoreCase("")) {
                        Glide.with(OfferDetailsActivity.this)
                                .load(offerqrcode)
                                .into(img_barcode);
                    }

                    JSONArray jsonArray_menudetail = jsonObject1.getJSONArray("menu_details");
                    String category_name = jsonObject1.getString("categories_ids");
                    Log.e("categoryname", category_name);

                    if (category_name.isEmpty()) {
                        menu_name = "";
                        for (int k = 0; k < jsonArray_menudetail.length(); k++) {
                            menu_nombre = "";
                            JSONObject jobj_menudetail = jsonArray_menudetail.getJSONObject(k);
                            menu_nombre = jobj_menudetail.getString("first_name");
                            if (menu_name.equalsIgnoreCase("")) {
                                menu_name = menu_nombre;
                                Log.e("menunamee", menu_name);
                            } else {
                                menu_name = menu_name + ", " + menu_nombre;
                                Log.e("menunameelse", menu_name);
                            }
                        }
                        txt_item.setText(menu_name);
                        Log.e("itemoffer", menu_name);
                    } else {
                        txt_item.setText(category_name);
                        Log.e("categoryoffer", category_name);
                    }
                } else {
                    rel_nodata.setVisibility(View.VISIBLE);
                    rel_des.setVisibility(View.GONE);
                    //txtnodata.setText(jsonObject.getString("message"));
                    txt_offer.setText(jsonObject.getString("message"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            loader.hidepDialog();
        }
    }

    /*private class special_offer_detail extends AsyncTask<String, Void, String> {
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
                        .add("special_offer_id", MCrypt.bytesToHex(mCrypt.encrypt(notificationofferid)))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("offerid", notificationofferid);
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Special_offer_Detail, reqbody);

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
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                String offername = jsonObject1.getString("offers_title");
                String offerdescription = jsonObject1.getString("offers_description");
                String todate = jsonObject1.getString("to_date");
                String totime = jsonObject1.getString("to_time");
                String offercode = jsonObject1.getString("offer_code");
                String offerimage = jsonObject1.getString("offer_image");
                String offerqrcode = jsonObject1.getString("generated_QR_Code");

                txt_offer.setText(offername);
                txt_offer_des.setText(offerdescription);
                txt_expiredate.setText(todate);
                txt_expiretime.setText(totime);
                txt_offercode.setText(offercode);

                if (!offerimage.equalsIgnoreCase("")) {
                    Glide.with(OfferDetailsActivity.this)
                            .load(offerimage)
                            .into(img_offer);
                }
                if (!offerqrcode.equalsIgnoreCase("")){
                    Glide.with(OfferDetailsActivity.this)
                            .load(offerqrcode)
                            .into(img_barcode);
                }

                JSONArray jsonArray_menudetail = jsonObject1.getJSONArray("menu_details");
                String category_name = jsonObject1.getString("categories_ids");
                Log.e("categoryname",category_name);

                if (category_name.isEmpty())
                {
                    menu_name = "";
                    for (int k=0; k<jsonArray_menudetail.length();k++)
                    {
                        menu_nombre = "";
                        JSONObject jobj_menudetail = jsonArray_menudetail.getJSONObject(k);
                        menu_nombre = jobj_menudetail.getString("nombre");
                        if (menu_name.equalsIgnoreCase("")) {
                            menu_name = menu_nombre;
                            Log.e("menunamee",menu_name);
                        } else {
                            menu_name = menu_name + ", " + menu_nombre;
                            Log.e("menunameelse",menu_name);
                        }
                    }
                    txt_item.setText(menu_name);
                    Log.e("itemoffer",menu_name);
                }
                else
                {
                    txt_item.setText(category_name);
                    Log.e("categoryoffer",category_name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            loader.hidepDialog();
        }
    }*/
}

