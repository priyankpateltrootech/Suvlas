package com.suvlas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Expirydatepicker.DatePickerPopWin;
import adpter.GiftcardImgSlideAdapter;
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
 * Created by hp on 3/8/2017.
 */

public class GiftCardPaymentFragment extends Fragment implements View.OnClickListener {

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    View rootView;
    private SharedPreferences sharedPreferences;
    TextView txt_username, txt_email, txt_msg_des, txt_usd;
    int images[] = {R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card, R.drawable.gift_card_card};
//    GiftcardImgSlideAdapter giftcardimgadapter;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    RelativeLayout rel_procced;
    ImageView img_back,img_card;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    static final int DATE_DIALOG_ID = 1;
        private int mYear = 2013;
    private int mMonth = 5;
    private int mDay = 30;
    EditText expirationdate, cardnumber,securitycode,edt_card;
    int keyDel;
    String a;
    private boolean lock;
    ArrayList<gift_card_templet> gift_templet = new ArrayList<>();
    String gift_card_amount,gift_card_id,from_email,to_email,to_message,gift_card_amount_id,delivery_method,ITBMS_percent;
    int year,month,maxyear;
    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.giftcardpayment, container, false);

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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        Log.e("month",month+"");
        Log.e("year",year+"");
        maxyear = year+100;

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.e("date",date);
        Log.e("year_gift_Card_payment", String.valueOf(year));

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        txt_username = (TextView) rootView.findViewById(R.id.txt_username);
        txt_email = (TextView) rootView.findViewById(R.id.txt_email);
        txt_msg_des = (TextView) rootView.findViewById(R.id.txt_msgdescription);
        txt_usd = (TextView) rootView.findViewById(R.id.txt_usd);
        img_back = (ImageView) rootView.findViewById(R.id.img_back);
        img_card = (ImageView) rootView.findViewById(R.id.img_card);
        rel_procced = (RelativeLayout) rootView.findViewById(R.id.rel_procced);
        expirationdate = (EditText)rootView. findViewById(R.id.expirationdate);
        cardnumber = (EditText) rootView.findViewById(R.id.cardnumber);
        securitycode = (EditText) rootView.findViewById(R.id.securitycode);
        edt_card = (EditText) rootView.findViewById(R.id.edt_card);
//        sharedPreferences = this.getActivity().getSharedPreferences(GiftFragment.PREFS_SHARED, Context.MODE_PRIVATE);
    }

    private void set_listeners() {
        rel_procced.setOnClickListener(this);
        img_back.setOnClickListener(this);
        /*expirationdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                    s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
                }

            }
        });*/

        expirationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin  = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        expirationdate.setText(dateDesc);
                    }
                }).textConfirm("CONFIRM") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                        .minYear(year) //min year in loop
                        .maxYear(maxyear) // max year in loop
                        .dateChose(date) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(getActivity());
            }
        });
        cardnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lock || s.length() > 16) {
                    return;
                }
                lock = true;
                for (int i = 4; i < s.length(); i += 5) {
                    if (s.toString().charAt(i) != ' ') {
                        s.insert(i, " ");
                    }
                }
                lock = false;
            }
        });
    }

    private void init() {



        mCrypt = new MCrypt();
        loader = new Request_loader(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        comman_dialog = new Comman_Dialog(getActivity());
//        giftcardimgadapter = new GiftcardImgSlideAdapter(getActivity(), images);
//        viewPager.setAdapter(giftcardimgadapter);
//        indicator.setViewPager(viewPager);

//        if (CommanMethod.isInternetOn(getActivity())) {
//            new Get_gift_templet().execute();
//        } else {
//            comman_dialog.Show_alert(Comman_message.Dont_internet);
//        }
        Bundle bundle=getArguments();

        gift_card_amount = bundle.getString("select_amount");

        txt_username.setText(bundle.getString("name"));
        txt_email.setText(bundle.getString("message_to"));
        txt_msg_des.setText(bundle.getString("message_text"));
        txt_usd.setText(bundle.getString("select_amount"));

        Log.e("gift_card_id",bundle.getString("templet_id"));
        gift_card_id = bundle.getString("templet_id");
        from_email = bundle.getString("message_form");
        to_email = bundle.getString("message_to");
        to_message =bundle.getString("message_text");
        gift_card_amount_id = bundle.getString("gift_card_amount");
        delivery_method = bundle.getString("delivery_method");
        ITBMS_percent = bundle.getString("itbms_percent");
        /*Glide.with(getActivity()).load(bundle.getString("preview")).placeholder(R.drawable.loading).error(R.drawable.loading).into(img_card);*/
        Glide.with(getActivity()).load(bundle.getString("preview")).into(img_card);
//        giftcardimgadapter = new GiftcardImgSlideAdapter(getActivity(), images);
//        viewPager.setAdapter(giftcardimgadapter);
//        indicator.setViewPager(viewPager);

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;
        Log.e("Width", display_width + "==" + display_width);
        Log.e("Height", display_height + "==" + display_height);

        img_card.getLayoutParams().height = (int) (display_height / 2.8);
    }
    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_procced:
                Log.e("cardnumber",cardnumber.getText().toString().replace(" ",""));
                Log.e("securitycode",securitycode.getText().toString());
                Log.e("expirationdate",expirationdate.getText().toString().replace("/",""));

                String card_number = cardnumber.getText().toString().replace(" ","");
                String card_cvv = securitycode.getText().toString();
                String card_exp = expirationdate.getText().toString().replace("/","");

                /*int expirymonth = Integer.parseInt(expirationdate.getText().toString().substring(0,2));
                int expiryyear = Integer.parseInt(expirationdate.getText().toString().substring(3,5));*/

                if (edt_card.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_cardholder_name));
                }
                else if (cardnumber.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_card_number));
                }
                else if (securitycode.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_cvv));
                }
                else if (expirationdate.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_expiry));
                }
                else if (expirationdate.getText().toString().length()!=5)
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_valid_expiry));
                    /*Log.e("expirydate",expirationdate.getText().toString().length()+"");
                    Log.e("expirydate",expirationdate.getText().toString().substring(0,2));
                    Log.e("expirymonth",expirationdate.getText().toString().substring(3,5));*/
                }
                else if(Integer.parseInt(expirationdate.getText().toString().substring(0,2))>12)
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.enter_valid_expiry));
                }
                else
                {
                    int expirymonth = Integer.parseInt(expirationdate.getText().toString().substring(0,2));
                    Log.e("expirymonth_else",expirymonth+"");
                    Intent intent = new Intent(getActivity(), TransactionSucessActivity.class);
                    intent.putExtra("gift_card_amount",gift_card_amount);
                    intent.putExtra("card_holder_name",edt_card.getText().toString());
                    intent.putExtra("card_number",card_number);
                    intent.putExtra("card_cvv",card_cvv);
                    intent.putExtra("card_exp",card_exp);
                    intent.putExtra("gift_card_id",gift_card_id);
                    intent.putExtra("from_email",from_email);
                    intent.putExtra("to_email",to_email);
                    intent.putExtra("to_message",to_message);
                    intent.putExtra("gift_card_amount_id",gift_card_amount_id);
                    intent.putExtra("delivery_method",delivery_method);
                    intent.putExtra("itbms_percent",ITBMS_percent);
                    startActivity(intent);
                }
                break;

            case R.id.img_back:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                 break;
        }
    }
}
