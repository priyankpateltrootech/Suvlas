package adpter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.suvlas.MainActivity;
import com.suvlas.MessageActivity;
import com.suvlas.MessageDetailActivity;
import com.suvlas.OfferDetailsActivity;
import com.suvlas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import bean.MainOrderlistItem;
import bean.MessageItem;
import common.CallingMethod;
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
 * Created by hp on 6/22/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Activity activity;
    ArrayList<MessageItem> item;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    public String msg_status;

    public MessageAdapter(Activity activity, ArrayList<MessageItem> item) {
        super();
        this.activity = activity;
        this.item = item;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, null);
        MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.ViewHolder viewHolder, final int position) {
        mCrypt = new MCrypt();
        loader = new Request_loader(activity);
        sharedPrefs = new SharedPrefs(activity);
        comman_dialog = new Comman_Dialog(activity);

        final MessageItem object = item.get(position);
        msg_status = object.getMessage_status();

        if (msg_status.equalsIgnoreCase("Unread")) {
            viewHolder.text_date.setTextColor(ContextCompat.getColor(activity, R.color.loginbg));
            viewHolder.txt_msgdes1.setTextColor(ContextCompat.getColor(activity, R.color.loginbg));
            viewHolder.relative_scnd.setBackgroundColor(ContextCompat.getColor(activity, R.color.unreadmsgbg));
            viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(activity, R.color.loginbg));
            viewHolder.back_right.setBackground(ContextCompat.getDrawable(activity, R.drawable.right_arrow));
        } else {
            viewHolder.text_date.setTextColor(ContextCompat.getColor(activity, R.color.read_text));
            viewHolder.txt_msgdes1.setTextColor(ContextCompat.getColor(activity, R.color.read_text));
            viewHolder.relative_scnd.setBackgroundColor(ContextCompat.getColor(activity, R.color.readmsgbg));
            viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(activity, R.color.read_text));
            viewHolder.back_right.setBackground(ContextCompat.getDrawable(activity, R.drawable.right_arrow_blue_dark));
        }
        viewHolder.text_date.setText(object.getMsg_date());
        viewHolder.txt_msgdes1.setText(object.getMsg_title());
//            Glide.with(activity).load(object.getMsg_descriptiong()).into(viewHolder.img_msg);

        viewHolder.relative_scnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefs.save_Message_id(object.getNotification_id());
                if (object.getMessage_status().equalsIgnoreCase("Unread")) {
                    MessageActivity.j = MessageActivity.j - 1;
                    if (MessageActivity.j == 0) {
                        MessageActivity.txt_remainmsg.setVisibility(View.GONE);
                    } else if (MessageActivity.j < 10) {
                        MessageActivity.txt_remainmsg.setText("Tiene " + "0" + MessageActivity.j + " mensajes nuevos");
                    } else {
                        MessageActivity.txt_remainmsg.setText("Tiene " + MessageActivity.j + " mensajes nuevos");
                    }
                }
                //call notification status api
                if (CommanMethod.isInternetOn(activity)) {
                    new Notification_status().execute();
                } else {
                    comman_dialog.Show_alert(Comman_message.Dont_internet);
                }

                //data goes to messagedetails activity
                activity.startActivity(new Intent(activity, MessageDetailActivity.class)
                        .putExtra("msg_title", object.getMsg_title())
                        .putExtra("msg_date", object.getMsg_date())
                        .putExtra("msg_description", object.getMsg_description())
                        .putExtra("msg_image", object.getMessageimg_url())
                        .putExtra("offer_type", object.getOffers_type())
                        .putExtra("offer_title", object.getOffers_title())
                        .putExtra("offer_derscription", object.getOffers_description())
                        .putExtra("menu_item_name", object.getNombre())
                        .putExtra("expire_date", object.getExpire_date())
                        .putExtra("expire_time", object.getExpiretime())
                        .putExtra("offer_code", object.getOffer_code())
                        .putExtra("offer_qr_code", object.getOffer_qr_code())
                        .putExtra("offre_img_des", object.getOffer_image_detail()));

                viewHolder.text_date.setTextColor(ContextCompat.getColor(activity, R.color.read_text));
                viewHolder.txt_msgdes1.setTextColor(ContextCompat.getColor(activity, R.color.read_text));
                viewHolder.relative_scnd.setBackgroundColor(ContextCompat.getColor(activity, R.color.readmsgbg));
                viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(activity, R.color.read_text));
                viewHolder.back_right.setBackground(ContextCompat.getDrawable(activity, R.drawable.right_arrow_blue_dark));

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text_date, txt_msgdes1;
        public ImageView img_msg, back_right;
        public RelativeLayout relative_scnd;
        public View view_line;

        public ViewHolder(View itemView) {
            super(itemView);
//            img_msg = (ImageView) itemView.findViewById(R.id.img_msg);
            back_right = (ImageView) itemView.findViewById(R.id.back_right);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            txt_msgdes1 = (TextView) itemView.findViewById(R.id.txt_msgdes1);
            view_line = (View) itemView.findViewById(R.id.view_line);
            relative_scnd = (RelativeLayout) itemView.findViewById(R.id.relative_scnd);

        }
    }

    private class Notification_status extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("notification_id", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_Message_id())))
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("notification_id", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_Message_id())));
                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.change_notification_status, reqbody);

            } catch (Exception e) {
                Log.e("err_message", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("message", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        Log.e("message sucessful read", "");
                    } else {
//                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("err_message", e.toString());
            }
            loader.hidepDialog();
        }
    }
}

