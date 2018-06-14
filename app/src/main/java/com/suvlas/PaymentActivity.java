package com.suvlas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SafeWebServices.PaymentGateway.PGEncrypt;
import com.SafeWebServices.PaymentGateway.PGEncryptedSwipedCard;
import com.SafeWebServices.PaymentGateway.PGKeyedCard;
import com.SafeWebServices.PaymentGateway.PGSwipedCard;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Expirydatepicker.DatePickerPopWin;
import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import common.PgPaymentCard;
import common.Request_loader;
import common.SharedPrefs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class PaymentActivity extends AppCompatActivity {

    EditText edt_card,cardnumber,securitycode,expirationdate;
    RelativeLayout rel_procced;
    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    private boolean lock;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    PGEncrypt pgEncrypt;
    String crypted,Total_amount;
    String payment_url = "https://secure.networkmerchants.com/api/transact.php";
    ImageView img_back_payment;
    String order_id,store_id,total_amount,transaction_id,store_api_key;
    ProgressDialog progressDialog;
    int year,month,maxyear;
    String date;
    String firstname,lastnamre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
    }

    private void findviewID()
    {
        edt_card = (EditText)findViewById(R.id.edt_card);
        cardnumber = (EditText)findViewById(R.id.cardnumber);
        securitycode = (EditText)findViewById(R.id.securitycode);
        expirationdate = (EditText)findViewById(R.id.expirationdate);

        rel_procced = (RelativeLayout)findViewById(R.id.rel_procced);

        img_back_payment = (ImageView)findViewById(R.id.img_back_payment);
    }

    private void init()
    {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        Log.e("month",month+"");
        Log.e("year",year+"");
        maxyear = year+100;

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.e("date",date);
        store_id = getIntent().getStringExtra("store_id");
        store_api_key = getIntent().getStringExtra("store_api_key");
        order_id = getIntent().getStringExtra("order_id");
        total_amount = getIntent().getStringExtra("total_amount");

        Log.e("store","store"+store_id+"order"+order_id+"total"+total_amount);

        loader = new Request_loader(this);
        comman_dialog = new Comman_Dialog(this);
        sharedPrefs = new SharedPrefs(this);
        mCrypt = new MCrypt();
        progressDialog = new ProgressDialog(PaymentActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setMessage(getResources().getString(R.string.during_transaction));
        progressDialog.setCancelable(false);


        pgEncrypt = new PGEncrypt();

        pgEncrypt.setKey("***6683|MIIERzCCAy+gAwIBAgIBADANBgkqhkiG9w0BAQUFADCBvTELMAkGA1UE" +
                "BhMCVVMxETAPBgNVBAgMCElsbGlub2lzMRMwEQYDVQQHDApTY2hhdW1idXJnMRgw" +
                "FgYDVQQKDA9TYWZlV2ViU2VydmljZXMxHjAcBgNVBAsMFUVuZC10by1lbmQgZW5j" +
                "cnlwdGlvbjEgMB4GA1UEAwwXd3d3LnNhZmV3ZWJzZXJ2aWNlcy5jb20xKjAoBgkq" +
                "hkiG9w0BCQEWG3N1cHBvcnRAc2FmZXdlYnNlcnZpY2VzLmNvbTAeFw0xODAyMTcw" +
                "NDQyNDhaFw0xODAyMTgwNDQyNDhaMIG9MQswCQYDVQQGEwJVUzERMA8GA1UECAwI" +
                "SWxsaW5vaXMxEzARBgNVBAcMClNjaGF1bWJ1cmcxGDAWBgNVBAoMD1NhZmVXZWJT" +
                "ZXJ2aWNlczEeMBwGA1UECwwVRW5kLXRvLWVuZCBlbmNyeXB0aW9uMSAwHgYDVQQD" +
                "DBd3d3cuc2FmZXdlYnNlcnZpY2VzLmNvbTEqMCgGCSqGSIb3DQEJARYbc3VwcG9y" +
                "dEBzYWZld2Vic2VydmljZXMuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB" +
                "CgKCAQEAu0ahdezF2gecfTQ0bRh261ZK/b346fXfEW4vBrP7UlOREWKdJE/4SVkn" +
                "14IdjthpIUvXUA0VFs7+CIhWnTnvOTIAmBBEuRYQbvspHKuzHGaUGc2qiIt+e9wL" +
                "AxSbwe7SNCnsf/9IrqMlDTh73SqsR241VwawjVXUPW4JiqQjpb+F37Zdu+n+va5E" +
                "aGa9fKzq1z4RzfPIptWH7iiMiuPTL5Ju4kBHoLjVFhG6abLBbUZVkWACl8ISFh5n" +
                "obp5IufjfMrqIS9nqWqL2jllUTKG3IAyebm6skXOEJurFPkLGx/NVmx3ORp9e2Qb" +
                "i9iudngR2AGZKiBlScQOFSeqmRavNQIDAQABo1AwTjAdBgNVHQ4EFgQUYreuy7At" +
                "87mdUN8rL/Bq2GBwIqYwHwYDVR0jBBgwFoAUYreuy7At87mdUN8rL/Bq2GBwIqYw" +
                "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEALkFNkwkt8Kp75jI6m5MD" +
                "N6akSUch5dDiaIXN4vZsolfAGkN0tCqBkmR5aU+/G8YZ20ebJLGh4miJG3z34rTp" +
                "0EBHFKhugpc9lAQsaZTUc6e19f5k+nraMWtUO1OWV2Wi23ZhujAmGmLMMgEWJfGO" +
                "EaTqgbhzzzJ2qRDnB8iSonmHb8QWwKQ9V0ElOMdME7oMxjQ3YZOCq5yVQIimN2F4" +
                "Y5fO3H8CBrP61xBiEjPJLP7kvoZ3/Aey/aqVYEIA0AbpUCCKHr+ngDCugAZENseK" +
                "dFtP54rKxnlznTXplzcju3KdW1lmyrhu693EYMP3WRwCOQ30iJHFgv6/R5T71aSj" +
                "zw==***");

        Total_amount = getIntent().getStringExtra("total_amount").replace("$ ","");
        Log.e("amount_out",Total_amount);


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

    private void set_listeners()
    {
        img_back_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        expirationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin  = new DatePickerPopWin.Builder(PaymentActivity.this, new DatePickerPopWin.OnDatePickedListener() {
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
                pickerPopWin.showPopWin(PaymentActivity.this);
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

        rel_procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String card_name = edt_card.getText().toString();

                if (card_name.contains(" "))
                {
                    String[] seperated =  card_name.split(" ");

                    firstname = seperated[0];
                    lastnamre = seperated[1];
                    Log.e("firstname",firstname);
                    Log.e("lastname",lastnamre);
                }
                else
                {
                    firstname = card_name;
                    lastnamre = card_name;
                }


                String card_number = cardnumber.getText().toString().replace(" ","");
                String card_cvv = securitycode.getText().toString();
                String card_exp = expirationdate.getText().toString().replace("/","");

                PGKeyedCard card = new PGKeyedCard(card_number, card_exp, card_cvv);
                //PgPaymentCard card = new PgPaymentCard(card_name,card_number, card_exp, card_cvv);
                crypted = pgEncrypt.encrypt(card, true);

                Log.e("crypted",crypted);


                if (edt_card.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert("Please Enter Cardholder Name");
                }
                else if (cardnumber.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert("Please Enter Card Number");
                }
                else if (securitycode.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert("Please Enter CVV/CVV2 Number");
                }
                else if (expirationdate.getText().toString().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert("Please Enter Expiry Date");
                }
                else if (expirationdate.getText().toString().length()!=5)
                {
                    comman_dialog.Show_alert("Please Enter Valid Expiry Date");
                }
                else if(Integer.parseInt(expirationdate.getText().toString().substring(0,2))>12)
                {
                    comman_dialog.Show_alert("Please Enter Valid Expiry Date");
                }
                else
                {
                    new Payment().execute();

                }
            }
        });
    }

    private class Payment extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //show loader
            //loader.showpDialog();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            Log.e("amount",Total_amount);
            try {
                OkHttpClient client ;

                client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                /*RequestBody reqbody = new FormBody.Builder()
                        .add("username", "suvlaslive")
                        .add("password", "suvlas123456")
                        .add("firstname", firstname)
                        .add("lastname", lastnamre)
                        .add("type","sale")
                        .add("amount", Total_amount)
                        .add("encrypted_payment",crypted)
                        .build();*/

                RequestBody reqbody = new FormBody.Builder()
                        .add("username", "suvlaslive")
                        .add("password", "suvlas98765")
                        .add("firstname", firstname)
                        .add("lastname", lastnamre)
                        .add("type","sale")
                        .add("amount", Total_amount)
                        .add("encrypted_payment",crypted)
                        .build();

                Responce_jason = CallingMethod.payment_POST(client, payment_url, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_payment", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_payment", result + "");

            try {
                if (result != null) {

                    String success = result.substring(result.length()-3);
                    Log.e("result",success);

                    if (success.equalsIgnoreCase("100"))
                    {
                        Toast.makeText(getBaseContext(),"Pago exitoso",Toast.LENGTH_SHORT).show();
                        String txt=result;
                        String re1=".*?";	// Non-greedy match on filler
                        String re2="=";	// Uninteresting: c
                        String re3=".*?";	// Non-greedy match on filler
                        String re4="=";	// Uninteresting: c
                        String re5=".*?";	// Non-greedy match on filler
                        String re6="=";	// Uninteresting: c
                        String re7=".*?";	// Non-greedy match on filler
                        String re8="(=)";	// Any Single Character 1
                        String re9="(\\d+)";	// Integer Number 1

                        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        Matcher m = p.matcher(txt);
                        if (m.find())
                        {
                            String c1=m.group(1);
                            String int1=m.group(2);
                            Log.e("c1_transaction",c1.toString());
                            Log.e("int1_transaction",int1.toString());
                            System.out.print("("+c1.toString()+")"+"("+int1.toString()+")"+"\n");

                            transaction_id = int1.toString();

                            new confirm_payment().execute();
                        }

                    }
                    else
                    {
                        //loader.hidepDialog();
                        progressDialog.hide();
                        Toast.makeText(getBaseContext(),"Pago sin éxito",Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (Exception e) {
                Log.e("exception_payment", e.toString());
                //loader.hidepDialog();
                progressDialog.hide();
            }
            //hide loader

        }
    }

    private class confirm_payment extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //show loader
            //loader.showpDialog();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client;

                client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("orders_id", MCrypt.bytesToHex(mCrypt.encrypt(order_id)))
                        .add("stores_id", MCrypt.bytesToHex(mCrypt.encrypt(store_id)))
                        .add("total_amount", MCrypt.bytesToHex(mCrypt.encrypt(total_amount)))
                        .add("txn_id", MCrypt.bytesToHex(mCrypt.encrypt(transaction_id)))
                        .add("currency_code", MCrypt.bytesToHex(mCrypt.encrypt("dollar")))
                        .add("payment_method", MCrypt.bytesToHex(mCrypt.encrypt("credit_debit_cards")))
                        .add("timezone", MCrypt.bytesToHex(mCrypt.encrypt(TimeZone.getDefault().getID())))
                        .add("stores_api_key", MCrypt.bytesToHex(mCrypt.encrypt(store_api_key)))
                        .build();

                Log.e("user_id",MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("orders_id",MCrypt.bytesToHex(mCrypt.encrypt(order_id)));
                Log.e("stores_id",MCrypt.bytesToHex(mCrypt.encrypt(store_id)));
                Log.e("total_amount",MCrypt.bytesToHex(mCrypt.encrypt(total_amount)));
                Log.e("txn_id",MCrypt.bytesToHex(mCrypt.encrypt(transaction_id)));
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

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        sharedPrefs.save_Custom_array("");
                        sharedPrefs.save_Custom_menu_array("");
                        sharedPrefs.save_Custom_menu_child_array("");
                        sharedPrefs.save_Custom_menu_group_array("");
                        sharedPrefs.save_Custom_combo_item_array("");
                        sharedPrefs.save_Custom_combo_group_array("");
                        sharedPrefs.save_CategoryMenu_array("");
                        sharedPrefs.save_CategoryMenuItem_array("");
                        Toast.makeText(getBaseContext(),"Su orden colocada con éxito",Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(PaymentActivity.this,MainActivity.class);
                       startActivity(i);


                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (Exception e) {
                Log.e("exception_confirm_payment", e.toString());
                //loader.hidepDialog();
                progressDialog.hide();
            }
            //hide loader
            //loader.hidepDialog();
            progressDialog.hide();
        }
    }
}
