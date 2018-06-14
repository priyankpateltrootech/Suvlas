package adpter;

/**
 * Created by hp on 3/7/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.suvlas.R;

import java.util.ArrayList;

import bean.MainOrderlistItem;
import bean.gift_card_templet;

public class GiftcardImgSlideAdapter extends PagerAdapter {

    Context context;
    public Activity mcontext;

    int images[];
    LayoutInflater layoutInflater;
    ArrayList<gift_card_templet> gift_templet;


    public GiftcardImgSlideAdapter(Context context, int images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GiftcardImgSlideAdapter(Context context, ArrayList<gift_card_templet> gift_templet) {
        this.context = context;
        this.gift_templet = gift_templet;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
//        return images.length;
        return gift_templet.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.giftcardimgslideadapter, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_card);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //load image inside imageview
        Glide.with(context).load(gift_templet.get(position).tem_image).into(imageView);
        Log.e("templet",gift_templet.get(position).tem_image);
//        imageView.setImageResource(images[position]);

        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

