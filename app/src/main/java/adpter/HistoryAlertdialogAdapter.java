package adpter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suvlas.OnClickListener;
import com.suvlas.R;

import java.util.ArrayList;

import bean.HistoryOrderModifier;
import bean.Storelistorder;

/**
 * Created by hp on 6/8/2017.
 */

public class HistoryAlertdialogAdapter extends RecyclerView.Adapter<HistoryAlertdialogAdapter.ViewHolder> {

    Context activity;
    ArrayList<HistoryOrderModifier> historyOrderModifierArrayList;

    public HistoryAlertdialogAdapter(Context activity, ArrayList<HistoryOrderModifier> historyOrderModifierArrayList) {
        super();
        this.activity = activity;
        this.historyOrderModifierArrayList = historyOrderModifierArrayList;
    }

    @Override
    public HistoryAlertdialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_alert_raw_layout, null);
        HistoryAlertdialogAdapter.ViewHolder viewHolder = new HistoryAlertdialogAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return historyOrderModifierArrayList.size();
    }

    @Override
    public void onBindViewHolder(final HistoryAlertdialogAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txt_name.setText(historyOrderModifierArrayList.get(i).getName());
        viewHolder.txt_price.setText(historyOrderModifierArrayList.get(i).getPrice());
        viewHolder.txt_quantity.setText(historyOrderModifierArrayList.get(i).getQuantity());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_price,txt_quantity;

        public ViewHolder(final View view) {
            super(view);

            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
            txt_quantity = (TextView) view.findViewById(R.id.txt_quantity);


        }

    }


}
