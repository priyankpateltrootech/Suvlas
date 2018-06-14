package com.suvlas;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.support.design.widget.TabLayout;

import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adpter.MenuOrderAdapter;
import adpter.RecyclerAdapter;
import adpter.ViewPagerAdapter;
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

/**
 * Created by hp on 5/1/2017.
 */

public class MenuOrder extends AppCompatActivity {


    ArrayList<MenuOrderItem> menu_item = new ArrayList<>();
    ArrayList<String> CategoryArray = new ArrayList<>();
    ImageView img_back;
    RecyclerView mRecyclerView;
    MenuOrderAdapter adapter;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    SharedPrefs sharedPrefs;
    String categoryName = "";
    ArrayList<MenuOrderItem> CategoryBeanArray = new ArrayList<>();
    StickyLayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    String lastheader;
    TabLayout tablayout_menuorder;
    ViewPager viewpager_menuorder;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuorder);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
//        //set_font();

        //initialize component
        init();


    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuOrder.this, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }


    private void init() {
        loader = new Request_loader(MenuOrder.this);
        sharedPrefs = new SharedPrefs(MenuOrder.this);
        comman_dialog = new Comman_Dialog(MenuOrder.this);

        tablayout_menuorder = (TabLayout)findViewById(R.id.tablayout_menuorder);
        viewpager_menuorder = (ViewPager)findViewById(R.id.viewpager_menuorder);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getIntent().getStringExtra("restaurant_name"),getIntent().getStringExtra("restaurant_id"));
        viewpager_menuorder.setAdapter(viewPagerAdapter);
        tablayout_menuorder.setupWithViewPager(viewpager_menuorder);

        for (int i = 0 ; i<tablayout_menuorder.getTabCount() ; i++)
        {
            TabLayout.Tab tab = tablayout_menuorder.getTabAt(i);

            if (tab != null)
            {
                TextView textView = new TextView(this);
                tab.setCustomView(textView);

                textView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                textView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                textView.setText(tab.getText());
                textView.setTextColor(Color.BLACK);

                if (i == 0) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setGravity(Gravity.RIGHT);
                }
            }
        }

        tablayout_menuorder.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(null, Typeface.BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();

                text.setTypeface(null, Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (CommanMethod.isInternetOn(MenuOrder.this)) {
            menu_item = new ArrayList<>();
            //call get menu api
            new Get_menu_Item().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }

//        adapter = new MenuOrderAdapter(menu_item, MenuOrder.this);
//        mRecyclerView.setAdapter(adapter);
////                        adapter.notifyDataSetChanged();
//        menu_item = new ArrayList<>();
//        adapter = new MenuOrderAdapter(menu_item, MenuOrder.this);
//        mRecyclerView.setAdapter(adapter);

    }

    private void set_listeners() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                //Responce_jason = CallingMethod.GET_Menu(client, Comman_api_name.Get_menu_item, MenuOrder.this);

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

                    recyclerAdapter = new RecyclerAdapter(menu_item, MenuOrder.this);

                    //recyclerview bind with menuorder adapoter
                    /*adapter = new MenuOrderAdapter(menu_item, MenuOrder.this);*/
                    layoutManager = new StickyLayoutManager(MenuOrder.this, recyclerAdapter);
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

