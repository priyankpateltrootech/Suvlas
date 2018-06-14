package adpter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.suvlas.R;
import com.suvlas.StoreActivity;

import java.util.ArrayList;
import java.util.Locale;

import bean.StoreItem;
import bean.Store_img_item;

/**
 * Created by hp on 7/7/2017.
 */

public class SearchAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<StoreItem> store_item;
    private ArrayList<StoreItem> arraylist;

    public SearchAdapter(Context context, ArrayList<StoreItem> store_item) {
        this.context = context;
        this.store_item = store_item;
        this.arraylist = new ArrayList<StoreItem>();
        this.arraylist.addAll(store_item);
    }

    @Override
    public int getCount() {
        return store_item.size();
    }

    @Override
    public Object getItem(int i) {
        return store_item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        final TextView store_name = (TextView)itemView.findViewById(R.id.store_name);
        store_name.setText(store_item.get(i).getName());
        StoreItem storeItem = store_item.get(i);

        store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e("lat", String.valueOf(store_item.get(i).getLatitude()));
                Log.e("lat", String.valueOf(store_item.get(i).getLongitude()));
                Log.e("name",String.valueOf(store_item.get(i).getName()));

                StoreActivity.spinner_search.setText(store_item.get(i).getName());

                /*store_img_item.clear();
                store_img_item.addAll(store_item.get(i).getStore_img_items());*/

                Log.e("imageadapter", String.valueOf(store_item.get(i).getStore_img_items().size()));
               // StoreActivity.mAdapter = new CustomViewPagerAdapter_StoreLocation(context,store_item.get(i).getStore_img_items());
                //StoreActivity.viewPager.setAdapter(StoreActivity.mAdapter);
                StoreActivity.store_img_item.clear();

                //fill storeimageitem array
                StoreActivity.store_img_item.addAll(store_item.get(i).getStore_img_items());

                //viewpager bind with adpater
                StoreActivity.mAdapter = new CustomViewPagerAdapter_StoreLocation(context,store_item.get(i).getStore_img_items());
                StoreActivity.viewPager.setAdapter(StoreActivity.mAdapter);
                //StoreActivity.indicator.setViewPager(StoreActivity.viewPager);
/*
                StoreActivity.viewPager.invalidate();
                StoreActivity.mAdapter.notifyDataSetChanged();
*/

  //              InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(StoreActivity.txt_search.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                StoreActivity.mMap.getUiSettings().setMapToolbarEnabled(false);
                StoreActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store_item.get(i).getLatitude(),store_item.get(i).getLongitude()),15));
                StoreActivity.listview.setVisibility(View.GONE);
    //            StoreActivity.txt_search.setText("");

                //StoreActivity.store_img_item.addAll(store_item.get(i).getStore_img_items());
            }
        });

        return itemView;
    }

    //filter data
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        store_item.clear();
        if (charText.length() == 0) {
            store_item.addAll(arraylist);
        } else {
            for (StoreItem wp : arraylist) {
                if (wp.getLocation().toLowerCase(Locale.getDefault()).contains(charText) || wp.getName().toLowerCase(Locale.getDefault()).contains(charText)  || wp.getContact_number().contains(charText))  {
                    //store filter data in arraylist
                    wp.setName(wp.getName());
                    wp.setId(wp.getId());
                    wp.setLocation(wp.getLocation());
                    wp.setLatitude(wp.getLatitude());
                    wp.setLongitude(wp.getLongitude());
                    wp.setStore_img_items(wp.getStore_img_items());
                    store_item.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
