package com.suvlas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Expirydatepicker.DatePickerPopWin;
import adpter.GiftCardAmountAdapter;
import bean.PriceId;
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
 * Created by hp on 6/16/2017.
 */

public class PayReloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    RelativeLayout rel_procced;
    Toolbar toolbar;
    ImageView img_back;
    static final int DATE_DIALOG_ID = 1;
    private int mYear = 2013;
    private int mMonth = 5;
    private int mDay = 30;
    EditText cardnumber;
    int keyDel;
    String a;
    private boolean lock;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    MCrypt mCrypt;
    ImageView img_card;
    TextView txt_balance,txt_card_no,txt_as;
    RecyclerView rel_wallet_amount;
    Button txt_add_wallet_amnt;
    LinearLayoutManager horizontalLayoutManagaer;
    ArrayList<PriceId> price_item = new ArrayList<>();
    GiftCardAmountAdapter giftCardAmountAdapter;
    EditText edt_cardname,securitycode,expirationdate;
    String wallet_amount;
    String Card_Number;
    String formattedDate,formattedTime;
    int year,month,maxyear;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_reload);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
        //set_font();


    }

    private void findviewID() {
        rel_procced = (RelativeLayout) findViewById(R.id.rel_procced);
        img_back = (ImageView) findViewById(R.id.img_back);
        expirationdate = (EditText) findViewById(R.id.expirationdate);
        cardnumber = (EditText) findViewById(R.id.cardnumber);
        edt_cardname = (EditText)findViewById(R.id.edt_cardname);
        securitycode = (EditText)findViewById(R.id.securitycode);
        expirationdate = (EditText)findViewById(R.id.expirationdate);

        img_card = (ImageView)findViewById(R.id.img_card);
        txt_balance = (TextView)findViewById(R.id.txt_balance);
        txt_as = (TextView)findViewById(R.id.txt_as);
        txt_card_no = (TextView)findViewById(R.id.txt_card_no);
        rel_wallet_amount = (RecyclerView)findViewById(R.id.rel_wallet_amount);
        txt_add_wallet_amnt = (Button)findViewById(R.id.txt_add_wallet_amnt);
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
                DatePickerPopWin pickerPopWin  = new DatePickerPopWin.Builder(PayReloadActivity.this, new DatePickerPopWin.OnDatePickedListener() {
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
                pickerPopWin.showPopWin(PayReloadActivity.this);
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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        Log.e("month",month+"");
        Log.e("year",year+"");
        maxyear = year+100;

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.e("date",date);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm a");
        formattedTime = df1.format(c.getTime());

        horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rel_wallet_amount.setLayoutManager(horizontalLayoutManagaer);

        loader = new Request_loader(this);
        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);
        mCrypt = new MCrypt();

        new reload_card().execute();
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

                if (txt_add_wallet_amnt.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert(getResources().getString(R.string.select_amount));
                }
                else if (edt_cardname.getText().toString().equalsIgnoreCase(""))
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
                    Intent intent = new Intent(PayReloadActivity.this, TransactionSuccessWalletActivity.class);
                    intent.putExtra("wallet_amount",wallet_amount);
                    intent.putExtra("card_holder_name",edt_cardname.getText().toString());
                    intent.putExtra("card_number",cardnumber.getText().toString().replace(" ",""));
                    intent.putExtra("card_cvv",securitycode.getText().toString());
                    intent.putExtra("card_exp",expirationdate.getText().toString());
                    intent.putExtra("card",Card_Number);
                    /*intent.putExtra("gift_card_amount",gift_card_amount);
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
                    intent.putExtra("itbms_percent",ITBMS_percent);*/
                    startActivity(intent);
                }


                break;
            case R.id.img_back:
                finish();
//                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                Fragment fragment = new PayManageFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.payreload_frame, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
//            case R.id.expirationdate:
//                createDialogWithoutDateField().show();
//                break;
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

                    price_item = new ArrayList<>();

                    JSONObject reload_card_object = new JSONObject(result);

                    if (reload_card_object.getString("code").equalsIgnoreCase("200"))
                    {
                        /*Glide.with(PayReloadActivity.this).load(reload_card_object.getString("card")).placeholder(R.drawable.loading).error(R.drawable.loading).into(img_card);*/
                        Glide.with(PayReloadActivity.this).load(reload_card_object.getString("card")).into(img_card);

                        JSONObject wallet_object = reload_card_object.getJSONObject("data");

                        txt_balance.setText(getString(R.string.balance)+" "+wallet_object.getString("wallet_amount"));
                        txt_card_no.setText(wallet_object.getString("barcode").replaceFirst(".{09}", "*****"));

                        txt_as.setText(getString(R.string.as)+" "+formattedDate+" " + formattedTime);
                        Card_Number = wallet_object.getString("barcode");

                        JSONArray wallet_amt_jsonarray = reload_card_object.getJSONArray("giftcardAmount");

                        for (int i =0 ; i< wallet_amt_jsonarray.length();i++)
                        {

                            JSONObject wallet_amt_obj = wallet_amt_jsonarray.getJSONObject(i);
                            price_item.add(new PriceId(wallet_amt_obj.getString("id"), wallet_amt_obj.getString("price")));

                        }
                        giftCardAmountAdapter = new GiftCardAmountAdapter(PayReloadActivity.this, price_item, txt_add_wallet_amnt, new OnClickListenerAmount() {
                            @Override
                            public void onItemClicked(int position,String amount) {
                                Log.e("position_gift",position+"");
                                Log.e("position_Wallet_amt",amount);
                                wallet_amount = amount;
                                //gift_card_amount = String.valueOf(position);
                            }
                        });
                        rel_wallet_amount.setAdapter(giftCardAmountAdapter);
                        Log.e("wallet_amt_jsonarray",wallet_amt_jsonarray+"");
                        Log.e("pricelength",price_item.size()+"");

                    }
                    else
                    {
                        Toast.makeText(PayReloadActivity.this,reload_card_object.getString("message"),Toast.LENGTH_SHORT).show();
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
