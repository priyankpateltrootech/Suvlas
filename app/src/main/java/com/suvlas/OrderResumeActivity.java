package com.suvlas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import adpter.ExpandListAdapter;
import bean.CategoryMenu;
import bean.CategoryMenuModifier;
import bean.ComboMenu;
import bean.ComboModifier;
import bean.ExpandableOrderChild;
import bean.ExpandableOrderGroup;
import bean.Menu;
import bean.MenuModifier;
import bean.MenuOrderBean;
import bean.OfferOrder;
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
public class OrderResumeActivity extends AppCompatActivity {

    Comman_Dialog comman_dialog;
    Request_loader loader;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    public static ExpandableListView expandable_listview;
    ExpandableListAdapter expandableListAdapter;
    private LinearLayout footerLayout;
    ArrayList<MenuOrderBean> menuOrderBeen_shared_pre_array = new ArrayList<>();
    Gson gson;
    ArrayList[] list;
    ArrayList<Double> price;
    Double Totalsum = 0.00;
    TextView resume_order_txt_totalprice, order_resume_restaurant_name, resume_order_txt_subtotal, resume_order_txt_discount,
            resume_order_txt_discount_label, resume_order_txt_itbms_label, resume_order_txt_itbms,resume_order_txt_wallet;
    Button btn_order_place, btn_order_offer_apply,btn_redeem_offer_apply;
    String TotalAmount;
    ImageView img_back_order_resume;
    private ExpandListAdapter ExpAdapter;
    ArrayList<ExpandableOrderChild> expandableChild_list;
    ArrayList<ExpandableOrderGroup> list1;
    ArrayList<ComboMenu> combo_menu_shared_pre_array = new ArrayList<>();
    ArrayList<ComboModifier> combo_modifier_shared_pre_array = new ArrayList<>();
    ArrayList<Menu> menu_group_shared_pre_array = new ArrayList<>();
    ArrayList<MenuModifier> menu_modifier_shared_pre_array = new ArrayList<>();
    EditText edt_order_offer,edt_redeem_offer;
    ArrayList<OfferOrder> offerOrderArrayList;
    String Itbms_percentage;
    String wallet_amount;
    String final_itbms_price;
    DecimalFormat df;
    JSONObject jo = null;
    JSONArray jsonArray;
    JSONObject object;
    JSONObject jsonObject;

    String selected_store_id,selected_store_name,selected_store_apikey;
    String offer_id, Total_reward_point, selected_offer_type, Total_Amount, Total_tax, Total_discount_price,Redeem_point,imageurl;
    ArrayList<String> pricecombo;
    ArrayList<String> modifiercombo;
    ImageView img_close_offer,img_close_redeem;
    Double Wallet_amount;
    Double walletfinalamount;
    ArrayList<CategoryMenu> categoryMenuArrayList_shared_pre_array = new ArrayList<>();
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList_shared_pre_array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_resume);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();

    }

    private void findviewID() {
        expandable_listview = (ExpandableListView) findViewById(R.id.expandable_listview);
        img_back_order_resume = (ImageView) findViewById(R.id.img_back_order_resume);
        order_resume_restaurant_name = (TextView) findViewById(R.id.order_resume_restaurant_name);

    }

    private void init() {

        selected_store_id = getIntent().getStringExtra("restaurant_id");
        selected_store_name = getIntent().getStringExtra("restaurant_name");
        selected_store_apikey = getIntent().getStringExtra("restaurant_apikey");
        imageurl = getIntent().getStringExtra("imageurl");

        Log.e("imageurlaaa",imageurl);

        order_resume_restaurant_name.setText(selected_store_name);

        Log.e("selected_store_id", selected_store_id);

        df = new DecimalFormat("00.00");

        sharedPrefs = new SharedPrefs(this);
        mCrypt = new MCrypt();
        loader = new Request_loader(this);
        comman_dialog = new Comman_Dialog(this);
        gson = new Gson();

        new Get_itbms().execute();

        categoryMenuArrayList_shared_pre_array = gson.fromJson(sharedPrefs.get_CategoryMenu_array(), new TypeToken<ArrayList<CategoryMenu>>() {
        }.getType());

        categoryMenuModifierArrayList_shared_pre_array = gson.fromJson(sharedPrefs.get_CategoryMenuItem_array(), new TypeToken<ArrayList<CategoryMenuModifier>>() {
        }.getType());

        //Log.e("menusize",categoryMenuArrayList_shared_pre_array.size()+"");
        //Log.e("menumodifiersize",categoryMenuModifierArrayList_shared_pre_array.size()+"");

        list1 = new ArrayList<ExpandableOrderGroup>();

        if (categoryMenuArrayList_shared_pre_array != null) {
            for (int i = 0; i < categoryMenuArrayList_shared_pre_array.size(); i++) {
                ExpandableOrderGroup expandableOrderGroup = new ExpandableOrderGroup();
                expandableOrderGroup.setName(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name());
                expandableOrderGroup.setId(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_id());
                expandableOrderGroup.setQuantity(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_quantity());
                expandableOrderGroup.setPrice(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price());

                Log.e("groupprice",categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price());

                expandableChild_list = new ArrayList<ExpandableOrderChild>();

                for (int j = 0; j < categoryMenuModifierArrayList_shared_pre_array.size(); j++) {
                    ExpandableOrderChild expandableOrderChild = new ExpandableOrderChild();

                    expandableOrderChild.setId(categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_id());
                    expandableOrderChild.setName(categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_name());
                    expandableOrderChild.setPrice(categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_price());
                    expandableOrderChild.setQuantity(categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_quantity());
                    if (categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name().equalsIgnoreCase(categoryMenuModifierArrayList_shared_pre_array.get(j).getCategory_item_name()) && categoryMenuArrayList_shared_pre_array.get(i).getOrder_count().equalsIgnoreCase(categoryMenuModifierArrayList_shared_pre_array.get(j).getOrder_count()))
                        expandableChild_list.add(expandableOrderChild);
                }
                expandableOrderGroup.setExpandableOrderChild(expandableChild_list);
                list1.add(expandableOrderGroup);
            }
        }

        for (int k = 0; k < list1.size(); k++) {
            //Log.e("price",list1.get(k).getPrice());
            Totalsum += Double.valueOf(list1.get(k).getPrice());
            Log.e("suma",Totalsum+"");
        }

        ExpAdapter = new ExpandListAdapter(
                OrderResumeActivity.this, list1);

        View view = getLayoutInflater().inflate(R.layout.expandable_list_footer_layout, expandable_listview, false);

        footerLayout = (LinearLayout) view.findViewById(R.id.footer_layout);

        resume_order_txt_totalprice = (TextView) footerLayout.findViewById(R.id.resume_order_txt_totalprice);

        resume_order_txt_subtotal = (TextView) footerLayout.findViewById(R.id.resume_order_txt_subtotal);

        resume_order_txt_discount = (TextView) footerLayout.findViewById(R.id.resume_order_txt_discount);

        resume_order_txt_discount_label = (TextView) footerLayout.findViewById(R.id.resume_order_txt_discount_label);

        resume_order_txt_itbms_label = (TextView) footerLayout.findViewById(R.id.resume_order_txt_itbms_label);

        resume_order_txt_itbms = (TextView) footerLayout.findViewById(R.id.resume_order_txt_itbms);

        resume_order_txt_wallet = (TextView)footerLayout.findViewById(R.id.resume_order_txt_wallet);

        btn_order_place = (Button) footerLayout.findViewById(R.id.btn_order_place);

        edt_order_offer = (EditText) footerLayout.findViewById(R.id.edt_order_offer);

        img_close_offer = (ImageView)footerLayout.findViewById(R.id.img_close_offer);

        img_close_redeem = (ImageView)footerLayout.findViewById(R.id.img_close_redeem);

        edt_redeem_offer = (EditText) footerLayout.findViewById(R.id.edt_redeem_offer);

        btn_order_offer_apply = (Button) footerLayout.findViewById(R.id.btn_order_offer_apply);

        btn_redeem_offer_apply = (Button) footerLayout.findViewById(R.id.btn_redeem_offer_apply);

        resume_order_txt_subtotal.setText("$" + " " + df.format(Totalsum) + "");

        resume_order_txt_totalprice.setText("$" + " " + df.format(Totalsum) + "");

        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

        TotalAmount = String.valueOf(Totalsum);

        expandable_listview.addFooterView(footerLayout);

        expandable_listview.setAdapter(ExpAdapter);

        jsonArray = new JSONArray();

    }

    private void set_listeners() {


        btn_order_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0 ; i< categoryMenuArrayList_shared_pre_array.size();i++)
                {
                    jsonObject = new JSONObject();

                    try {
                        Log.e("iuddd",categoryMenuArrayList_shared_pre_array.get(i).getCategory_id());
                        jsonObject.put("category_id",categoryMenuArrayList_shared_pre_array.get(i).getCategory_id());
                        jsonObject.put("category_name",StringEscapeUtils.escapeHtml4(categoryMenuArrayList_shared_pre_array.get(i).getCategory_name()));
                        jsonObject.put("category_item_id",categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_id());
                        jsonObject.put("category_item_name",StringEscapeUtils.escapeHtml4(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name()));
                        jsonObject.put("category_quantity",categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_quantity());
                        jsonObject.put("category_price",categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price());
                        jsonObject.put("order_item_image",categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_image());
                        JSONArray jsonArray1 = new JSONArray();
                        for (int j = 0 ; j < categoryMenuModifierArrayList_shared_pre_array.size();j++)
                        {
                            JSONObject jsonObject1 = new JSONObject();
                            if (categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name().equalsIgnoreCase(categoryMenuModifierArrayList_shared_pre_array.get(j).getCategory_item_name()))
                            {
                                jsonObject1.put("modifier_id",categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_id());
                                jsonObject1.put("modifier_name",StringEscapeUtils.escapeHtml4(categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_name()));
                                jsonObject1.put("modifier_price",categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_price());
                                jsonObject1.put("modifier_quantity",categoryMenuModifierArrayList_shared_pre_array.get(j).getModifier_quantity());
                                jsonArray1.put(jsonObject1);
                            }
                        }
                        jsonObject.put("modifiers",jsonArray1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }



                Log.e("jsonobject",jsonArray.toString());

                String wallet = resume_order_txt_wallet.getText().toString().replace("$ ","");
                String amount = resume_order_txt_totalprice.getText().toString().replace("$ ","");
                if (jsonArray.length() > 0) {
                    new  confirm_order(offer_id, selected_store_id, Total_reward_point, Total_discount_price, jsonArray, selected_offer_type, amount, Total_tax,Redeem_point,wallet).execute();
                } else {
                    comman_dialog.Show_alert("Please Select Menu Item");
                }

            }
        });

        img_back_order_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_order_offer_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_order_offer.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(getBaseContext(),getResources().getString(R.string.enter_offer_code),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //double amount = Double.parseDouble(resume_order_txt_totalprice.getText().toString().replace("$ ", ""));
                    //Log.e("amount",amount+"");

                    if (Totalsum>0)
                    {
                        new Get_qr_code_offer_detail(edt_order_offer.getText().toString()).execute();
                    }
                    else
                    {
                        comman_dialog.Show_alert(getResources().getString(R.string.select_menu_item));
                    }


                }
            }
        });

        edt_order_offer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                {
                    img_close_offer.setVisibility(View.VISIBLE);
                }
                else
                {
                    img_close_offer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    btn_redeem_offer_apply.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (edt_redeem_offer.getText().toString().trim().equalsIgnoreCase(""))
            {
                Toast.makeText(getBaseContext(),getResources().getString(R.string.enter_redeem_point),Toast.LENGTH_SHORT).show();
            }
            else
            {
                double amount = Double.parseDouble(resume_order_txt_totalprice.getText().toString().replace("$ ", ""));
                //Log.e("amount",amount+"");

                if (Totalsum>0)
                {
                    new Get_redeem_offer_point(edt_redeem_offer.getText().toString()).execute();
                }
                else
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.select_menu_item));
                }

            }

        }
    });

    edt_redeem_offer.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0)
            {
                img_close_redeem.setVisibility(View.VISIBLE);
            }
            else
            {
                img_close_redeem.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });
    img_close_offer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edt_order_offer.setText("");
        }
    });

    img_close_redeem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edt_redeem_offer.setText("");
        }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(OrderResumeActivity.this,MenuOrderActivity.class);
        i.putExtra("restaurant_id",selected_store_id);
        i.putExtra("restaurant_name",selected_store_name);
        i.putExtra("restaurant_apikey",selected_store_apikey);
        startActivity(i);*/
    }

    private class Get_qr_code_offer_detail extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String offer_code;

        public Get_qr_code_offer_detail(String offer_code) {
            this.offer_code = offer_code;
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

                RequestBody reqbody = new FormBody.Builder()
                        .add("qrcode", MCrypt.bytesToHex(mCrypt.encrypt(offer_code)))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.getqrcodeofferdetail, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_qrcodeofferdetail", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_qrcodeofferdetail", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        edt_order_offer.setText("");

                        JSONArray offerdetailarray = main_obj.getJSONArray("data");

                        for (int i = 0; i < offerdetailarray.length(); i++) {
                            JSONObject offerdetailobject = offerdetailarray.getJSONObject(i);

                            offer_id = offerdetailobject.getString("id");

                            String menu_product_id = offerdetailobject.getString("menu_product_id");
                            String categorie_id = offerdetailobject.getString("categories_ids");

                            String offer_type = offerdetailobject.getString("offers_types");

                            String offer = offerdetailobject.getString("type");

                            selected_offer_type = offer;

                            String discountprice = offerdetailobject.getString("discount_price");

                            String reward_points = offerdetailobject.getString("offers_reward_point");

                            int offer_reward_point = 0;

                            if (!reward_points.equalsIgnoreCase(""))
                            {
                                Log.e("null_","null_");
                                offer_reward_point = Integer.parseInt(reward_points);
                            }
                            Log.e("reward_point",reward_points);



                            //Log.e("offer_type",offer_type+disountprice+reward_points);

                            resume_order_txt_discount_label.setText(getResources().getString(R.string.discount) + " " + discountprice + "%");

                            //menu_id
                            if (!menu_product_id.equalsIgnoreCase("")) {
                                Log.e("notnull", "notnull");
                                List<String> items = Arrays.asList(menu_product_id.split("\\s*,\\s*"));

                                if (offer_type.equalsIgnoreCase("direct discounts")) {
                                    offerOrderArrayList = new ArrayList<>();
                                    for (int j = 0; j < categoryMenuArrayList_shared_pre_array.size(); j++) {
                                        if (items.contains(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id())) {
                                            Log.e("match", "match");
                                            offerOrderArrayList.add(new OfferOrder(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id(), categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_price()));
                                        } else {
                                            Log.e("unmatch", "unmatch");
                                        }
                                    }

                                    Double Total_offer_apply_order_price = 0.00;
                                    if (offerOrderArrayList.size() > 0) {
                                        for (int k = 0; k < offerOrderArrayList.size(); k++) {
                                            //Log.e("price",list1.get(k).getPrice());
                                            Total_offer_apply_order_price += Double.valueOf(offerOrderArrayList.get(k).getPrice());
                                            Log.e("sumb", Total_offer_apply_order_price + "");
                                        }

                                        Log.e("offerOrderArrayList", offerOrderArrayList.size() + "");

                                        Double discount_percentage = Double.valueOf(discountprice);

                                        double discount_price = Total_offer_apply_order_price * (discount_percentage / 100.0);

                                        Total_discount_price = String.valueOf(discount_price);

                                        Double disount = Double.valueOf(df.format(discount_price));

                                        resume_order_txt_discount.setText("$ " + df.format(discount_price));

                                        Double final_itbms_added_price = Double.valueOf(final_itbms_price);

                                        Double final_disount_price = final_itbms_added_price - disount;

                                        Log.e("final_itbms_added_price",final_itbms_added_price+"");

                                        Log.e("final_disount_price",final_disount_price+"");
                                        //aaaaaaaaaa

                                        if (Wallet_amount>=final_disount_price)
                                        {
                                            walletfinalamount = final_disount_price - final_disount_price;
                                            resume_order_txt_wallet.setText("$ " + df.format(final_disount_price));
                                        }
                                        else
                                        {
                                            walletfinalamount = final_disount_price - Wallet_amount;
                                            resume_order_txt_wallet.setText("$ " + df.format(Wallet_amount));
                                        }


                                        resume_order_txt_totalprice.setText("$ " + df.format(walletfinalamount));

                                        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_applied)+" "+ resume_order_txt_discount.getText().toString() + " "+getResources().getString(R.string.success));
                                    } else {
                                        selected_offer_type = null;
                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_unavailable));
                                    }


                                } else if (offer_type.equalsIgnoreCase("points multiplier")) {
                                    Log.e("points multiplier", "points multiplier");

                                    offerOrderArrayList = new ArrayList<>();
                                    for (int j = 0; j < categoryMenuArrayList_shared_pre_array.size(); j++) {
                                        if (items.contains(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id())) {
                                            Log.e("match", "match");
                                            offerOrderArrayList.add(new OfferOrder(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id(), categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_price()));
                                        } else {
                                            Log.e("unmatch", "unmatch");
                                        }
                                    }

                                    int Total_offer_apply_order_reward_point = 0;

                                    if (offerOrderArrayList.size() > 0) {
                                        for (int k = 0; k < offerOrderArrayList.size(); k++) {
                                            //Log.e("price",list1.get(k).getPrice());
                                            Total_offer_apply_order_reward_point += offer_reward_point;
                                            Log.e("offer_reward_point", Total_offer_apply_order_reward_point + "");
                                        }

                                        Log.e("offer_reward_point", Total_offer_apply_order_reward_point + "");

                                        Total_reward_point = String.valueOf(Total_offer_apply_order_reward_point);

                                        resume_order_txt_discount.setText("$ 00.00" );

                                        Log.e("final_itbms_price_menu_product",final_itbms_price);

                                        Double final_itbms_added_price = Double.valueOf(final_itbms_price);

                                        resume_order_txt_totalprice.setText("$ " + df.format(final_itbms_added_price));

                                        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_applied_success));

                                    } else {
                                        selected_offer_type = null;
                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_unavailable));
                                    }


                                }
                            } else {
                                Log.e("menu_null", "menu_null");
                            }

                            //category_id
                            if (!categorie_id.equalsIgnoreCase("")) {
                                List<String> items = Arrays.asList(categorie_id.split("\\s*,\\s*"));
                                Log.e("items", items.size() + "");


                                Double discount_percentage = Double.valueOf(discountprice);
                                if (offer_type.equalsIgnoreCase("direct discounts")) {
                                    offerOrderArrayList = new ArrayList<>();
                                    for (int j = 0; j < categoryMenuArrayList_shared_pre_array.size(); j++) {
                                        if (items.contains(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id())) {
                                            Log.e("match", "match");
                                            offerOrderArrayList.add(new OfferOrder(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id(), categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_price()));
                                        } else {
                                            Log.e("unmatch", "unmatch");
                                        }
                                    }

                                    Double Total_offer_apply_order_price = 0.00;
                                    if (offerOrderArrayList.size() > 0) {
                                        for (int k = 0; k < offerOrderArrayList.size(); k++) {
                                            //Log.e("price",list1.get(k).getPrice());
                                            Total_offer_apply_order_price += Double.valueOf(offerOrderArrayList.get(k).getPrice());
                                            Log.e("sumc", Total_offer_apply_order_price + "");
                                        }

                                        Log.e("offerOrderArrayList", offerOrderArrayList.size() + "");

                                        double discount_price = Total_offer_apply_order_price * (discount_percentage / 100.0);

                                        Total_discount_price = String.valueOf(discount_price);

                                        Double disount = Double.valueOf(df.format(discount_price));

                                        resume_order_txt_discount.setText("$ " + df.format(discount_price));

                                        Double final_itbms_added_price = Double.valueOf(final_itbms_price);

                                        Double final_disount_price = final_itbms_added_price - disount;

                                        if (Wallet_amount>=final_disount_price)
                                        {
                                            walletfinalamount = final_disount_price - final_disount_price;
                                            resume_order_txt_wallet.setText("$ " + df.format(final_disount_price));
                                        }
                                        else
                                        {
                                            walletfinalamount = final_disount_price - Wallet_amount;
                                            resume_order_txt_wallet.setText("$ " + df.format(Wallet_amount));
                                        }

                                        resume_order_txt_totalprice.setText("$ " + df.format(walletfinalamount));

                                        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_applied) +" "+ resume_order_txt_discount.getText().toString() + " "+getResources().getString(R.string.success));

                                    } else {
                                        selected_offer_type = null;
                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_unavailable));
                                    }
                                } else if (offer_type.equalsIgnoreCase("points multiplier")) {
                                    Log.e("points multiplier", "points multiplier");

                                    offerOrderArrayList = new ArrayList<>();
                                    for (int j = 0; j < categoryMenuArrayList_shared_pre_array.size(); j++) {
                                        if (items.contains(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id())) {
                                            Log.e("match", "match");
                                            offerOrderArrayList.add(new OfferOrder(categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_id(), categoryMenuArrayList_shared_pre_array.get(j).getCategory_item_price()));
                                        } else {
                                            Log.e("unmatch", "unmatch");
                                        }
                                    }

                                    int Total_offer_apply_order_reward_point = 0;

                                    if (offerOrderArrayList.size() > 0) {
                                        for (int k = 0; k < offerOrderArrayList.size(); k++) {
                                            //Log.e("price",list1.get(k).getPrice());
                                            Total_offer_apply_order_reward_point += offer_reward_point;
                                            Log.e("offer_reward_point", Total_offer_apply_order_reward_point + "");
                                        }

                                        Log.e("offer_reward_point", Total_offer_apply_order_reward_point + "");

                                        Total_reward_point = String.valueOf(Total_offer_apply_order_reward_point);

                                        resume_order_txt_discount.setText("$ 00.00" );

                                        Log.e("final_itbms_price_category",final_itbms_price);

                                        Double final_itbms_added_price = Double.valueOf(final_itbms_price);

                                        resume_order_txt_totalprice.setText("$ " + df.format(final_itbms_added_price));

                                        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_applied_success));

                                    } else {
                                        selected_offer_type = null;
                                        comman_dialog.Show_alert(getResources().getString(R.string.offer_unavailable));
                                    }
                                }
                            } else {
                                Log.e("category_null", "_category_null");
                            }

                        }

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_offer", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }

    private class Get_itbms extends AsyncTask<String, Void, String> {

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

                RequestBody reqbody = new FormBody.Builder()
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.My_card_userprofiledetail, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_getitbms", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_qgetitbms", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONObject itbmsobject = main_obj.getJSONObject("data");

                        Itbms_percentage = itbmsobject.getString("ITBMS_Percentage");

                        wallet_amount = itbmsobject.getString("wallet_amount");

                        Wallet_amount = Double.valueOf(wallet_amount);

                        resume_order_txt_wallet.setText("$ " + df.format(Wallet_amount));

                        Double itbms = Double.valueOf(Itbms_percentage);

                        resume_order_txt_itbms_label.setText(getResources().getString(R.string.itbms) + " " + Itbms_percentage + "%");

                        double itbms_price = Totalsum * (itbms / 100.0);

                        DecimalFormat df = new DecimalFormat("00.00");

                        Double disount = Double.valueOf(df.format(itbms_price));

                        resume_order_txt_itbms.setText("$ " + df.format(itbms_price));

                        Total_tax = String.valueOf(itbms_price);

                        final_itbms_price = String.valueOf(Totalsum + disount);

                        Double itbms_added = Totalsum + disount;

                        resume_order_txt_totalprice.setText("$ " + df.format(itbms_added));

                        if (Wallet_amount>=itbms_added)
                        {
                            Log.e("itbms_added",itbms_added+"");
                            walletfinalamount = itbms_added - itbms_added;
                            resume_order_txt_wallet.setText("$ " + df.format(itbms_added));
                        }
                        else
                        {
                            walletfinalamount = itbms_added - Wallet_amount;
                            resume_order_txt_wallet.setText("$ " + df.format(Wallet_amount));
                        }


                        resume_order_txt_totalprice.setText("$ " + df.format(walletfinalamount));

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_getitbms", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }

    private class confirm_order extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String offer_id, store_id, reward_point, discount_price, offer_type, total_Amount, total_tax,redeem_point,wallet_amount;
        JSONArray jsonArray;

        public confirm_order(String offer_id, String store_id, String reward_point, String discount_price, JSONArray jsonArray, String offer_type, String total_Amount, String total_tax, String redeem_point,String wallet_amount) {
            this.offer_id = offer_id;
            this.store_id = store_id;
            this.reward_point = reward_point;
            this.discount_price = discount_price;
            this.jsonArray = jsonArray;
            this.offer_type = offer_type;
            this.total_Amount = total_Amount;
            this.total_tax = total_tax;
            this.redeem_point = redeem_point;
            this.wallet_amount = wallet_amount;
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

                //String Escape = MCrypt.bytesToHex(mCrypt.encrypt(StringEscapeUtils.escapeHtml4(jsonArray.toString())));
                /*Log.e("Escape3", StringEscapeUtils.escapeHtml3(jsonArray.toString()));
                Log.e("UnEscape3", StringEscapeUtils.unescapeHtml3(jsonArray.toString()));

                Log.e("Escape4", StringEscapeUtils.escapeHtml4(jsonArray.toString()));
                Log.e("UnEscape4", StringEscapeUtils.unescapeHtml4(jsonArray.toString()));*/

                if (offer_type != null) {
                    Log.e("offer_if", "offer_if");
                    if (reward_point != null) {

                        Log.e("offer_reward", "offer_reward");
                        Log.e("userID", sharedPrefs.get_User_id());
                        Log.e("offers_id", offer_id);
                        Log.e("stores_id", store_id);
                        Log.e("rewards_point", reward_point);
                        Log.e("timezone", TimeZone.getDefault().getID());
                        Log.e("order_details", String.valueOf(jsonArray));
                        Log.e("offers_type", offer_type);
                        Log.e("total_amount", total_Amount);
                        Log.e("order_tax", total_tax);
                        Log.e("wallet_amount", wallet_amount);

                        Log.e("offer_reward", "offer_reward");
                        Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                        Log.e("offers_id", MCrypt.bytesToHex(mCrypt.encrypt(offer_id)));
                        Log.e("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)));
                        Log.e("rewards_point", MCrypt.bytesToHex(mCrypt.encrypt(reward_point)));
                        Log.e("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())));
                        Log.e("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))));
                        Log.e("offers_type", MCrypt.bytesToHex(mCrypt.encrypt(offer_type)));
                        Log.e("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)));
                        Log.e("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)));
                        Log.e("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)));

                        RequestBody reqbody = new FormBody.Builder()
                                .add("Apikey", Comman_url.api_key)
                                .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                                .add("offers_id", MCrypt.bytesToHex(mCrypt.encrypt(offer_id)))
                                .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_id)))
                                .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)))
                                .add("order_description", "")
                                .add("rewards_point", MCrypt.bytesToHex(mCrypt.encrypt(reward_point)))
                                .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                                .add("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))))
                                .add("offers_type", MCrypt.bytesToHex(mCrypt.encrypt(offer_type)))
                                .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)))
                                .add("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)))
                                .add("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)))
                                .build();

                        Responce_jason = CallingMethod.POST(client, Comman_api_name.get_confirm_order, reqbody);
                    } else if (discount_price != null) {
                        Log.e("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)));
                        Log.e("offer_discount", "offer_discount");
                        RequestBody reqbody = new FormBody.Builder()
                                .add("Apikey", Comman_url.api_key)
                                .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                                .add("offers_id", MCrypt.bytesToHex(mCrypt.encrypt(offer_id)))
                                .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)))
                                .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)))
                                .add("order_description", "")
                                .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                                .add("discount_price", MCrypt.bytesToHex(mCrypt.encrypt(discount_price)))
                                .add("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))))
                                .add("offers_type", MCrypt.bytesToHex(mCrypt.encrypt(offer_type)))
                                .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)))
                                .add("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)))
                                .add("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)))
                                .build();

                        Responce_jason = CallingMethod.POST(client, Comman_api_name.get_confirm_order, reqbody);
                    }
                } else {
                    Log.e("offer_else", "offer_else");

                    //Log.e("escape", Escape);
                    Log.e("unescape", StringEscapeUtils.unescapeHtml4(jsonArray.toString()));

                    if (redeem_point != null)
                    {
                        RequestBody reqbody = new FormBody.Builder()
                                .add("Apikey", Comman_url.api_key)
                                .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                                .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)))
                                .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)))
                                .add("order_description", "")
                                .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                                .add("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))))
                                .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)))
                                .add("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)))
                                .add("redeemed_point", MCrypt.bytesToHex(mCrypt.encrypt(redeem_point)))
                                .add("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)))
                                .build();

                        Responce_jason = CallingMethod.POST(client, Comman_api_name.get_confirm_order, reqbody);
                    }
                    else
                    {

                        Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                        Log.e("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)));
                        Log.e("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)));
                        Log.e("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())));
                        Log.e("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))));
                        Log.e("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)));
                        Log.e("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)));
                        Log.e("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)));

                        RequestBody reqbody = new FormBody.Builder()
                                .add("Apikey", Comman_url.api_key)
                                .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                                .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)))
                                .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)))
                                .add("order_description", "")
                                .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                                .add("order_details", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(jsonArray))))
                                .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_Amount)))
                                .add("order_tax", MCrypt.bytesToHex(mCrypt.encrypt(total_tax)))
                                .add("wallet_amount", MCrypt.bytesToHex(mCrypt.encrypt(wallet_amount)))
                                .build();

                        Responce_jason = CallingMethod.POST(client, Comman_api_name.get_confirm_order, reqbody);
                    }

                }


                Log.e("ELSE_ONLY", "ELSE_ONLY");


            } catch (Exception e) {
                Log.e("errrrrr_confirmorder", e.toString());
            }
            Log.e("Responce_jason", "RSPNS_JSON = " + Responce_jason);
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_confirmorder", result + "");

            try {
                if (result != null) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.order_received), Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("code");

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsondataObject = jsonArray.getJSONObject(i);

                            Double totalprice = Double.valueOf(resume_order_txt_totalprice.getText().toString().replace("$ ",""));
                            Log.e("resume_order_txt_totalprice",totalprice+"");

                            if (totalprice>0)
                            {
                                //hide loader
                                loader.hidepDialog();

                                Intent intent = new Intent(OrderResumeActivity.this, PaymentActivity.class);
                                intent.putExtra("order_id", jsondataObject.getString("id"));
                                intent.putExtra("store_id", selected_store_id);
                                intent.putExtra("store_api_key", selected_store_apikey);
                                intent.putExtra("total_amount", jsondataObject.getString("total_amount"));
                                startActivity(intent);
                            }
                            else
                            {
                                Log.e("else0","else0");
                                new confirm_payment(jsondataObject.getString("id"),selected_store_id,jsondataObject.getString("total_amount")).execute();
                            }

                        }
                    }

                } else {
                    //comman_dialog.Show_alert(main_obj.getString("message"));
                }
            } catch (Exception e) {
                Log.e("exception_confirmorder", e.toString());
            }

        }
    }

    private class Get_redeem_offer_point extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String redeem_point;

        public Get_redeem_offer_point(String redeem_point) {
            this.redeem_point = redeem_point;
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

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("order_amount", MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(Totalsum))))
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("redeem_point", MCrypt.bytesToHex(mCrypt.encrypt(redeem_point)))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.get_redeem_payment, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_redeempoint", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_redeempoint", result + "");
            try {
                JSONObject jsonmainObject = new JSONObject(result);

                String status = jsonmainObject.getString("code");

                if (status.equalsIgnoreCase("200"))
                {

                    JSONArray jsonmainArray = jsonmainObject.getJSONArray("data");

                    for (int i = 0 ; i < jsonmainArray.length();i++)
                    {
                        selected_offer_type = null;

                        JSONObject jsondataobject = jsonmainArray.getJSONObject(i);

                        double order_amount = jsondataobject.getDouble("order_amount");

                        double final_amount = jsondataobject.getDouble("final_amount");

                        Log.e("order_amount", String.valueOf(order_amount));
                        Log.e("final_amount", String.valueOf(final_amount));

                        double redeem_point = jsondataobject.getDouble("redeem_point");

                        Redeem_point = String.valueOf(redeem_point);

                        Log.e("Redeem_point",redeem_point+"");

                        resume_order_txt_discount_label.setText(getResources().getString(R.string.discount) + " " + edt_redeem_offer.getText().toString() + " Points");

                        double redeem_amount = order_amount - final_amount;

                        edt_redeem_offer.setText("");

                        resume_order_txt_discount.setText("$ " +df.format(redeem_amount));

                        Double disount = Double.valueOf(df.format(redeem_amount));

                        Double final_itbms_added_price = Double.valueOf(final_itbms_price);

                        Double final_disount_price = final_itbms_added_price - disount;

                        if (Wallet_amount>=final_disount_price)
                        {
                            Log.e("itbms_added",final_disount_price+"");
                            walletfinalamount = final_disount_price - final_disount_price;
                            resume_order_txt_wallet.setText("$ " + df.format(final_disount_price));
                        }
                        else
                        {
                            walletfinalamount = final_disount_price - Wallet_amount;
                            resume_order_txt_wallet.setText("$ " + df.format(Wallet_amount));
                        }

                        resume_order_txt_totalprice.setText("$ " + df.format(walletfinalamount));

                        Total_Amount = resume_order_txt_totalprice.getText().toString().replace("$ ", "");

                        comman_dialog.Show_alert(jsonmainObject.getString(getResources().getString(R.string.redeem_point_success)));
                        
                    }
                }
                else
                {
                    comman_dialog.Show_alert(jsonmainObject.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //hide loader
            loader.hidepDialog();
        }
    }

    private class confirm_payment extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String order_id,store_id,total_amount;

        public confirm_payment(String order_id, String store_id, String total_amount) {
            this.order_id = order_id;
            this.store_id = store_id;
            this.total_amount = total_amount;
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

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("orders_id", MCrypt.bytesToHex(mCrypt.encrypt(order_id)))
                        .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)))
                        .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_amount)))
                        .add("txn_id", "")
                        .add("currency_code", MCrypt.bytesToHex(mCrypt.encrypt("dollar")))
                        .add("payment_method", MCrypt.bytesToHex(mCrypt.encrypt("credit_debit_cards")))
                        .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                        .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(selected_store_apikey)))
                        .build();

                Log.e("user_id",MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("orders_id",MCrypt.bytesToHex(mCrypt.encrypt(order_id)));
                Log.e("stores_id",MCrypt.bytesToHex(mCrypt.encrypt(store_id)));
                Log.e("total_amount",MCrypt.bytesToHex(mCrypt.encrypt(total_amount)));
                Log.e("txn_id","");
                Log.e("currency_code",MCrypt.bytesToHex(mCrypt.encrypt("dollar")));
                Log.e("payment_method",MCrypt.bytesToHex(mCrypt.encrypt("credit_debit_cards")));
                Log.e("timezone",MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())));

                Responce_jason = CallingMethod.POST(client, Comman_api_name.get_confirm_payment, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_confirm_payment", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_confirm_payment", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    Log.e("codeabcdef",main_obj.getString("code"));
                    int code = Integer.parseInt(main_obj.getString("code"));
                    if (code == 200) {

                        sharedPrefs.save_Custom_array("");
                        sharedPrefs.save_Custom_menu_array("");
                        sharedPrefs.save_Custom_menu_child_array("");
                        sharedPrefs.save_Custom_menu_group_array("");
                        sharedPrefs.save_Custom_combo_item_array("");
                        sharedPrefs.save_Custom_combo_group_array("");
                        sharedPrefs.save_CategoryMenu_array("");
                        sharedPrefs.save_CategoryMenuItem_array("");
                        Toast.makeText(getBaseContext(),getResources().getString(R.string.order_placed_success),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OrderResumeActivity.this,MainActivity.class);
                        startActivity(i);


                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_confirm_payment", e.toString());
                loader.hidepDialog();
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}