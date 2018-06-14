package com.suvlas;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hp on 5/1/2017.
 */

public class MenuCustomItem extends Fragment implements View.OnClickListener {


    View rootView;
    ImageView img_back, img_graytag;
    int counter = 0;
    boolean showText = false;
    TabLayout tab;
    CheckBox chk_box_pita;
    Button txt_continue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menucustomitem, container, false);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
        return rootView;


}

    private void findviewID() {
        tab = (TabLayout) getActivity().findViewById(R.id.tabs2);
        img_back = (ImageView)rootView.findViewById(R.id.img_back);
        chk_box_pita=(CheckBox)rootView.findViewById(R.id.chk_box_pita);
        txt_continue=(Button)rootView.findViewById(R.id.txt_continue);
//        img_graytag = (ImageView) findViewById(R.id.img_graytag);
    }

    private void set_listeners() {
//        img_back.setOnClickListener(this);
        txt_continue.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_continue:
                ChooseItem();
                break;
        }

    }

    private void ChooseItem() {

        final Dialog exit_dialog = new Dialog(getActivity());
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.setCancelable(false);
        exit_dialog.setContentView(R.layout.menu_custom_itemdetails);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(exit_dialog.getWindow().getAttributes());
        Point size = new Point();
       getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;
        lp.width = (display_width * 90) / 100;
        //lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        exit_dialog.getWindow().setAttributes(lp);

        Button btn_decrease, btn_increase;
        final TextView txt_desimal;

        btn_increase = (Button) exit_dialog.findViewById(R.id.btn_increase);
        btn_decrease = (Button) exit_dialog.findViewById(R.id.btn_decrease);
        txt_desimal = (TextView) exit_dialog.findViewById(R.id.txt_desimal);
        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                txt_desimal.setText(counter+"");
                Log.e("hello", String.valueOf(counter+""));
//                showText = true;
            }
        });
        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter--;
                    txt_desimal.setText(counter+"");
//                    showText = true;
                }
            }
        });
//        if (showText)
//            txt_desimal.setText(counter);


//        TextView txt_transc_type = (TextView) exit_dialog.findViewById(R.id.txt_transc_type);
//        TextView txt_account_number = (TextView) exit_dialog.findViewById(R.id.txt_account_number);
//        TextView txt_amount = (TextView) exit_dialog.findViewById(R.id.txt_amount2);
//        TextView txt_account_num = (TextView) exit_dialog.findViewById(R.id.txt_account_num);
//
//        txt_account_number.setText(accountNumber);
//        txt_amount.setText(String.valueOf(amount));
//        txt_transc_type.setText(isDirectPayment ? "DIRECT" : "PIN");
//        if (txt_transc_type.getText().toString().equals("PIN")) {
//            Log.e("pin", "");
//            txt_account_number.setVisibility(View.GONE);
//            txt_account_num.setVisibility(View.GONE);
//        }


//        Button btn_cancel = (Button) exit_dialog.findViewById(R.id.btn_cancel);
        Button btn_continue = (Button) exit_dialog.findViewById(R.id.btn_continue);

//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                exit_dialog.dismiss();
//            }
//        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
//                finish();
            }
        });
        exit_dialog.show();


    }
}