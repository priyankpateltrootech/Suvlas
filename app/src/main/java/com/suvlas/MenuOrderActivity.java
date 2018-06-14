package com.suvlas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import adpter.MenuCategoryAdapter;
import adpter.MenuOrderCategoryItemAdapter;
import adpter.RecyclerAdapter;
import bean.MenuOrderCategory;
import bean.MenuOrderCategoryItem;
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

public class MenuOrderActivity extends AppCompatActivity {

    Request_loader loader;
    Comman_Dialog comman_dialog;
    SharedPrefs sharedPrefs;
    ArrayList<String> CategoryArray;
    RecyclerView recyclerview_category_menuorder,recyclerview_category_item_menuorder;
    MenuCategoryAdapter menuCategoryAdapter;
    ArrayList<MenuOrderCategoryItem> menuOrderCategoryItemArrayList;
    MenuOrderCategoryItemAdapter menuOrderCategoryItemAdapter;
    LinearLayout linear_menucategory;
    ArrayList<String> modifieridlist;
    String restaurant_id,restaurant_name,restaurant_api_key;
    ArrayList<MenuOrderCategory> menuOrderCategoryArrayList;
    ImageView img_back_order,img_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
//        //set_font();

        //initialize component
        init();
    }

    private void findviewID()
    {
        recyclerview_category_menuorder = (RecyclerView)findViewById(R.id.recyclerview_category_menuorder);
        recyclerview_category_item_menuorder = (RecyclerView)findViewById(R.id.recyclerview_category_item_menuorder);

        linear_menucategory = (LinearLayout)findViewById(R.id.linear_menucategory);

        img_back_order = (ImageView)findViewById(R.id.img_back_order);

        img_menu = (ImageView)findViewById(R.id.img_menu);

        /*Glide.with(this).load("http://admin.invupos.com/invuPos/images/banner/chicken%20pita.jpg").asBitmap().override(500, 500)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        Log.e("e",e+"");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {

                        return false;
                    }
                }).into(img_menu);*/
    }

    private void init()
    {
        loader = new Request_loader(MenuOrderActivity.this);
        sharedPrefs = new SharedPrefs(MenuOrderActivity.this);
        comman_dialog = new Comman_Dialog(MenuOrderActivity.this);

        recyclerview_category_menuorder.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_category_item_menuorder.setLayoutManager(new GridLayoutManager(this,2));

        CategoryArray = new ArrayList<>();
        menuOrderCategoryArrayList = new ArrayList<>();

        restaurant_id = getIntent().getStringExtra("restaurant_id");
        restaurant_name = getIntent().getStringExtra("restaurant_name");
        restaurant_api_key = getIntent().getStringExtra("restaurant_apikey");

        if (CommanMethod.isInternetOn(MenuOrderActivity.this)) {

            new Get_menu_Item().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }

    }

    private void set_listeners()
    {
        img_back_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.MINUTES)
                        .writeTimeout(30, TimeUnit.MINUTES)
                        .readTimeout(30, TimeUnit.MINUTES)
                        .build();
                Responce_jason = CallingMethod.GET_Menu(client, Comman_api_name.Get_menu_item,restaurant_api_key,MenuOrderActivity.this);

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

                    final JSONArray data_array = main_obj.getJSONArray("data");
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
                            menuOrderCategoryArrayList.add(new MenuOrderCategory(category_id,categori));
                            //CategoryBeanArray.add(new MenuOrderItem("",categori, "","", MenuOrderItem.HEADER_TYPE));
                        }
                    }

                    Collections.sort(CategoryArray);
                    menuCategoryAdapter = new MenuCategoryAdapter(MenuOrderActivity.this, CategoryArray, new OnClickListener() {
                        @Override
                        public void onItemClicked(int position) {

                            linear_menucategory.setVisibility(View.GONE);
                            recyclerview_category_menuorder.setVisibility(View.GONE);
                            recyclerview_category_item_menuorder.setVisibility(View.VISIBLE);

                            menuOrderCategoryItemArrayList = new ArrayList<>();

                                for (int k = 0; k < data_array.length(); k++) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = data_array.getJSONObject(k);
                                        JSONObject jsonObject_cxategory = jsonObject.getJSONObject("categoria");

                                        String item_id = jsonObject.getString("idmenu");
                                        String item_name = jsonObject.getString("nombre");
                                        String item_img = jsonObject.getString("imagen");
                                        String cost = jsonObject.getString("precioSugerido");
                                        final String categori = jsonObject_cxategory.getString("nombremenu");
                                        String menu_id = jsonObject.getString("idmenu");
                                        final String category_id = jsonObject_cxategory.getString("idcategoriamenu");

                                        JSONArray jsonmodifierArray = jsonObject.getJSONArray("modificadores");
                                        //Log.e("jsonmodifierArray",jsonmodifierArray.toString());

                                        if (CategoryArray.get(position).equalsIgnoreCase(categori)) {

                                            //Log.e("sadasdcate",categori);
                                            //Log.e("sadasdcate",category_id);
                                            modifieridlist = new ArrayList<>();
                                            for (int i = 0 ; i< jsonmodifierArray.length();i++)
                                            {
                                                JSONObject jsonmodifierObject = jsonmodifierArray.getJSONObject(i);

                                                String modifierid = jsonmodifierObject.getString("idmodificador");
                                                //Log.e("modifierid",modifierid);
                                                modifieridlist.add(modifierid);
                                            }
                                            //Log.e("modifierid",modifieridlist.size()+"");
                                            menuOrderCategoryItemArrayList.add(new MenuOrderCategoryItem(item_id,item_name,cost,item_img,modifieridlist));

                                            menuOrderCategoryItemAdapter = new MenuOrderCategoryItemAdapter(MenuOrderActivity.this, menuOrderCategoryItemArrayList, new OnClickListenerMenu() {
                                                @Override
                                                public void onItemClicked(int position,String imageurl) {

                                                    Intent i = new Intent(MenuOrderActivity.this,AddtoCartActivity.class);
                                                    i.putExtra("restaurant_id",restaurant_id);
                                                    i.putExtra("imageurl",imageurl);
                                                    i.putExtra("restaurant_name",restaurant_name);
                                                    i.putExtra("categoryitemname",menuOrderCategoryItemArrayList.get(position).getCategory_item_name());
                                                    i.putExtra("categoryitemid",menuOrderCategoryItemArrayList.get(position).getCategory_item_id());
                                                    i.putExtra("categoryitemimage",menuOrderCategoryItemArrayList.get(position).getCategory_item_image());
                                                    i.putExtra("categoryitemprice",menuOrderCategoryItemArrayList.get(position).getCategory_item_price());
                                                    i.putStringArrayListExtra("modifierlist",menuOrderCategoryItemArrayList.get(position).getModifierlist());
                                                    i.putExtra("restaurantapikey",restaurant_api_key);
                                                    i.putExtra("category_id",category_id);
                                                    i.putExtra("category_name",categori);
                                                    startActivity(i);
                                                }
                                            });

                                            recyclerview_category_item_menuorder.setAdapter(menuOrderCategoryItemAdapter);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                    });
                    recyclerview_category_menuorder.setAdapter(menuCategoryAdapter);


                }
            } catch (JSONException e) {
                Log.e("exception_news", e.toString());
            }
            loader.hidepDialog();
        }

    }

    @Override
    public void onBackPressed() {

        if (recyclerview_category_item_menuorder.getVisibility() == View.VISIBLE)
        {
            linear_menucategory.setVisibility(View.VISIBLE);
            recyclerview_category_menuorder.setVisibility(View.VISIBLE);
            recyclerview_category_item_menuorder.setVisibility(View.GONE);
        }
        else
        {
            super.onBackPressed();
            /*Intent i = new Intent(MenuOrderActivity.this,OrderActivity.class);
            startActivity(i);*/
        }
    }
}
