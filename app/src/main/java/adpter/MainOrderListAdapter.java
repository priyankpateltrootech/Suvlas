package adpter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
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
import com.suvlas.OfferDetailsActivity;
import com.suvlas.R;
import com.suvlas.RewardPontsActivity;

import java.util.Collections;
import java.util.List;

import bean.MainOrderlistItem;

/**
 * Created by hp on 5/19/2017.
 */

public class MainOrderListAdapter extends RecyclerView.Adapter<MainOrderListAdapter.ViewHolder> {

    Activity activity;

    List<MainOrderlistItem> item = Collections.emptyList();
    LayoutInflater inflater;

    public MainOrderListAdapter(Activity activity, List<MainOrderlistItem> item) {
        super();
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.item = item;
    }

    @Override
    public MainOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_orderlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final MainOrderlistItem object = item.get(position);

        //set data inside layout
        viewHolder.txt_greak.setText(object.offer_name);
        viewHolder.txt_greakextra.setText(object.offer_idescription);
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        int display_width1 = size.x;
        int display_height1 = size.y;

        Log.e("dasdas",display_width1+" "+display_height1);
        if (!object.getOffer_image().equalsIgnoreCase("")) {

            viewHolder.img_food1.getLayoutParams().width=display_width1;
            viewHolder.img_food1.getLayoutParams().height=display_width1;

            viewHolder.img_food1.setVisibility(View.VISIBLE);
            /*Glide.with(activity).load(object.offer_image).placeholder(R.drawable.dashboard_placeholder_img).error(R.drawable.dashboard_placeholder_img).into(viewHolder.img_food1);*/
            Glide.with(activity).load(object.offer_image).into(viewHolder.img_food1);


        } else {
            viewHolder.img_food1.setVisibility(View.GONE);
        }
        viewHolder.rel_offerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.e("click", object.getMenu_detail_item());
                //data goes to offerdetails activity
                activity.startActivity(new Intent(activity, OfferDetailsActivity.class)
                        .putExtra("screen","mainscreen")
                        .putExtra("offer_name",object.offer_name)
                        .putExtra("offer_description",object.offer_idescription)
                        .putExtra("offer_image",object.offer_image)
                        .putExtra("offer_code",object.offer_code)
                        .putExtra("offer_qrcode",object.generated_QR_Code)
                        .putExtra("offer_exp_date",object.expire_date)
                        .putExtra("productname",object.product_name)
                        .putExtra("itemname",object.getMenu_detail_item())
                        .putExtra("category_name",object.getCategory_name()));
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_greak, txt_greakextra;
        public ImageView img_food1;
        public RelativeLayout rel_offerlist;

        public ViewHolder(View itemView) {
            super(itemView);
            img_food1 = (ImageView) itemView.findViewById(R.id.img_food1);
            txt_greak = (TextView) itemView.findViewById(R.id.txt_greak);
            txt_greakextra = (TextView) itemView.findViewById(R.id.txt_greakextra);
            rel_offerlist=(RelativeLayout)itemView.findViewById(R.id.rel_offerlist);

        }
    }
}


