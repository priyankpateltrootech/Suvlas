package com.suvlas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adpter.GiftCardAmountAdapter;
import adpter.GiftcardImgSlideAdapter;
import bean.PriceId;
import bean.gift_card_templet;
import common.CallingMethod;
import common.CirclePageIndicator;
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
 * Created by hp on 3/3/2017.
 */

public class GiftFragment extends Fragment implements View.OnClickListener {

    public static final String PREFS_SHARED = "PREFS_PRIVATE";

    public EditText edt_to, edt_form, edt_msg,name;
    public Button btn_payrs5;
    TextView text_status;
    RelativeLayout rel_top;
    Toolbar toolbar;
    RelativeLayout rel_next;
    ImageView img_back, img_msg, img_gift;
    GiftcardImgSlideAdapter giftcardimgadapter;
    ViewPager viewPager;
    View rootView;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    ArrayList<gift_card_templet> gift_templet = new ArrayList<>();
    ArrayList<PriceId> price_item = new ArrayList<>();
    String[] spinnerValues = {"Gmail", "Mensaje"};
    int total_images[] = {R.drawable.dropdown_gmail, R.drawable.dropdown_gmail};
    String gmail;
    CirclePageIndicator indicator;
    String Gift_temp_id;
    RecyclerView recycler_view_amount;
    GiftCardAmountAdapter giftCardAmountAdapter;
    LinearLayoutManager horizontalLayoutManagaer;
    Bundle bundle = new Bundle();
    String gift_card_amount,spinner_selected_value;
    private String valid_email;
    Spinner spinner_gift_card_method;
    String ITBMS_percent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.giftcard, container, false);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
        return rootView;
    }

    private void findviewID() {

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);

        text_status = (TextView) getActivity().findViewById(R.id.txt_status);
        rel_top = (RelativeLayout) getActivity().findViewById(R.id.rel_top);
        btn_payrs5 = (Button) rootView.findViewById(R.id.payrs6);
        rel_next = (RelativeLayout) rootView.findViewById(R.id.rel_next);
        spinner_gift_card_method = (Spinner) rootView.findViewById(R.id.spinner_show);
        spinner_gift_card_method.setAdapter(new MyAdapter(getActivity(), R.layout.custom_spinner_giftcard, spinnerValues));
        img_back = (ImageView) rootView.findViewById(R.id.img_back);
        img_msg = (ImageView) getActivity().findViewById(R.id.img_msg);

        edt_to = (EditText) rootView.findViewById(R.id.to);
        edt_form = (EditText) rootView.findViewById(R.id.form);
        edt_msg = (EditText) rootView.findViewById(R.id.message);
        name = (EditText)rootView.findViewById(R.id.name);

        recycler_view_amount = (RecyclerView) rootView.findViewById(R.id.rel_gift_amount);
        horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_amount.setLayoutManager(horizontalLayoutManagaer);
    }


    private void init() {
        mCrypt = new MCrypt();
        loader = new Request_loader(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        comman_dialog = new Comman_Dialog(getActivity());

        edt_form.setText(sharedPrefs.get_Email_id());
        if (CommanMethod.isInternetOn(getActivity())) {
            price_item = new ArrayList<>();
            new Get_gift_templet().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
        bundle = getArguments();

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;
        Log.e("Width", display_width + "==" + display_width);
        Log.e("Height", display_height + "==" + display_height);

        viewPager.getLayoutParams().height = (int) (display_height / 2.8);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Gift_temp_id = gift_templet.get(position).tem_image_id;

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private void set_listeners() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        edt_to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Is_Valid_Email(edt_to);
            }
        });


        edt_form.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Is_Valid_Email(edt_to);
            }
        });

        rel_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_payrs5.getText().toString().equalsIgnoreCase("")) {
                    comman_dialog.Show_alert(getResources().getString(R.string.select_amount));
                } else if (edt_to.getText().toString().equalsIgnoreCase("")) {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_email));
                } else if (edt_form.getText().toString().equalsIgnoreCase("")) {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_email));
                }else if (name.getText().toString().equalsIgnoreCase("")) {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_gift_card_name));
                } else if (edt_msg.getText().toString().equalsIgnoreCase("")) {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_message));
                } else {
                    gmail = "gmail";
                    Log.e("gift_card_amount",gift_card_amount);
                    if (CommanMethod.isInternetOn(getActivity())) {
                        new Get_gift_card_detail().execute(Gift_temp_id, gift_card_amount,
                                spinner_selected_value, edt_to.getText().toString(), edt_form.getText().toString(), edt_msg.getText().toString(),name.getText().toString());
                    } else {
                        comman_dialog.Show_alert(Comman_message.Dont_internet);
                    }
                }
            }
        });

        spinner_gift_card_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinner_selected_value = spinner_gift_card_method.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    @Override
    public void onClick(View v) {
    }

    private class Get_gift_templet extends AsyncTask<String, Void, String> {

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
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Gift_card_templet, reqbody);

            } catch (Exception e) {
                Log.e("err_gift_card_templet", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_gift_card_templet", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                        if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {
                            JSONObject json_data = data_array.getJSONObject(i);
//                            gift_card_templet trmplet_list = new gift_card_templet();
                            String tem_name = json_data.getString("template_name");
                            String tem_image_id = json_data.getString("id");
                            String tem_image = json_data.getString("Image_Url");
                            //Log.e("tem_image", json_data.getString("template_image"));
                            gift_templet.add(new gift_card_templet(tem_name, tem_image_id, tem_image));

                            giftcardimgadapter = new GiftcardImgSlideAdapter(getActivity(), gift_templet);
                            viewPager.setAdapter(giftcardimgadapter);
                            indicator.setViewPager(viewPager);
                        }
                        JSONArray data_array_amount = main_obj.getJSONArray("giftcardAmount");
                        price_item = new ArrayList<>();
                        for (int j = 0; j < data_array_amount.length(); j++) {
                            JSONObject json_data_amount = data_array_amount.getJSONObject(j);

                            String price_id = json_data_amount.getString("id");
                            String price = json_data_amount.getString("price");

                            Log.e("priceitem", json_data_amount.getString("price"));
                            price_item.add(new PriceId(price_id, price));


                            giftCardAmountAdapter = new GiftCardAmountAdapter(getActivity(), price_item, btn_payrs5, new OnClickListenerAmount() {
                                @Override
                                public void onItemClicked(int position,String amount) {
                                    Log.e("position_gift",position+"");
                                    gift_card_amount = String.valueOf(position);
                                }
                            });
                            recycler_view_amount.setAdapter(giftCardAmountAdapter);
//                            btn_payrs5.setText(price_item.get(j).getPrice());
                        }
                            ITBMS_percent = main_obj.getString("ITBMS_Percent");
                            Log.e("ITBMS_percent",ITBMS_percent);
                        Log.e("length_array_offer", String.valueOf(data_array));
                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("err_gift_card_templet", e.toString());
            }
            loader.hidepDialog();
        }
    }

    private class Get_gift_card_detail extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
            Log.e("gift_card", "gift_card");
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                String name_escape = StringEscapeUtils.escapeHtml4(params[6]);
                String from_email_escape = StringEscapeUtils.escapeHtml4(params[4]);
                String to_email_escape = StringEscapeUtils.escapeHtml4(params[3]);
                String message_escape = StringEscapeUtils.escapeHtml4(params[5]);

                RequestBody reqbody = new FormBody.Builder()
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("giftcard_template", MCrypt.bytesToHex(mCrypt.encrypt(params[0])))
                        .add("giftcard_amount", MCrypt.bytesToHex(mCrypt.encrypt(params[1])))
                        .add("delivery_method", MCrypt.bytesToHex(mCrypt.encrypt(params[2])))
                        .add("to_email", MCrypt.bytesToHex(mCrypt.encrypt(to_email_escape)))
                        .add("from_email", MCrypt.bytesToHex(mCrypt.encrypt(from_email_escape)))
                        .add("message", MCrypt.bytesToHex(mCrypt.encrypt(message_escape)))
                        .add("to_name", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("userID", sharedPrefs.get_User_id());
                Log.e("giftcard_template2", params[0]);
                Log.e("giftcard_amount", (params[1]));
                Log.e("delivery_method", (params[2]));
                Log.e("to_email", (params[3]));
                Log.e("from_email", (params[4]));
                Log.e("message", (params[5]));
                Log.e("message", (params[6]));


                /*Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("giftcard_template", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                Log.e("giftcard_template2", params[0]);
                Log.e("giftcard_amount", MCrypt.bytesToHex(mCrypt.encrypt(params[1])));
                Log.e("delivery_method", MCrypt.bytesToHex(mCrypt.encrypt(params[2])));
                Log.e("to_email", MCrypt.bytesToHex(mCrypt.encrypt(params[3])));
                Log.e("from_email", MCrypt.bytesToHex(mCrypt.encrypt(params[4])));
                Log.e("message", MCrypt.bytesToHex(mCrypt.encrypt(params[5])));
                Log.e("message", MCrypt.bytesToHex(mCrypt.encrypt(params[6])));
                Log.e("Apikey", Comman_url.api_key);
*/
                Responce_jason = CallingMethod.POST(client, Comman_api_name.Preview_gift_card, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_giftcard", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_giftcard_msg", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);
                    Log.e("gift_message_result", String.valueOf(main_obj));
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        String preview = main_obj.getString("preview");
                        Log.e("preview", preview);

                        bundle = new Bundle();
                        bundle.putString("user_id", sharedPrefs.get_User_Name());
                        bundle.putString("preview", preview);
                        Log.e("templet_id", Gift_temp_id);
                        bundle.putString("templet_id", Gift_temp_id);
                        bundle.putString("select_amount", btn_payrs5.getText().toString());
                        bundle.putString("message_type", gmail);
                        bundle.putString("message_to", edt_to.getText().toString());
                        bundle.putString("message_form", edt_form.getText().toString());
                        bundle.putString("message_text", edt_msg.getText().toString());
                        bundle.putString("gift_card_amount",gift_card_amount);
                        bundle.putString("delivery_method",spinner_selected_value);
                        bundle.putString("itbms_percent",ITBMS_percent);
                        bundle.putString("name",main_obj.getString("to_name"));

                        Fragment fragment = new GiftCardPaymentFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.gift_card_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("result_giftcard", e.toString());
            }
            loader.hidepDialog();
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_spinner_giftcard, parent, false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(spinnerValues[position]);
//            TextView subSpinner = (TextView) mySpinner .findViewById(R.id.sub_text_seen);
//            subSpinner.setText(spinnerSubs[position]);
            ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
            if (position == 0)
            {
                left_icon.setImageResource(total_images[position]);
            }
            return mySpinner;
        }
    }


}
