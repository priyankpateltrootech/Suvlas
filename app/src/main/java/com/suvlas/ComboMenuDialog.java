package com.suvlas;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.ComboModifier;
import bean.MenuOrderBean;
import common.SharedPrefs;

/**
 * Created by hp on 11/23/2017.
 */

public class ComboMenuDialog extends Dialog{

    String combo_name,combo_modifier_name,combo_modifier_price,combo_modifier_id;
    TextView txt_custom_menu_combo_name,txt_combo_modifier_price;
    CheckBox checkbox_combo;
    Button btn_cancel,btn_continue;
    String str_selected_menu_arraylist;
    Gson gson;
    SharedPrefs sharedPrefs;
    JSONObject jo;
    JSONArray ja;
    JSONObject mainObj;
    OnMyDialogResult mDialogResult;
    ArrayList<String> combo_modifier_idlist;
    ArrayList<ComboModifier> comboModifier;
    int menuposition;
    String Checkbox_State;

    public ComboMenuDialog(Context context,String combo_name,String combo_modifier_name,String combo_modifier_price,String combo_modifier_id,ArrayList<String> combo_modifier_idlist,ArrayList<ComboModifier> comboModifier)
    {
        super(context);
        this.combo_name = combo_name;
        this.combo_modifier_name = combo_modifier_name;
        this.combo_modifier_price = combo_modifier_price;
        this.combo_modifier_id = combo_modifier_id;
        this.combo_modifier_idlist = combo_modifier_idlist;
        this.comboModifier = comboModifier;
        gson = new Gson();
        sharedPrefs = new SharedPrefs(context);
        jo = new JSONObject();
        ja = new JSONArray();
        mainObj = new JSONObject();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_menu_dialog);

        findviewid();

        init();

        set_listener();
    }

    private void findviewid()
    {
        txt_custom_menu_combo_name = (TextView)findViewById(R.id.txt_custom_menu_combo_name);
        txt_combo_modifier_price = (TextView)findViewById(R.id.txt_combo_modifier_price);
        checkbox_combo = (CheckBox)findViewById(R.id.checkbox_modifier);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_continue = (Button)findViewById(R.id.btn_continue);
    }
    private void init()
    {
        for (int i = 0 ; i< combo_modifier_idlist.size();i++)
        {
            Log.e("combo_modifier_idlist_dialog",combo_modifier_idlist.get(i));
        }
        txt_custom_menu_combo_name.setText(combo_name);
        txt_combo_modifier_price.setText("$ "+combo_modifier_price);
        checkbox_combo.setText(combo_modifier_name);

        if (combo_modifier_idlist.contains(combo_modifier_id))
        {
            Log.e("idddddd",combo_modifier_id);
            checkbox_combo.setChecked(true);
        }

        Log.e("combomodifier",comboModifier.size()+"");
    }
    private void set_listener()
    {

        checkbox_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkbox_combo.isChecked())
                {
                    Log.e("checked","checked");
                    //Boolean status = combo_modifier_idlist.contains(combo_modifier_id);
                    //Log.e("status_checkbox",status+"");
                    Checkbox_State = "Checked";
                    combo_modifier_idlist.add(combo_modifier_id);
                    comboModifier.add(new ComboModifier(combo_modifier_id,combo_modifier_name,combo_modifier_price,combo_name));

                }
                else
                {
                    Checkbox_State = "unChecked";
                    Log.e("unchecked","unchecked");
                    for (int i = 0 ; i< combo_modifier_idlist.size();i++)
                    {
                        if (comboModifier.get(i).getId().equalsIgnoreCase(combo_modifier_id))
                        {
                            combo_modifier_idlist.remove(i);
                            comboModifier.remove(i);
                            break;
                        }
                    }

                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDialogResult != null)
                {
                    mDialogResult.finish(combo_modifier_name,combo_modifier_id,combo_modifier_price,combo_name,combo_modifier_idlist,comboModifier,Checkbox_State);
                }

                dismiss();

                /*if (!checkbox_combo.isChecked())
                {
                    Toast.makeText(getContext(),"please select item",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(mDialogResult != null)
                    {
                        mDialogResult.finish(combo_modifier_name,combo_modifier_id,combo_modifier_price,combo_name);
                    }

                    dismiss();
                }*/

            }
        });
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String name,String id,String price,String combo_name,ArrayList<String> combo_modifier_idlist,ArrayList<ComboModifier> comboModifier,String checkboxstate);
    }

}
