package com.suvlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 4/19/2017.
 */

public class GoldStatus extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    TextView txt_reward,txt_points,txt_since;
    String rewardpoint="",status="",statusupdatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstatus);

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
        txt_reward=(TextView) findViewById(R.id.txt_reward);
        txt_points=(TextView) findViewById(R.id.txt_points);
        txt_since=(TextView)findViewById(R.id.txt_since);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
    }

    private void init() {
        Intent intent = getIntent();
        rewardpoint=intent.getStringExtra("reward_point");
        txt_points.setText("PUNTOS "+rewardpoint);
        status=intent.getStringExtra("status");
        //txt_reward.setText(status+" STATUS");

        statusupdatedate=intent.getStringExtra("statusupdate_date");

        String[] parts = statusupdatedate.split(" ");
        String date = parts[0];

        SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = df_date.parse(date);
            df_date = new SimpleDateFormat("MM dd yyyy");
            String final_date = df_date.format(newDate);
            Log.e("final_date", final_date);

            txt_since.setText("DESDE "+final_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }

    }
}