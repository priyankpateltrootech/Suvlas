package com.suvlas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brandongogetap.stickyheaders.StickyLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adpter.RecyclerAdapter;
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

public class MenuFragment extends Fragment {

    ArrayList<MenuOrderItem> menu_item = new ArrayList<>();
    ArrayList<String> CategoryArray = new ArrayList<>();
    RecyclerView mRecyclerView;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    SharedPrefs sharedPrefs;
    StickyLayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    String lastheader;
    View view;
    Button btn_menu_order;
    String selected_restaurant_name,selected_restaurant_id;

    public MenuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String restaurant_name,String restaurant_id) {
        MenuFragment fragment = new MenuFragment();
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

        view = inflater.inflate(R.layout.fragment_menu, container, false);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
//        //set_font();

        return view;
    }

    private void findviewID()
    {
        btn_menu_order = (Button)view.findViewById(R.id.btn_menu_order);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void set_listeners()
    {
        btn_menu_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),OrderResumeActivity.class);
                i.putExtra("restaurant_name",selected_restaurant_name);
                i.putExtra("restaurant_id",selected_restaurant_id);
                startActivity(i);
            }
        });
    }
    private void init()
    {
        loader = new Request_loader(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        comman_dialog = new Comman_Dialog(getActivity());

        if (CommanMethod.isInternetOn(getActivity())) {
            menu_item = new ArrayList<>();
            //call get menu api
            new Get_menu_Item().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }

        Bundle b = getArguments();
        selected_restaurant_name = b.getString("restaurant_name");
        selected_restaurant_id = b.getString("restaurant_id");
//        Log.e("param",selected_restaurant_name);
    }

    public class Get_menu_Item extends AsyncTask<String, Void, String> {

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
                Responce_jason = CallingMethod.GET_Menu(client, Comman_api_name.Get_menu_item,"bd_suvlasmarbellapos", getActivity());

            } catch (Exception e) {
                Log.e("errrrrr_menu", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_menu", result + "");

            try {
                if (result != null) {
                    JSONObject main_obj = new JSONObject(result);

                    JSONArray data_array = main_obj.getJSONArray("data");
                    Log.e("data_array", String.valueOf(data_array));

                    for (int j = 0; j < data_array.length(); j++) {

                        JSONObject jsonObject = data_array.getJSONObject(j);
                        JSONObject jsonObject_cxategory = jsonObject.getJSONObject("categoria");

                        //String category_name = jsonObject.getString("first_name");
                        String item_img = jsonObject.getString("imagen");
                        String menu_id = jsonObject.getString("idmenu");
                        String code = jsonObject.getString("codigo");
                        String cost = jsonObject.getString("precioSugerido");
                        String tax = jsonObject.getString("impuesto");

                        String categori = jsonObject_cxategory.getString("nombremenu");
                        String category_id = jsonObject_cxategory.getString("idcategoriamenu");
                        String category_tax = jsonObject_cxategory.getString("impuesto");
                        String category_code = jsonObject_cxategory.getString("codigo");
                        String category_order = jsonObject_cxategory.getString("orden");



                        if (!CategoryArray.contains(categori)) {
                            CategoryArray.add(categori);
                            //CategoryBeanArray.add(new MenuOrderItem("",categori, "","", MenuOrderItem.HEADER_TYPE));
                        }
                    }

                    for (int j = 0; j < CategoryArray.size(); j++) {
                        //menu_item.add(CategoryBeanArray.get(j));

                        for (int k = 0; k < data_array.length(); k++) {
                            JSONObject jsonObject = data_array.getJSONObject(k);
                            JSONObject jsonObject_cxategory = jsonObject.getJSONObject("categoria");

                            String item_name = jsonObject.getString("nombre");
                            String item_img = jsonObject.getString("imagen");
                            String cost = jsonObject.getString("precioSugerido");
                            String categori = jsonObject_cxategory.getString("nombremenu");
                            String menu_id = jsonObject.getString("idmenu");
                            String category_id = jsonObject_cxategory.getString("idcategoriamenu");
                            //Log.e("category_id",category_id);

                            Collections.sort(CategoryArray);

                            if (CategoryArray.get(j).equalsIgnoreCase(categori)) {

                                if (lastheader != null)
                                {
                                    if (lastheader.equalsIgnoreCase(categori))
                                    {
                                        menu_item.add(new MenuOrderItem(category_id,menu_id,item_img, item_name, "", cost,MenuOrderItem.ITEM_TYPE));
                                    }
                                    else
                                    {
                                        menu_item.add(new HeaderItem(categori,""));
                                        menu_item.add(new MenuOrderItem(category_id,menu_id,item_img, item_name, "", cost,MenuOrderItem.ITEM_TYPE));
                                    }
                                }
                                else
                                {
                                    menu_item.add(new HeaderItem(categori,""));
                                    menu_item.add(new MenuOrderItem(category_id,menu_id,item_img, item_name, "", cost,MenuOrderItem.ITEM_TYPE));
                                }


                                lastheader = CategoryArray.get(j);
                            }

//                            Log.e("lastheaderstore",lastheader);
                        }

                    }

                    recyclerAdapter = new RecyclerAdapter(menu_item, getActivity());

                    //recyclerview bind with menuorder adapoter
                    /*adapter = new MenuOrderAdapter(menu_item, MenuOrder.this);*/
                    layoutManager = new StickyLayoutManager(getActivity(), recyclerAdapter);
                    layoutManager.elevateHeaders(true);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(recyclerAdapter);
                    //adapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                Log.e("exception_news", e.toString());
            }
            loader.hidepDialog();
        }

    }

}
