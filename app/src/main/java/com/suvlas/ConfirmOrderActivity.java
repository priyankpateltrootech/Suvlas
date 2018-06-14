package com.suvlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by hp on 4/24/2017.
 */

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    Button btn_continue;
    RelativeLayout relative_third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.confirm_order);

        //initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
    }

    private void findviewID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_continue=(Button)findViewById(R.id.btn_continue);
        relative_third=(RelativeLayout)findViewById(R.id.relative_third);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        relative_third.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_continue:
                Intent intent = new Intent(this, ReloadTransactionActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_third:
                Intent intent2 = new Intent(this, RewardBonus.class);
                startActivity(intent2);
                break;
        }

    }
}