package com.suvlas;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adpter.ModifierAdapter;
import bean.CategoryMenu;
import bean.CategoryMenuModifier;
import bean.ComboModifier;
import bean.OrderModifier;
import common.SharedPrefs;

/**
 * Created by hp on 11/23/2017.
 */

public class MenuModifierDialog extends Dialog{

    String menu_name,modifier_name,modifier_price,modifier_id;
    TextView txt_custom_menu_combo_name;
    Button btn_cancel,btn_continue;
    SharedPrefs sharedPrefs;
    OnMyDialogResult mDialogResult;
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList_shared_pre_array;
    Gson gson;
    RecyclerView recyclerview_modifier;
    ArrayList<OrderModifier> orderModifierArrayList;
    ModifierAdapter modifierAdapter;
    Context context;
    ArrayList<OrderModifier> selectedorderModifierArrayList;
    ArrayList<String> removemodifieridlist;
    ArrayList<String> categorymodifieridarraylistshared_pre;
    ArrayList<String> categorymodifieridarraylist;
    String minimum_quantity;

    public MenuModifierDialog(Context context,String minimum_quantity,String menu_name,ArrayList<OrderModifier> orderModifierArrayList,ArrayList<String> categorymodifieridarraylist)
    {
        super(context);
        this.context = context;
        this.minimum_quantity = minimum_quantity;
        this.menu_name = menu_name;
        this.orderModifierArrayList = orderModifierArrayList;
        this.categorymodifieridarraylist = categorymodifieridarraylist;
        selectedorderModifierArrayList = new ArrayList<>();
        gson = new Gson();
        sharedPrefs = new SharedPrefs(getContext());


        removemodifieridlist = new ArrayList<>();

        selectedorderModifierArrayList = new ArrayList<>();

        categoryMenuModifierArrayList_shared_pre_array = new ArrayList<>();

        categoryMenuModifierArrayList_shared_pre_array = gson.fromJson(sharedPrefs.get_CategoryMenuItem_array(), new TypeToken<ArrayList<CategoryMenuModifier>>() {
        }.getType());

        /*categorymodifieridarraylistshared_pre = gson.fromJson(sharedPrefs.get_CategoryModifierId_array(), new TypeToken<ArrayList<String>>() {
        }.getType());*/

        Log.e("categorymodifieridarraylist",categorymodifieridarraylist.size()+"");
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
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_continue = (Button)findViewById(R.id.btn_continue);
         /*txt_combo_modifier_price = (TextView)findViewById(R.id.txt_combo_modifier_price);
        checkbox_modifier = (CheckBox)findViewById(R.id.checkbox_modifier);
        btn_increase = (Button)findViewById(R.id.btn_increase);
        btn_decrease = (Button)findViewById(R.id.btn_decrease);
        txt_category_menuitem = (TextView)findViewById(R.id.txt_category_menuitem);*/
        recyclerview_modifier = (RecyclerView)findViewById(R.id.recyclerview_modifier);
    }
    private void init()
    {

        txt_custom_menu_combo_name.setText(menu_name);
//        checkbox_modifier.setText(modifier_name);

        recyclerview_modifier.setLayoutManager(new LinearLayoutManager(getContext()));
        //Log.e("dialogadapter", menumodifieridlist.size()+"");
        modifierAdapter = new ModifierAdapter(context, menu_name,orderModifierArrayList,categorymodifieridarraylist, new OnClickListenerModifier() {
            @Override
            public void onItemClicked(int position,boolean status) {

                if (status == true)
                {
                    if (selectedorderModifierArrayList.size()>0)
                    {
                        selectedorderModifierArrayList.clear();
                        selectedorderModifierArrayList.add(new OrderModifier(orderModifierArrayList.get(position).getMenu_modifier_name(),orderModifierArrayList.get(position).getMenu_modifier_cost(),orderModifierArrayList.get(position).getMenu_modifier_id(),menu_name));
                    }
                    else
                    {
                        selectedorderModifierArrayList.add(new OrderModifier(orderModifierArrayList.get(position).getMenu_modifier_name(),orderModifierArrayList.get(position).getMenu_modifier_cost(),orderModifierArrayList.get(position).getMenu_modifier_id(),menu_name));
                    }
                }
                else
                {
                    if (removemodifieridlist.contains(orderModifierArrayList.get(position).getMenu_modifier_id()))
                    {

                    }
                    else
                    {
                        removemodifieridlist.add(orderModifierArrayList.get(position).getMenu_modifier_id());
                    }

                }
            }
        });
        recyclerview_modifier.setAdapter(modifierAdapter);

    }
    private void set_listener()
    {

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (minimum_quantity.equalsIgnoreCase("1"))
                {
                    if (selectedorderModifierArrayList.size()>0)
                    {
                        if (mDialogResult != null)
                        {
                            mDialogResult.finish(selectedorderModifierArrayList,removemodifieridlist);
                        }
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(context,"Please Select Atleast 1 Modifier",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (mDialogResult != null)
                    {
                        mDialogResult.finish(selectedorderModifierArrayList,removemodifieridlist);
                    }
                    dismiss();
                }

            }
        });
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(ArrayList<OrderModifier> selectedorderModifierArrayList,ArrayList<String> removemodifieridlist);
    }

}
