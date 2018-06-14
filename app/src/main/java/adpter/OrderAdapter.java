package adpter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suvlas.OnClickListener;
import com.suvlas.R;

import java.util.ArrayList;
import java.util.List;

import bean.PriceId;
import bean.Restaurant_list;
import bean.Storelistorder;
import common.SharedPrefs;

/**
 * Created by hp on 6/8/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Activity activity;
    OnClickListener listener;

    ArrayList<Storelistorder> restaurant_name_list;

    public OrderAdapter(Activity activity, ArrayList<Storelistorder> restaurant_name_list,OnClickListener listener) {
        super();
        this.activity = activity;
        this.restaurant_name_list = restaurant_name_list;
        this.listener = listener;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_layout, null);
        OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return restaurant_name_list.size();
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txt_restaurant_name.setText(restaurant_name_list.get(i).getName());

        viewHolder.linear_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(i);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_restaurant_name;
        LinearLayout linear_recycler;

        public ViewHolder(final View view) {
            super(view);

            txt_restaurant_name = (TextView) view.findViewById(R.id.txt_restaurant_name);
            linear_recycler = (LinearLayout)view.findViewById(R.id.linear_recycler);

        }

    }


}
