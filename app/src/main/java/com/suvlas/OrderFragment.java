package com.suvlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hp on 3/3/2017.
 */

public class OrderFragment extends Fragment implements View.OnClickListener {


    View rootView;
    TextView txt_history,txt_card;
//    Toolbar toolbar;
    TabLayout tab;
    AppBarLayout apbar;
    TextView text_status, text_setting;
    RelativeLayout rel_top, rel_reward,relative_subtop,relative_reward,relative_top1;
    ImageView img_back, img_msg, img_bluetag,img_food1,img_food2,img_bluebarcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_fragment, container, false);

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
        img_back = (ImageView) rootView.findViewById(R.id.img_back);
        img_msg = (ImageView) getActivity().findViewById(R.id.img_msg);
//        tab = (TabLayout) getActivity().findViewById(R.id.tabs);
        text_status = (TextView) getActivity().findViewById(R.id.txt_status);
        rel_top = (RelativeLayout) getActivity().findViewById(R.id.rel_top);
        rel_reward = (RelativeLayout) rootView.findViewById(R.id.relative_reward);
        txt_history = (TextView) rootView.findViewById(R.id.txt_history);
        txt_card = (TextView) rootView.findViewById(R.id.txt_card);
        text_setting = (TextView) rootView.findViewById(R.id.txt_setting);
        img_bluetag = (ImageView) getActivity().findViewById(R.id.img_tag_blue);
        img_bluebarcode = (ImageView)rootView.findViewById(R.id.img_bluebarcode);
        img_food1 = (ImageView) rootView.findViewById(R.id.img_food1);
        img_food2 = (ImageView) rootView.findViewById(R.id.img_food2);
        relative_subtop=(RelativeLayout)rootView.findViewById(R.id.relative_subtop);
        relative_reward=(RelativeLayout)rootView.findViewById(R.id.relative_reward);
        relative_top1=(RelativeLayout)rootView.findViewById(R.id.relative_top1);
    }

    private void set_listeners() {
        txt_history.setOnClickListener(this);
        rel_reward.setOnClickListener(this);
        img_food1.setOnClickListener(this);
        img_food2.setOnClickListener(this);
        img_bluebarcode.setOnClickListener(this);
        relative_subtop.setOnClickListener(this);
        relative_reward.setOnClickListener(this);
        relative_top1.setOnClickListener(this);
        txt_card.setOnClickListener(this);
    }

    private void init() {
        tab.setBackgroundColor(getResources().getColor(R.color.white));
        text_status.setText(R.string.welcome);
        rel_top.setBackgroundColor(getResources().getColor(R.color.white));
//        img_back.setBackgroundDrawable( getResources().getDrawable(R.drawable.header_back));
//        img_msg.setBackgroundResource(R.drawable.header_msg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_history:
                Intent intent4 = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent4);
                break;
            case R.id.txt_card:
                Intent intent1 = new Intent(getActivity(), MyCards.class);
                startActivity(intent1);
                break;
            case R.id.img_food1:
                Intent intent2 = new Intent(getActivity(), RewardPontsActivity.class);
                startActivity(intent2);
                break;
            case R.id.img_food2:
                Intent intent5 = new Intent(getActivity(), RewardPontsActivity.class);
                startActivity(intent5);
                break;
            case R.id.relative_reward:
                Intent intent6 = new Intent(getActivity(), RewardPontsActivity.class);
                startActivity(intent6);
                break;
            case R.id.relative_subtop:

                Intent intent3 = new Intent(getActivity(), RewardPontsActivity.class);
                startActivity(intent3);
                break;
            case R.id.relative_top1:
                Intent intent = new Intent(getActivity(), GoldStatus.class);
                startActivity(intent);
                break;
        }
    }
}
