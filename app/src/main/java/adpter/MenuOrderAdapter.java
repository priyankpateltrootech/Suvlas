package adpter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.bumptech.glide.Glide;
import com.google.firebase.database.Transaction;
import com.suvlas.GiftCardPaymentFragment;
import com.suvlas.MainActivity;
;
import com.suvlas.MenuOrder;
import com.suvlas.OrderFragment;
import com.suvlas.OrderItemsSelect;
import com.suvlas.R;
import com.suvlas.SearchActivity;
import com.suvlas.TransactionSucessActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.MenuOrderItem;

import static bean.MenuOrderItem.HEADER_TYPE;
import static bean.MenuOrderItem.ITEM_TYPE;

/**
 * Created by hp on 5/1/2017.
 */

public class MenuOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler{
    private ArrayList<MenuOrderItem> mList;
    //    private Map<String, MenuOrderItem> mList;
    int counter = 0;
    boolean showText = false;
    Activity mActivity;
    Context context;

    public MenuOrderAdapter(ArrayList<MenuOrderItem> mList, Activity mActivity) {
        this.mList = mList;
        this.mActivity = mActivity;
        Log.e("v1", "v1");

    }

    //    public MenuOrderAdapter( Map<String, MenuOrderItem> mList, Activity mActivity) {
//        this.mList = mList;
//        this.mActivity = mActivity;
//        Log.e("v1","v1");
//
//    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                Log.e("v2", "v1");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuorder_header, parent, false);
                return new ViewHolder(view);
            case ITEM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuorder_item, parent, false);
                return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MenuOrderItem object = mList.get(position);

        if (object != null) {
            switch (object.getType()) {
                case HEADER_TYPE:
                    Log.e("v3", "v1");
                    ((ViewHolder) holder).mTitle.setText(object.getName());
                    break;
                case ITEM_TYPE:
                    String img_url = object.getFood_img();
                    Log.e("img_name", object.getFood_img());
                    img_url.replaceAll(" ", "%20");
                    Log.e("img_url", img_url);
                    ((ItemViewHolder) holder).mTitle.setText(object.getName());
                    ((ItemViewHolder) holder).mDescription.setText(object.getDescription());
                    Glide.with(mActivity).load("http://admin.invucorp.com/invuPos/images/banner/" + img_url).into(((ItemViewHolder) holder).img_icon);
//                    if (mList.size()-1)
                    ((ItemViewHolder) holder).img_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            private void ChooseItem() {
                            final Dialog exit_dialog = new Dialog(mActivity);
                            exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            exit_dialog.setCancelable(true);
                            exit_dialog.setContentView(R.layout.menu_custom_itemdetails);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(exit_dialog.getWindow().getAttributes());
                            Point size = new Point();
                            mActivity.getWindowManager().getDefaultDisplay().getSize(size);
                            int display_width = size.x;
                            int display_height = size.y;
                            lp.width = (display_width * 90) / 100;
                            //lp.height = (display_height * 80) / 100;
                            lp.gravity = Gravity.CENTER;
                            //lp.verticalMargin = 10;
                            exit_dialog.getWindow().setAttributes(lp);

                            Button btn_decrease, btn_increase;
                            final TextView txt_desimal;
                            TextView txt_itemname, txt_custm_item1, txt_custm_item2, txt_custm_item3, txt_itemname_1, txt_custm_item3_3;

                            txt_itemname = (TextView) exit_dialog.findViewById(R.id.txt_itemname);
                            txt_custm_item1 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item1);
                            txt_custm_item2 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item2);
                            txt_custm_item3 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item3);
                            txt_itemname_1 = (TextView) exit_dialog.findViewById(R.id.txt_itemname_1);
                            txt_custm_item3_3 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item3_3);

                            txt_itemname.setText(object.getName());
                            txt_custm_item3.setText(object.getCost());

                            btn_increase = (Button) exit_dialog.findViewById(R.id.btn_increase);
                            btn_decrease = (Button) exit_dialog.findViewById(R.id.btn_decrease);
                            txt_desimal = (TextView) exit_dialog.findViewById(R.id.txt_desimal);
                            btn_increase.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    counter++;
                                    txt_desimal.setText(counter + "");
                                    Log.e("hello", String.valueOf(counter + ""));

                                }
                            });
                            btn_decrease.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (counter > 0) {
                                        counter--;
                                        txt_desimal.setText(counter + "");
                                    }
                                }
                            });
//
                            Button btn_continue = (Button) exit_dialog.findViewById(R.id.btn_continue);

                            btn_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    exit_dialog.dismiss();
//                Fragment fragment2 = new OrderItemsSelect();
//                FragmentManager fm = mActivity.getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(R.id.fram_menuorder, fragment2);
//                ft.commit();

                                }
                            });
                            exit_dialog.show();
                        }

//                        }
                    });

                    if (mList.size() == 1) {
                        ((ItemViewHolder) holder).view.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            MenuOrderItem object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    @Override
    public List<?> getAdapterData() {
        return mList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        View view;
        ImageView img_add, img_icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);
            view = (View) itemView.findViewById(R.id.view1);
            img_add = (ImageView) itemView.findViewById(R.id.img_add);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);

        }
    }
}
