package com.suvlas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SafeWebServices.PaymentGateway.PGEncrypt;
import com.SafeWebServices.PaymentGateway.PGKeyedCard;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.CallingMethod;
import common.CardType;
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
 * Created by hp on 3/7/2017.
 */

public class TransactionSucessActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    String amount,card_name,card_number,card_cvv,card_exp,gift_card_id,from_email,to_email,to_message,transaction_id,
            gift_card_amount_id,delivery_method;
    TextView txt_total_price,txt_card2,txt_tax,txt_total_price_with_tax,txt_itbms,txt_username;
    PGEncrypt pgEncrypt;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Request_loader loader;
    Double Total_amount_with_tax;
    String crypted;
    String payment_url = "https://secure.networkmerchants.com/api/transact.php";
    Button txt_continue;
    MCrypt mCrypt;
    Double ITBMS_percent;
    ProgressDialog progressDialog;
    String firstname,lastnamre;
    ImageView img_creditcatrd_type;
    TextView txt_cardnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.transaction);

        // initialize all component
        findviewID();

        //initialize component
        init();

        // onclick listener method for required components
        set_listeners();
        //set_font();


    }

    private void findviewID() {

        img_back = (ImageView) findViewById(R.id.img_back);
        txt_total_price = (TextView) findViewById(R.id.txt_total_price);
        txt_card2 = (TextView) findViewById(R.id.txt_card2);
        txt_tax = (TextView) findViewById(R.id.txt_tax);
        txt_total_price_with_tax = (TextView)findViewById(R.id.txt_total_price_with_tax);
        txt_continue = (Button) findViewById(R.id.txt_continue);
        txt_itbms = (TextView) findViewById(R.id.txt_itbms);
        txt_username = (TextView)findViewById(R.id.txt_username);
        txt_cardnumber = (TextView)findViewById(R.id.txt_cardnumber);

        loader = new Request_loader(TransactionSucessActivity.this);
        comman_dialog = new Comman_Dialog(TransactionSucessActivity.this);
        sharedPrefs = new SharedPrefs(TransactionSucessActivity.this);

        img_creditcatrd_type = (ImageView)findViewById(R.id.img_creditcatrd_type);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        txt_continue.setOnClickListener(this);
    }

    private void init() {

        mCrypt = new MCrypt();
        progressDialog = new ProgressDialog(TransactionSucessActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setMessage(getResources().getString(R.string.during_transaction));
        progressDialog.setCancelable(false);

        amount = getIntent().getStringExtra("gift_card_amount");
        card_name = getIntent().getStringExtra("card_holder_name");
        card_number= getIntent().getStringExtra("card_number");
        card_cvv = getIntent().getStringExtra("card_cvv");
        card_exp = getIntent().getStringExtra("card_exp");
        gift_card_id = getIntent().getStringExtra("gift_card_id");
        from_email = getIntent().getStringExtra("from_email");
        to_email = getIntent().getStringExtra("to_email");
        to_message = getIntent().getStringExtra("to_message");
        gift_card_amount_id = getIntent().getStringExtra("gift_card_amount_id");
        delivery_method = getIntent().getStringExtra("delivery_method");
        ITBMS_percent = Double.valueOf(getIntent().getStringExtra("itbms_percent"));

        txt_cardnumber.setText(getResources().getString(R.string.cardnumber)+" - "+sharedPrefs.get_Barcode_id());
        String cardtype = String.valueOf(CardType.detect(card_number));

        if (CardType.detect(card_number).equals(CardType.VISA))
        {
            img_creditcatrd_type.setImageResource(R.drawable.visa);
        }
        else if (CardType.detect(card_number).equals(CardType.MASTERCARD))
        {
            img_creditcatrd_type.setImageResource(R.drawable.master_card);
        }
        else if (CardType.detect(card_number).equals(CardType.AMERICAN_EXPRESS))
        {
            img_creditcatrd_type.setImageResource(R.drawable.amx);
        }
        else if (CardType.detect(card_number).equals(CardType.DISCOVER))
        {
            img_creditcatrd_type.setImageResource(R.drawable.discover);
        }
        else if (CardType.detect(card_number).equals(CardType.JCB))
        {
            img_creditcatrd_type.setImageResource(R.drawable.jcb);
        }
        else if (CardType.detect(card_number).equals(CardType.UNKNOWN))
        {
            img_creditcatrd_type.setImageResource(R.drawable.unknown_card);
        }

        txt_username.setText(sharedPrefs.get_User_Name());
        txt_itbms.setText(getString(R.string.itbms) +" "+ITBMS_percent+"%");

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
            Log.e("firstname",firstname);
            Log.e("lastname",lastnamre);
        }


        /*Log.e("amount",amount);
        Log.e("card_name",card_name);
        Log.e("card_number",card_number.replaceFirst(".{12}", "*****"));
        Log.e("card_Cvv",card_cvv);
        Log.e("card_Exp",card_exp);
        Log.e("gift_Card_id_transaction",gift_card_id);
        Log.e("from_email",from_email);
        Log.e("to_email",to_email);
        Log.e("to_message",to_message);
        Log.e("gift_card_amount_id",gift_card_amount_id);
        Log.e("delivery_method",delivery_method);*/



        txt_total_price.setText(amount);
        txt_card2.setText(card_number.replaceFirst(".{12}", "*****"));
        double total_amount = Double.parseDouble(amount.replace("$",""));

        double itbms_percentage = total_amount*(ITBMS_percent/100.0);
        DecimalFormat df = new DecimalFormat("00.00");

        Double tax = Double.valueOf(df.format(itbms_percentage));

        //txt_tax.setText(df.format(itbms_percentage));

        String formattedData = String.format("%.02f", itbms_percentage);
        Log.e("formateddata",formattedData);

        Total_amount_with_tax = total_amount;

        txt_total_price_with_tax.setText(Total_amount_with_tax+"");

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


        PGKeyedCard card = new PGKeyedCard(card_number, card_exp, card_cvv);
        crypted = pgEncrypt.encrypt(card, true);
        Log.e("crypted",crypted);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_continue:
                new Payment().execute();
        }

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

            try {
                OkHttpClient client ;

                client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                /*RequestBody reqbody = new FormBody.Builder()
                        .add("username", "suvlaslive")
                        .add("password", "LASsuv$55")
                        .add("amount", Total_amount_with_tax+"")
                        .add("encrypted_payment",crypted)
                        .build();*/
                /*RequestBody reqbody = new FormBody.Builder()
                        .add("username", "suvlaslive")
                        .add("password", "suvlas123456")
                        .add("firstname", firstname)
                        .add("lastname", lastnamre)
                        .add("type","sale")
                        .add("amount", Total_amount_with_tax+"")
                        .add("encrypted_payment",crypted)
                        .build();*/

                RequestBody reqbody = new FormBody.Builder()
                        .add("username", "suvlaslive")
                        .add("password", "suvlas98765")
                        .add("firstname", firstname)
                        .add("lastname", lastnamre)
                        .add("type","sale")
                        .add("amount", Total_amount_with_tax+"")
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
                        Toast.makeText(getBaseContext(),getResources().getString(R.string.payment_successfull),Toast.LENGTH_SHORT).show();

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

                            new send_gift_card().execute();
                        }
                    }
                    else
                    {
                        //loader.hidepDialog();
                        progressDialog.hide();
                        Toast.makeText(getBaseContext(),getResources().getString(R.string.payment_unsuccessfull),Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (Exception e) {
                Log.e("exception_payment", e.toString());
            }
            //hide loader
            //loader.hidepDialog();
        }
    }

    private class send_gift_card extends AsyncTask<String, Void, String> {

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
                OkHttpClient client = new OkHttpClient();

                Log.e("apikey",Comman_url.api_key);
                Log.e("userID",MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("from_email",MCrypt.bytesToHex(mCrypt.encrypt(from_email)));
                Log.e("to_email",MCrypt.bytesToHex(mCrypt.encrypt(to_email)));
                /*Log.e("amount",MCrypt.bytesToHex(mCrypt.encrypt(String.valueOf(Total_amount_with_tax))));*/
                Log.e("message",MCrypt.bytesToHex(mCrypt.encrypt(to_message)));
                Log.e("payment_method",MCrypt.bytesToHex(mCrypt.encrypt("giftcard")));
                Log.e("txn_id",MCrypt.bytesToHex(mCrypt.encrypt(transaction_id)));
                Log.e("giftcard_template",MCrypt.bytesToHex(mCrypt.encrypt(gift_card_id)));
                Log.e("giftcard_amount",MCrypt.bytesToHex(mCrypt.encrypt(gift_card_amount_id)));
                Log.e("delivery_method",MCrypt.bytesToHex(mCrypt.encrypt(delivery_method)));

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("from_email", MCrypt.bytesToHex(mCrypt.encrypt(from_email)))
                        .add("to_email",MCrypt.bytesToHex(mCrypt.encrypt(to_email)))
                        .add("message",MCrypt.bytesToHex(mCrypt.encrypt(to_message)))
                        .add("payment_method",MCrypt.bytesToHex(mCrypt.encrypt("giftcard")))
                        .add("txn_id",MCrypt.bytesToHex(mCrypt.encrypt(transaction_id)))
                        .add("giftcard_template",MCrypt.bytesToHex(mCrypt.encrypt(gift_card_id)))
                        .add("giftcard_amount",MCrypt.bytesToHex(mCrypt.encrypt(gift_card_amount_id)))
                        .add("delivery_method",MCrypt.bytesToHex(mCrypt.encrypt(delivery_method)))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.send_gift_card, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_payment", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_send_gift_card", result + "");

            try {
                if (result != null) {

                    JSONObject send_gift_object = new JSONObject(result);
                    Toast.makeText(getApplicationContext(),send_gift_object.getString("message"),Toast.LENGTH_SHORT).show();

                    if (send_gift_object.getString("code").equalsIgnoreCase("200"))
                    {
                        Intent i = new Intent(TransactionSucessActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Transaction unsuccessfull",Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("exception_send_gift_card", e.toString());
            }
            //hide loader
            //loader.hidepDialog();
            progressDialog.hide();
        }
    }
}