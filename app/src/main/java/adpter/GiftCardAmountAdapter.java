package adpter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.suvlas.GiftFragment;
import com.suvlas.OnClickListener;
import com.suvlas.OnClickListenerAmount;
import com.suvlas.R;
import com.suvlas.SearchActivity;

import java.text.DecimalFormat;
import java.util.List;

import bean.PriceId;
import common.SharedPrefs;

/**
 * Created by hp on 6/8/2017.
 */

public class GiftCardAmountAdapter extends RecyclerView.Adapter<GiftCardAmountAdapter.ViewHolder> {

    Activity activity;

    List<PriceId> item;
    String amount_id;
    String amount;
    Bundle bundle;
    Button amount_btn;
    SharedPrefs sharedPrefs;
    private OnClickListenerAmount listener;
    DecimalFormat df;

    public GiftCardAmountAdapter(Activity activity, List<PriceId> item,Button amount_btn,OnClickListenerAmount listener) {
        super();
        this.activity = activity;
        this.item = item;
        this.amount_btn=amount_btn;
        this.listener = listener;
        df = new DecimalFormat("00.00");


    }

    @Override
    public GiftCardAmountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_card_amount, null);
        GiftCardAmountAdapter.ViewHolder viewHolder = new GiftCardAmountAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(final GiftCardAmountAdapter.ViewHolder viewHolder, final int i) {

        Log.e("GiftCardAmountAdapter", "GiftCardAmountAdapter");
        viewHolder.btn1.setText("$"+item.get(i).getPrice());
        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //store dat ain shared preferences
                sharedPrefs=new SharedPrefs(activity);
                amount_id= item.get(i).getPrice_id();
                sharedPrefs.save_Amount_id(amount_id);
                amount=item.get(i).getPrice();
                Log.e("amount_id",amount_id);
                Double value = Double.valueOf(item.get(i).getPrice());
                amount_btn.setText("$"+df.format(value)+"");
                int gift_amount_id = Integer.parseInt(amount_id);
                listener.onItemClicked(gift_amount_id,item.get(i).getPrice());

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn1;


        public ViewHolder(final View view) {
            super(view);


            btn1 = (Button) view.findViewById(R.id.payrs1);


        }

    }


}
