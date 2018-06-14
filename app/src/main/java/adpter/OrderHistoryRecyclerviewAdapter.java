package adpter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suvlas.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bean.ItemOrderHistory;

/**
 * Created by hp on 3/6/2017.
 */

public class OrderHistoryRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemOrderHistory> itemList;
    private Context context;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;
    HistoryAlertdialogAdapter historyAlertdialogAdapter;

    public OrderHistoryRecyclerviewAdapter(Context context, List<ItemOrderHistory> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistory_cardview_header, parent, false);
            return  new VHHeader(v);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistory_cardview,parent,false);
            return new OrderHistoryRecyclerviewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof VHHeader)
        {
            VHHeader VHheader = (VHHeader)holder;
           // VHheader.txtTitle.setText(header.getHeader());
            Log.e("VHheader position",""+position);
            VHheader.txt_foodname.setText(itemList.get(position).getFood_name());
            if (itemList.get(position).getFood_img().isEmpty())
            {
                //VHheader.foodimage.setImageResource(R.drawable.imagen_history);
            }
            else
            {
                Picasso.with(context).load(itemList.get(position).getFood_img()).into(VHheader.foodimage);
            }

            String timezone = TimeZone.getDefault().getID();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));

            try {
                long time = sdf.parse(itemList.get(position).getFood_time()).getTime();
                VHheader.txt_time.setText(getlongtoago(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            VHheader.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    View dialogView = inflater.inflate(R.layout.history_alert_layout, null);
                    dialogBuilder.setView(dialogView);

                    RecyclerView recyclerview_history_alert = (RecyclerView)dialogView.findViewById(R.id.recyclerview_history_alert);
                    recyclerview_history_alert.setLayoutManager(new LinearLayoutManager(context));

                    historyAlertdialogAdapter = new HistoryAlertdialogAdapter(context,itemList.get(position).getHistoryOrderModifierArrayList());
                    recyclerview_history_alert.setAdapter(historyAlertdialogAdapter);

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });

        }
        else if(holder instanceof OrderHistoryRecyclerviewHolder) {
            Log.e("item position",""+position);
            OrderHistoryRecyclerviewHolder VHitem = (OrderHistoryRecyclerviewHolder)holder;
            VHitem.foodname.setText(itemList.get(position).getFood_name());

            if (itemList.get(position).getFood_img().isEmpty())
            {
                //VHitem.foodimage.setImageResource(R.drawable.imagen_history);
            }
            else
            {
                Picasso.with(context).load(itemList.get(position).getFood_img()).into(VHitem.foodimage);
            }
            String timezone = TimeZone.getDefault().getID();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));

            try {
                long time = sdf.parse(itemList.get(position).getFood_time()).getTime();
                VHitem.foodtime.setText(getlongtoago(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            VHitem.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    View dialogView = inflater.inflate(R.layout.history_alert_layout, null);
                    dialogBuilder.setView(dialogView);

                    RecyclerView recyclerview_history_alert = (RecyclerView)dialogView.findViewById(R.id.recyclerview_history_alert);
                    recyclerview_history_alert.setLayoutManager(new LinearLayoutManager(context));

                    historyAlertdialogAdapter = new HistoryAlertdialogAdapter(context,itemList.get(position).getHistoryOrderModifierArrayList());
                    recyclerview_history_alert.setAdapter(historyAlertdialogAdapter);

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });

        }
    }



    @Override
    public  int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }
    public boolean isHeader(int position) {
        return position == 0;
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class OrderHistoryRecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView foodname;
        public TextView foodtime;
        public ImageView foodimage;
        RelativeLayout card_view;

        public OrderHistoryRecyclerviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            foodname = (TextView) itemView.findViewById(R.id.txt_foodname);
            foodtime = (TextView) itemView.findViewById(R.id.txt_time);
            foodimage = (ImageView) itemView.findViewById(R.id.img_food);
            card_view = (RelativeLayout)itemView.findViewById(R.id.card_view);

        }

        @Override
        public void onClick(View view) {
//        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }


    class VHHeader extends RecyclerView.ViewHolder{
        TextView txt_foodname,txt_time;
        public ImageView foodimage;
        RelativeLayout card_view;
        public VHHeader(View itemView) {
            super(itemView);

            txt_foodname = (TextView)itemView.findViewById(R.id.txt_foodname);
            txt_time = (TextView)itemView.findViewById(R.id.txt_time);
            card_view = (RelativeLayout)itemView.findViewById(R.id.card_view);
            foodimage = (ImageView) itemView.findViewById(R.id.img_food);
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

