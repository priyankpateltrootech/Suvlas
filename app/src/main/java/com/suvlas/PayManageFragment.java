package com.suvlas;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 3/7/2017.
 */

public class PayManageFragment extends Fragment implements View.OnClickListener {

    Button btn_transfer,btn_reload,btn_refresh;
    View rootView;
    TabLayout tab;
    TextView text_status,txt_wallet_balance;
    RelativeLayout rel_top;
    ImageView img_back,img_msg,img_card;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    MCrypt mCrypt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pay_managepage, container, false);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
        //set_font();


        return rootView;
    }
    private void findviewID() {

        btn_reload=(Button)rootView.findViewById(R.id.btn_reload);
        btn_transfer=(Button)rootView.findViewById(R.id.btn_transfer);
        btn_refresh = (Button)rootView.findViewById(R.id.btn_refresh);
//        tab=(TabLayout)getActivity().findViewById(R.id.tabs);
        text_status=(TextView)getActivity().findViewById(R.id.txt_status);
        rel_top=(RelativeLayout)getActivity().findViewById(R.id.rel_top);
        img_back=(ImageView)rootView.findViewById(R.id.img_back_paymanage);
        img_msg=(ImageView)getActivity().findViewById(R.id.img_msg);
        img_card=(ImageView)rootView.findViewById(R.id.img_card);
        txt_wallet_balance = (TextView)rootView.findViewById(R.id.txt_wallet_balance);
    }
    private void set_listeners() {
        btn_reload.setOnClickListener(this);
//        img_card.setOnClickListener(this);
        btn_transfer.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
    }
    private void init() {


        loader = new Request_loader(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        comman_dialog = new Comman_Dialog(getActivity());
        mCrypt = new MCrypt();

        new reload_card().execute();
//        tab.setBackgroundColor(getResources().getColor(R.color.white));
//        text_status.setText(R.string.manage);
//        rel_top.setBackgroundColor(getResources().getColor(R.color.white));
//        Point size = new Point();
//        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
//        int display_width = size.x;
//        int display_height = size.y;
//        Log.e("Width", display_width + "==" + display_width);
//        Log.e("Height", display_height + "==" + display_height);
//        img_card.getLayoutParams().height = (int) (display_height /3.2);
//        img_back.setBackgroundResource(R.drawable.back);
//        img_msg.setBackgroundResource(R.color.white);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_reload:
                Intent intent3 = new Intent(getActivity(), PayReloadActivity.class);
                startActivity(intent3);
                break;

            case R.id.btn_transfer:
                Intent intent2 = new Intent(getActivity(), TransferBalance.class);
                startActivity(intent2);

                break;
            case R.id.img_back_paymanage:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
            case R.id.btn_refresh:
                new reload_card().execute();
                break;
        }
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
                        /*Glide.with(getActivity()).load(reload_card_object.getString("card")).placeholder(R.drawable.loading).error(R.drawable.loading).into(img_card);*/
                        Glide.with(getActivity()).load(reload_card_object.getString("card")).into(img_card);
                        JSONObject wallet_object = reload_card_object.getJSONObject("data");

                        txt_wallet_balance.setText(wallet_object.getString("wallet_amount"));
                    }
                    else
                    {
                        Toast.makeText(getActivity(),reload_card_object.getString("message"),Toast.LENGTH_SHORT).show();
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