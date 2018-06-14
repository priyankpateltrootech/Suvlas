package com.suvlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.Transaction;

/**
 * Created by hp on 5/2/2017.
 */

public class ReloadTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RelativeLayout rel_procced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reload_transaction_activity);

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
        rel_procced=(RelativeLayout)findViewById(R.id.rel_procced);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        rel_procced.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
//            case R.id.rel_procced:
//                Intent intent = new Intent(this, TransactionSucessActivity.class);
//                startActivity(intent);
//                break;
        }

    }
}
