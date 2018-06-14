package com.suvlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 6/23/2017.
 */

public class MessageDetailActivity extends AppCompatActivity {
    ImageView img_offer, img_back, img_barcode;
    TextView txt_title, txt_omsg_des, txt_datetitle, txt_datetime, txt_menuname, txt_offer, txt_offercode;
    String msg_title = "", msg_date = "", msg_description = "", msg_image = "", offer_type = "", offer_title = "", offer_derscription = "",
            menu_item_name = "", expire_date = "", expire_time = "", offre_img_des = "", offer_code = "", offer_qr_code = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_detail);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_offer = (ImageView) findViewById(R.id.img_offer);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_omsg_des = (TextView) findViewById(R.id.txt_omsg_des);
        txt_datetitle = (TextView) findViewById(R.id.txt_datetitle);
        txt_datetime = (TextView) findViewById(R.id.txt_datetime);
        txt_menuname = (TextView) findViewById(R.id.txt_menuname);
        txt_offer = (TextView) findViewById(R.id.txt_offer);
        txt_offercode = (TextView) findViewById(R.id.txt_offercode);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);

    }

    private void set_listeners() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {

        //get data from message activity
        intent = getIntent();
        msg_title = intent.getStringExtra("msg_title");
        msg_date = intent.getStringExtra("msg_date");
        msg_description = intent.getStringExtra("msg_description");
        offer_type = intent.getStringExtra("offer_type");
        msg_image = intent.getStringExtra("msg_image");
        offer_code = intent.getStringExtra("offer_code");

        offer_title = intent.getStringExtra("offer_title");
        offer_derscription = intent.getStringExtra("offer_derscription");
        menu_item_name = intent.getStringExtra("menu_item_name");
        expire_date = intent.getStringExtra("expire_date");
        expire_time = intent.getStringExtra("expire_time");
        offre_img_des = intent.getStringExtra("offre_img_des");
        offer_qr_code = intent.getStringExtra("offer_qr_code");

        if (offer_type.equalsIgnoreCase("Message")) {
            txt_title.setText(msg_title);
            txt_omsg_des.setText(msg_description);
            if (!msg_image.equalsIgnoreCase("")) {
                /*Glide.with(this)
                        .load(msg_image)
                        .placeholder(R.drawable.dashboard_placeholder_img)
                        .error(R.drawable.dashboard_placeholder_img)
                        .into(img_offer);*/
                Glide.with(this)
                        .load(msg_image)
                        .into(img_offer);
            }
        } else {
            txt_menuname.setVisibility(View.VISIBLE);
            txt_datetitle.setVisibility(View.VISIBLE);
            txt_datetime.setVisibility(View.VISIBLE);
            txt_offer.setVisibility(View.VISIBLE);
            txt_offercode.setVisibility(View.VISIBLE);
            img_barcode.setVisibility(View.VISIBLE);

            txt_title.setText(offer_title);
            txt_omsg_des.setText(offer_derscription);
            String[] parts = expire_date.split(" ");
            String date = parts[0];
            SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = df_date.parse(date);
                df_date = new SimpleDateFormat("dd/MM/yyyy");
                String final_date = df_date.format(newDate);
                Log.e("final_date", final_date);
                txt_datetime.setText(final_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txt_menuname.setText(menu_item_name);
            txt_offercode.setText(offer_code);
            if (!offre_img_des.equalsIgnoreCase("")) {
                /*Glide.with(this)
                        .load(offre_img_des)
                        .placeholder(R.drawable.dashboard_placeholder_img)
                        .error(R.drawable.dashboard_placeholder_img)
                        .into(img_offer);*/
                Glide.with(this)
                        .load(offre_img_des)
                        .into(img_offer);
            }
            if (!offer_qr_code.equalsIgnoreCase("")) {
                Glide.with(this)
                        .load(offer_qr_code)
                        .into(img_barcode);
            }
        }
    }
}
