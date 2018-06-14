package adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.suvlas.MainActivity;
import com.suvlas.MenuOrder;
import com.suvlas.R;
import com.suvlas.StoreActivity;

import java.util.ArrayList;
import java.util.List;

import bean.MenuOrderBean;
import bean.OrderItem;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 5/13/2017.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    Activity activity;
    List<MenuOrderBean> item;

    public OrderItemAdapter(Activity activity, List<MenuOrderBean> item) {
        super();
        this.activity = activity;
        this.item = item;
    }
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_row, null);
        OrderItemAdapter.ViewHolder viewHolder = new OrderItemAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(final OrderItemAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txt_msgdes.setText(item.get(i).getName());
        //viewHolder.img_item.setImageResource(item.get(i).getFood_item_img());

        final String img_url = item.get(i).getImage();
        Log.e("img_name_order", item.get(i).getImage());
        img_url.replaceAll(" ", "%20");
        Log.e("img_url_order", img_url);
        Log.e("image_url_order","http://admin.invupos.com/invuPos/images/banner/"+img_url);

        Glide.with(activity).load("http://admin.invupos.com/invuPos/images/banner/" + img_url).into(viewHolder.img_item);
        viewHolder.rel_yog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent menu = new Intent(activity, MenuOrder.class);
                activity.startActivity(menu);*/
//                Fragment fragment2 = new MenuOrder();
//                FragmentManager fragmentManager2 = ((FragmentActivity)activity).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//                fragmentTransaction2.replace(R.id.fram_map, fragment2);
//                fragmentTransaction2.addToBackStack(null);
//                fragmentTransaction2.commit();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView img_bank;
        public TextView txt_msgdes;
        public ImageView img_item;
        public RelativeLayout rel_yog;



        public ViewHolder(final View view) {
            super(view);

            img_item = (ImageView) view.findViewById(R.id.img_icon);
            txt_msgdes = (TextView) view.findViewById(R.id.txt_msgdes);
            rel_yog=(RelativeLayout)view.findViewById(R.id.rel_yog);


        }

    }


}
