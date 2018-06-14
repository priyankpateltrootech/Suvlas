package com.suvlas;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adpter.CustomMenuAdapter;
import adpter.RecyclerAdapter;
import bean.MenuCombo;
import bean.MenuComboModifier;
import bean.MenuOrderItem;
import common.CallingMethod;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_message;
import common.HeaderItem;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.OkHttpClient;

public class CustomMenuFragment extends Fragment {

    CustomMenuAdapter customMenuAdapter;
    RecyclerView custom_menu_recyclerview;
    View view;
    GridLayoutManager gridLayoutManager;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    SharedPrefs sharedPrefs;
    ArrayList<MenuCombo> menu_combo;
    ArrayList<MenuComboModifier> menu_combo_modifier;
    String selected_restaurant_name,selected_restaurant_id;

    public CustomMenuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CustomMenuFragment newInstance(String restaurant_name,String restaurant_id) {
        CustomMenuFragment fragment = new CustomMenuFragment();
        Bundle args = new Bundle();
        args.putString("restaurant_name", restaurant_name);
        args.putString("restaurant_id", restaurant_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_custom_menu, container, false);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();

        if (CommanMethod.isInternetOn(getActivity())) {

            menu_combo = new ArrayList<>();
            menu_combo_modifier = new ArrayList<>();
            new Get_combo_menu().execute();
        }
        else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }


        return view;
    }

    private void findviewID()
    {
        custom_menu_recyclerview = (RecyclerView)view.findViewById(R.id.custom_menu_recyclerview);
    }

    private void init()
    {
        loader = new Request_loader(getActivity());
        comman_dialog = new Comman_Dialog(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());


        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        custom_menu_recyclerview.setLayoutManager(gridLayoutManager);

        Bundle b = getArguments();
        selected_restaurant_name = b.getString("restaurant_name");
        //Log.e("param",selected_restaurant_name);
        selected_restaurant_id = b.getString("restaurant_id");
    }
    private void set_listeners()
    {

    }

    public class Get_combo_menu extends AsyncTask<String, Void, String> {

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
                //Responce_jason = CallingMethod.GET_Menu(client, Comman_api_name.Get_menu_modifier, getActivity());

            } catch (Exception e) {
                Log.e("errrrrr_combo_menu", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null)
            {
                Log.e("result_combo_menu", result + "");

                try {
                    JSONObject jsoncomboobject = new JSONObject(result);

                    JSONArray jsoncomboarray = jsoncomboobject.getJSONArray("data");

                    for (int i = 0 ; i < jsoncomboarray.length();i++)
                    {
                        JSONObject jsomcombodataobject = jsoncomboarray.getJSONObject(i);
                        String combo_name = jsomcombodataobject.getString("nombre");
                        String combo_id = jsomcombodataobject.getString("idmodificador");

                        JSONArray jsoncombodataarray = jsomcombodataobject.getJSONArray("modificadorDetalle");

                       // menu_combo_modifier = new ArrayList<>();

                        if (jsoncombodataarray.length()>0)
                        {
                            menu_combo_modifier = new ArrayList<>();
                            for (int j = 0 ; j <jsoncombodataarray.length();j++)
                            {
                                JSONObject jsoncombomodiiferobject = jsoncombodataarray.getJSONObject(j);

                                String combo_modifier_id = jsoncombomodiiferobject.getString("id");
                                String combo_modifier_name = jsoncombomodiiferobject.getString("nombre");
                                String combo_modifier_cost = jsoncombomodiiferobject.getString("costo");

                                menu_combo_modifier.add(new MenuComboModifier(combo_modifier_id,combo_modifier_name,combo_modifier_cost));
                            }
                        }

                        menu_combo.add(new MenuCombo(combo_id,combo_name,menu_combo_modifier));
                    }

                    customMenuAdapter = new CustomMenuAdapter(getActivity(),menu_combo,selected_restaurant_name,selected_restaurant_id);
                    custom_menu_recyclerview.setAdapter(customMenuAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            loader.hidepDialog();
        }

    }
}
