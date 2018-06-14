package com.suvlas;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adpter.AddCardAdapter;
import adpter.CustomMenuAdapter;
import bean.CategoryMenu;
import bean.CategoryMenuModifier;
import bean.MenuCombo;
import bean.MenuComboModifier;
import bean.Menucombomodifiercart;
import bean.OrderMenuModifier;
import bean.OrderModifier;
import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.OkHttpClient;

public class AddtoCartActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    MCrypt mCrypt;
    RecyclerView recycler_add_cart;
    AddCardAdapter addCardAdapter;
    ArrayList<Menucombomodifiercart> menucombomodifiercart;
    String selected_restaurant_name,selected_restaurant_id,restaurant_api_key,category_id,category_name,category_item_id,category_item_name,category_item_price,category_item_image;
    ImageView img_back;
    ArrayList<String> modifieridlist;
    ArrayList<OrderMenuModifier> orderMenuModifierArrayList;
    public static String recyclerscroll="start";
    ArrayList<OrderModifier> orderModifierArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
    }

    private void findviewID()
    {
        recycler_add_cart = (RecyclerView)findViewById(R.id.recycler_add_cart);
        img_back = (ImageView)findViewById(R.id.img_back);
    }
    private void init()
    {

        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);
        loader = new Request_loader(this);
        mCrypt = new MCrypt();

        modifieridlist = getIntent().getStringArrayListExtra("modifierlist");
        selected_restaurant_id = getIntent().getStringExtra("restaurant_id");
        selected_restaurant_name = getIntent().getStringExtra("restaurant_name");
        restaurant_api_key = getIntent().getStringExtra("restaurantapikey");
        category_id = getIntent().getStringExtra("category_id");
        category_name = getIntent().getStringExtra("category_name");
        category_item_id = getIntent().getStringExtra("categoryitemid");
        category_item_name = getIntent().getStringExtra("categoryitemname");
        category_item_price = getIntent().getStringExtra("categoryitemprice");


        String name = getIntent().getStringExtra("categoryitemname");
        //category_item_image = getIntent().getStringExtra("categoryitemimage");
        category_item_image = getIntent().getStringExtra("imageurl");

        orderMenuModifierArrayList = new ArrayList<>();
        orderModifierArrayList = new ArrayList<>();
        orderMenuModifierArrayList.add(0,new OrderMenuModifier(category_item_image,name,"","",orderModifierArrayList));

        new Get_menu_modifier().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        linearLayoutManager = new LinearLayoutManager(this);

        recycler_add_cart.setLayoutManager(linearLayoutManager);

        ScrollActivity();

    }
    private void set_listeners()
    {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void ScrollActivity()
    {

        recycler_add_cart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy>0){
                    recyclerscroll= "scrollup";
                    //Log.e("scrollup","scrollup");
                }
                else{
                    recyclerscroll="scrolldown";
                    //Log.e("scrolldown","scrolldown");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("resume","resume");
    }

    public class Get_menu_modifier extends AsyncTask<String, Void, String> {

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
                Responce_jason = CallingMethod.GET_Menu(client, Comman_api_name.Get_menu_modifier,restaurant_api_key, AddtoCartActivity.this);

            } catch (Exception e) {
                Log.e("errrrrr_menu_modifier", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null)
            {
                Log.e("result_menu_modifier", result + "");

                try {
                    JSONObject jsoncomboobject = new JSONObject(result);

                    JSONArray jsoncomboarray = jsoncomboobject.getJSONArray("data");

                    for (int i = 0 ; i < jsoncomboarray.length();i++) {
                        JSONObject jsomcombodataobject = jsoncomboarray.getJSONObject(i);

                        String combo_id = jsomcombodataobject.getString("idmodificador");

                        JSONArray jsonmodifierArray = jsomcombodataobject.getJSONArray("modificadorDetalle");

                        for (int j = 0 ; j < modifieridlist.size();j++)
                        {
                            if (combo_id.equalsIgnoreCase(modifieridlist.get(j)))
                            {
                                String modifire_combo_name = jsomcombodataobject.getString("nombre");
                                String minimium_quantity = jsomcombodataobject.getString("min");
                                //Log.e("combo_name",combo_name);
                                if (jsoncomboarray.length()>0)
                                {
                                    orderModifierArrayList = new ArrayList<>();

                                    for (int k = 0 ; k < jsonmodifierArray.length();k++)
                                    {
                                        JSONObject jsonmodifierObject = jsonmodifierArray.getJSONObject(k);

                                        String modifier_name = jsonmodifierObject.getString("nombre");
                                        //Log.e("modifier_name",modifier_name);
                                        String modifier_cost = jsonmodifierObject.getString("costo");
                                        String modifier_id = jsonmodifierObject.getString("id");

                                        orderModifierArrayList.add(new OrderModifier(modifier_name,modifier_cost,modifier_id,""));
                                    }

                                    orderMenuModifierArrayList.add(new OrderMenuModifier("","",modifire_combo_name,minimium_quantity,orderModifierArrayList));
                                    //Log.e("ordermodifier",orderModifierArrayList.size()+"");
                                    /*addCardAdapter = new AddCardAdapter(AddtoCartActivity.this,selected_restaurant_id,selected_restaurant_name,restaurant_api_key,category_id,category_name,category_item_id,category_item_name,category_item_price,orderMenuModifierArrayList);*/
                                    /*recycler_add_cart.setAdapter(addCardAdapter);*/
                                }
                            }
                        }
                        addCardAdapter = new AddCardAdapter(AddtoCartActivity.this,selected_restaurant_id,selected_restaurant_name,restaurant_api_key,category_id,category_name,category_item_id,category_item_name,category_item_price,category_item_image,orderMenuModifierArrayList);
                        recycler_add_cart.setAdapter(addCardAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            loader.hidepDialog();
        }

    }

}
