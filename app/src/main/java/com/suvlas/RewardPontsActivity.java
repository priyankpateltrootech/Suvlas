package com.suvlas;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adpter.OrderHistoryRecyclerviewAdapter;
import bean.ItemOrderHistory;
import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 3/10/2017.
 */

public class RewardPontsActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;

    static {
        System.loadLibrary("iconv");
    }

    ImageView img_back, img_back2, img_back_child, img_barcode;
    LinearLayout lin_scn_btn, lin_scn_code, lin_top;
    TextView txt_barcode_num;
    ViewGroup contentFrame;
    RelativeLayout rel_lay, relative_top;
    private ZXingScannerView mScannerView;

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
        setContentView(R.layout.rewardpage);

        // initialize all component
        findviewID();

        //initialize component
        //set_font();
        init();

        // onclick listener method for required components
        set_listeners();
    }

    private void findviewID() {

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back2 = (ImageView) findViewById(R.id.img_back2);
//        img_back_child = (ImageView) findViewById(R.id.img_back_child);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);
        txt_barcode_num = (TextView) findViewById(R.id.txt_barcode_num);
        lin_scn_btn = (LinearLayout) findViewById(R.id.lin_scan);
        lin_top = (LinearLayout) findViewById(R.id.lin_top);
        lin_scn_code = (LinearLayout) findViewById(R.id.lin_top_child);
        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        relative_top = (RelativeLayout) findViewById(R.id.relative_top);
    }

    private void set_listeners() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        lin_scn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(RewardPontsActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RewardPontsActivity.this, android.Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(RewardPontsActivity.this, new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(RewardPontsActivity.this, new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else {
                    if (lin_top.getVisibility() == View.VISIBLE) {
                        mScannerView.startCamera();
                        lin_top.setVisibility(View.GONE);
                        lin_scn_code.setVisibility(View.VISIBLE);
                        relative_top.setVisibility(View.GONE);
                        img_barcode.setVisibility(View.GONE);
                        txt_barcode_num.setVisibility(View.GONE);
                    } else {
//                        mScannerView.stopCamera();
                        lin_top.setVisibility(View.VISIBLE);
                        lin_scn_code.setVisibility(View.GONE);
                        relative_top.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        img_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
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

        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(this);
        loader = new Request_loader(this);
        sharedPrefs = new SharedPrefs(this);
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(RewardPontsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RewardPontsActivity.this.finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(RewardPontsActivity.this,
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
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(RewardPontsActivity.this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            saveToPreferences(RewardPontsActivity.this, ALLOW_KEY, true);
                        }
                    } else if (grantResults.length > 0) {
                        if (lin_top.getVisibility() == View.VISIBLE) {
                            lin_top.setVisibility(View.GONE);
                            lin_scn_code.setVisibility(View.VISIBLE);
                            relative_top.setVisibility(View.GONE);
                            img_barcode.setVisibility(View.GONE);
                            txt_barcode_num.setVisibility(View.GONE);
                        } else {
                            lin_top.setVisibility(View.VISIBLE);
                            lin_scn_code.setVisibility(View.GONE);
                            relative_top.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }



    @Override
    public void handleResult(Result result) {
        //Toast.makeText(getApplicationContext(), "Contents = " + result.getText() + ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        new scan_barcode(result.getText()).execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(RewardPontsActivity.this);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(RewardPontsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);*/
    }

    private class scan_barcode extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String barcode_code;

        public scan_barcode(String barcode_code) {
            this.barcode_code = barcode_code;
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

                Log.e("user_id", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("barcode", MCrypt.bytesToHex(mCrypt.encrypt(barcode_code)))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.scanrewardspoint, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_scanbarcode", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_scanbarcode", result + "");

            try {
                if (result != null) {

                    JSONObject jsonmainObject = new JSONObject(result);

                    String code = jsonmainObject.getString("message");

                    lin_top.setVisibility(View.VISIBLE);
                    lin_scn_code.setVisibility(View.GONE);
                    relative_top.setVisibility(View.VISIBLE);

                    if (code.equalsIgnoreCase("success"))
                    {
                        JSONObject jsondataObject = jsonmainObject.getJSONObject("Orders");
                        comman_dialog.Show_alert("Felicidades"+" "+jsondataObject.getString("users_point")+" "+"puntos fueron añadidos a su cuenta.");
                       // Toast.makeText(getBaseContext(),jsondataObject.getString("users_point"),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        comman_dialog.Show_alert(code);
                    }
                }
            } catch (Exception e) {
                Log.e("exception_scanbarcode", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}
