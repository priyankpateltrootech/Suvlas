package adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suvlas.AddtoCartActivity;
import com.suvlas.R;

import java.util.ArrayList;

import bean.MenuCombo;

/**
 * Created by hp on 10/13/2017.
 */

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.ViewHolder> {

    Activity activity;
    ArrayList<MenuCombo> menu_combo;
    Gson gson;
    String selected_restaurant_name;
    String selected_restaurant_id;

    public CustomMenuAdapter(Activity activity,ArrayList<MenuCombo> menu_combo,String selected_restaurant_name,String selected_restaurant_id) {
        this.activity = activity;
        this.menu_combo = menu_combo;
        this.selected_restaurant_name = selected_restaurant_name;
        this.selected_restaurant_id = selected_restaurant_id;
        gson = new Gson();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_combo_recycler, null);
        CustomMenuAdapter.ViewHolder viewHolder = new CustomMenuAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.menu_name.setText(menu_combo.get(position).getCombo_name());

        holder.card_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("position",position+"");
                Log.e("menu_combo_modifier_size",menu_combo.get(position).getMenu_combo_modifier().size()+"");

                String jsonmenucombomodifier = gson.toJson(menu_combo.get(position).getMenu_combo_modifier());

                Intent i = new Intent(activity, AddtoCartActivity.class);
                i.putExtra("menu_combo_name",menu_combo.get(position).getCombo_name());
                i.putExtra("menu_combo_id",menu_combo.get(position).getCombo_id());
                i.putExtra("jsonmenucombomodifier",jsonmenucombomodifier);
                i.putExtra("selected_restaurant_name",selected_restaurant_name);
                i.putExtra("selected_restaurant_id",selected_restaurant_id);
                activity.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return menu_combo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card_combo;
        TextView menu_name;

        public ViewHolder(final View view) {
            super(view);

            menu_name = (TextView)view.findViewById(R.id.menu_name);
            card_combo = (CardView)view.findViewById(R.id.card_combo);

        }

    }
}
