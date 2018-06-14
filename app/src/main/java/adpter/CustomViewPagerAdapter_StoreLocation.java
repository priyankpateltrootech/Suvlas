package adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.suvlas.R;

import java.util.ArrayList;

import bean.Store_img_item;

/**
 * Created by hp on 3/29/2017.
 */

public class CustomViewPagerAdapter_StoreLocation extends PagerAdapter {

    private Context context;
    private ArrayList<Store_img_item> viewPagerModel;
    private LayoutInflater layoutInflater;

    public CustomViewPagerAdapter_StoreLocation(Context context, ArrayList<Store_img_item> viewPagerModel) {
        this.context = context;
        this.viewPagerModel = viewPagerModel;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return viewPagerModel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = layoutInflater.inflate(R.layout.multi_store_img, container, false);
        Store_img_item mHotDealObject = viewPagerModel.get(position);
        ImageView store_logo = (ImageView) view.findViewById(R.id.multi_store_locations);

        Log.e("imagsadasdsadasdas",mHotDealObject.getStore_rest_img());

        //load image inside imageview
        Glide.with(context).load(mHotDealObject.getStore_rest_img()).into(store_logo);


        container.addView(view);
        return view;
    }

}