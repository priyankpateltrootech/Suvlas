package com.suvlas;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adpter.TransferbalanceAdapter;
import bean.TransferBalanceBean;
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
 * Created by hp on 11/27/2017.
 */

public class FeedbackDialog extends Dialog {

    CommentReplyDialoglistener commentReplyDialoglistener;
    Request_loader loader;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    Comman_Dialog comman_dialog;
    Context context;
    EditText edt_feedback;
    TextView txt_cancel,txt_save;

    public FeedbackDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedback_dialog);

        findviewid();

        init();

        set_listener();
    }

    private void findviewid()
    {
        edt_feedback = (EditText)findViewById(R.id.edt_feedback);
        txt_cancel = (TextView)findViewById(R.id.txt_cancel);
        txt_save = (TextView)findViewById(R.id.txt_save);
    }
    private void init()
    {
        loader = new Request_loader(context);
        mCrypt = new MCrypt();
        sharedPrefs = new SharedPrefs(context);
        comman_dialog = new Comman_Dialog(context);
    }
    private void set_listener()
    {
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_feedback.getText().toString().trim().equalsIgnoreCase(""))
                {
                    comman_dialog.Show_alert("Please Write Feedback");
                }
                else
                {
                    new feedback(edt_feedback.getText().toString()).execute();
                }

            }
        });
    }

    public void setDialogResult(CommentReplyDialoglistener dialogResult){
        commentReplyDialoglistener = dialogResult;
    }

    public interface CommentReplyDialoglistener{
        void finish(String message);
    }

    private class feedback extends AsyncTask<String, Void, String> {

        String Responce_jason = "";
        String feedback_text;

        public feedback(String feedback_text) {
            this.feedback_text = feedback_text;
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
                        .add("feedback_msg", MCrypt.bytesToHex(mCrypt.encrypt(feedback_text)))
                        .build();

                Responce_jason = CallingMethod.POST(client, Comman_api_name.feedback, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_history", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("result_history", result + "");

            try {
                if (result != null) {

                    JSONObject jsonObject = new JSONObject(result);

                    String code = jsonObject.getString("code");

                    if (code.equalsIgnoreCase("200"))
                    {
                        dismiss();
                        Toast.makeText(context,"Feedback Send Successfully",Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (Exception e) {
                Log.e("exception_history", e.toString());
            }
            //hide loader
            loader.hidepDialog();
        }
    }
}
