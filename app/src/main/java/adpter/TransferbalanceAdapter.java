package adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.suvlas.OfferDetailsActivity;
import com.suvlas.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bean.MainOrderlistItem;
import bean.TransferBalanceBean;

/**
 * Created by hp on 5/19/2017.
 */

public class TransferbalanceAdapter extends RecyclerView.Adapter<TransferbalanceAdapter.ViewHolder> {

    Activity activity;

    List<TransferBalanceBean> transferBalanceBeanList;
    LayoutInflater inflater;

    public TransferbalanceAdapter(Activity activity, List<TransferBalanceBean> transferBalanceBeanList) {
        super();
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.transferBalanceBeanList = transferBalanceBeanList;
    }

    @Override
    public TransferbalanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return transferBalanceBeanList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.txt_tranfer_transactionid.setText(transferBalanceBeanList.get(position).getTransaction_id());
        viewHolder.txt_transfer_amount.setText(transferBalanceBeanList.get(position).getTransaction_amount());

        String timezone = TimeZone.getDefault().getID();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));

        try {
            long time = sdf.parse(transferBalanceBeanList.get(position).getTransaction_time()).getTime();
            viewHolder.txt_transfer_time.setText(getlongtoago(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_tranfer_transactionid, txt_transfer_time,txt_transfer_amount;
        public ImageView img_transfer_balance;

        public ViewHolder(View itemView) {
            super(itemView);
            img_transfer_balance = (ImageView) itemView.findViewById(R.id.img_transfer_balance);
            txt_tranfer_transactionid = (TextView) itemView.findViewById(R.id.txt_tranfer_transactionid);
            txt_transfer_time = (TextView) itemView.findViewById(R.id.txt_transfer_time);
            txt_transfer_amount = (TextView) itemView.findViewById(R.id.txt_transfer_amount);

        }
    }


    public static String getlongtoago(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy/MM/dd HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        crdate1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime()-CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + "day ago ";
            } else {
                time = diffDays + "days ago ";
            }
        }
        else {
            time = "0 days ago";
        }/*else {

            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + "hr ago";
                } else {
                    time = diffHours + "hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + "min ago";
                    } else {
                        time = diffMinutes + "mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + "secs ago";
                    }
                }

            }

        }*/
        return time;
    }
}


