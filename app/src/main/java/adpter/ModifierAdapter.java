package adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suvlas.AddtoCartActivity;
import com.suvlas.OnClickListener;
import com.suvlas.OnClickListenerModifier;
import com.suvlas.R;

import java.util.ArrayList;

import bean.MenuCombo;
import bean.OrderModifier;
import common.SharedPrefs;

/**
 * Created by hp on 10/13/2017.
 */

public class ModifierAdapter extends RecyclerView.Adapter<ModifierAdapter.ViewHolder> {

    public int mSelectedItem = -1;
    Context activity;
    ArrayList<OrderModifier> orderModifierArrayList;
    OnClickListenerModifier listener;
    ArrayList<String> categorymodifieridarraylistshared_pre = new ArrayList<>();
    Gson gson;
    SharedPrefs sharedPrefs;
    String screen ="";
    String category_name;
    ArrayList<String> categorymodifieridarraylist;

    public ModifierAdapter(Context activity, String category_name,ArrayList<OrderModifier> orderModifierArrayList, ArrayList<String> categorymodifieridarraylist,OnClickListenerModifier listener) {
        this.activity = activity;
        this.category_name = category_name;
        this.orderModifierArrayList = orderModifierArrayList;
        this.categorymodifieridarraylist = categorymodifieridarraylist;
        this.listener = listener;
        gson = new Gson();
        sharedPrefs = new SharedPrefs(activity);
        /*categorymodifieridarraylistshared_pre = gson.fromJson(sharedPrefs.get_CategoryModifierId_array(), new TypeToken<ArrayList<String>>() {
        }.getType());*/

        Log.e("categorymodifieridarraylistadapter",categorymodifieridarraylist.size()+"");

        screen = "start";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modifier_raw_layout, null);
        ModifierAdapter.ViewHolder viewHolder = new ModifierAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.checkbox_modifier.setText(orderModifierArrayList.get(position).getMenu_modifier_name());
        holder.txt_combo_modifier_price.setText(orderModifierArrayList.get(position).getMenu_modifier_cost());

        if (screen.equalsIgnoreCase("start"))
        {
            if (categorymodifieridarraylist != null)
            {
                if (categorymodifieridarraylist.contains(orderModifierArrayList.get(position).getMenu_modifier_id()))
                {
                    mSelectedItem = position;
                    holder.checkbox_modifier.setChecked(true);
                    screen = "";
                }
                else
                {

                    if (position == (orderModifierArrayList.size()-1))
                    {
                        mSelectedItem = -1;
                        holder.checkbox_modifier.setChecked(position == mSelectedItem);
                        screen = "";
                    }
                    else
                    {
                        mSelectedItem = -1;
                        holder.checkbox_modifier.setChecked(false);
                    }

                }
            }
            else
            {
                holder.checkbox_modifier.setChecked(position == mSelectedItem);
            }

        }
        else
        {
            holder.checkbox_modifier.setChecked(position == mSelectedItem);
        }


    }

    @Override
    public int getItemCount() {
        return orderModifierArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkbox_modifier;
        TextView txt_combo_modifier_price;

        public ViewHolder(final View view) {
            super(view);

            checkbox_modifier = (CheckBox)view.findViewById(R.id.checkbox_modifier);
            txt_combo_modifier_price = (TextView)view.findViewById(R.id.txt_combo_modifier_price);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkbox_modifier.isChecked())
                    {
                        mSelectedItem = getAdapterPosition();
                        listener.onItemClicked(mSelectedItem,true);
                    }
                    else
                    {
                        mSelectedItem = -1;
                        listener.onItemClicked(getAdapterPosition(),false);
                    }
                    notifyItemRangeChanged(0,orderModifierArrayList.size());


                }
            };

            itemView.setOnClickListener(clickListener);
            checkbox_modifier.setOnClickListener(clickListener);
        }

    }
}
