package adpter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.suvlas.OnClickListener;
import com.suvlas.R;

import java.util.ArrayList;
import java.util.List;

import bean.MenuOrderBean;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 5/13/2017.
 */

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> item;
    OnClickListener listener;

    public MenuCategoryAdapter(Activity activity, ArrayList<String> item,OnClickListener listener) {
        super();
        this.activity = activity;
        this.item = item;
        this.listener = listener;
    }
    @Override
    public MenuCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_layout, null);
        MenuCategoryAdapter.ViewHolder viewHolder = new MenuCategoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(final MenuCategoryAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txt_restaurant_name.setText(item.get(i));

        viewHolder.linear_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(i);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_restaurant_name;
        LinearLayout linear_recycler;

        public ViewHolder(final View view) {
            super(view);

            txt_restaurant_name = (TextView) view.findViewById(R.id.txt_restaurant_name);
            linear_recycler = (LinearLayout)view.findViewById(R.id.linear_recycler);

        }

    }


}
