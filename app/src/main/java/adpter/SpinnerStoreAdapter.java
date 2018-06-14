package adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.suvlas.R;

import java.util.ArrayList;

import bean.Restaurant_list;

public class SpinnerStoreAdapter extends ArrayAdapter<Restaurant_list> {

    Context context;
    ArrayList<Restaurant_list> restaurantListArrayList;

    public SpinnerStoreAdapter(@NonNull Context context, int resource,ArrayList<Restaurant_list> restaurantListArrayList) {
        super(context, resource,restaurantListArrayList);
        this.context = context;
        this.restaurantListArrayList = restaurantListArrayList;
        Log.e("adapter",restaurantListArrayList.size()+"");
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // return super.getView(position, convertView, parent);

        Restaurant_list item = restaurantListArrayList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.setting_favourite_location, parent, false);
        TextView store_name = (TextView) row.findViewById(R.id.store_name);

        Log.e("spinneritem",item.getName());
        store_name.setText(item.getName());

        return row;
    }
}
