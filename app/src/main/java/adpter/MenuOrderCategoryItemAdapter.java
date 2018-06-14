package adpter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;
import com.suvlas.AddtoCartActivity;
import com.suvlas.MainActivity;
import com.suvlas.OnClickListener;
import com.suvlas.OnClickListenerMenu;
import com.suvlas.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import bean.MenuCombo;
import bean.MenuOrderCategoryItem;
import common.Download;
import okhttp3.OkHttpClient;

/**
 * Created by hp on 10/13/2017.
 */

public class MenuOrderCategoryItemAdapter extends RecyclerView.Adapter<MenuOrderCategoryItemAdapter.ViewHolder> {

    Activity activity;
    ArrayList<MenuOrderCategoryItem> menuOrderCategoryItemArrayList;
    OnClickListenerMenu listener;
    ImageLoader imageLoader2;
    DisplayImageOptions options2;

    public MenuOrderCategoryItemAdapter(Activity activity, ArrayList<MenuOrderCategoryItem> menuOrderCategoryItemArrayList,OnClickListenerMenu listener) {
        this.activity = activity;
        this.menuOrderCategoryItemArrayList = menuOrderCategoryItemArrayList;
        this.listener = listener;

        final File cacheDir = StorageUtils.getOwnCacheDirectory(activity, "UniversalImageLoader/Cache/images");

        //ImageLoader.getInstance().destroy();

        imageLoader2 = ImageLoader.getInstance();

        ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(activity)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(activity)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(options2) // default
                .writeDebugLogs()
                .build();

        if (!imageLoader2.isInited())
            imageLoader2.init(config2);

        options2 = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(false)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_category_item_recycler, null);
        MenuOrderCategoryItemAdapter.ViewHolder viewHolder = new MenuOrderCategoryItemAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        //Log.e("name",menuOrderCategoryItemArrayList.get(position).getCategory_item_name());
        holder.menu_name.setText(menuOrderCategoryItemArrayList.get(position).getCategory_item_name());

        final String img_url = menuOrderCategoryItemArrayList.get(position).getCategory_item_image();

        String newstring = img_url.replace(" ", "%20");

        //Log.e("img_url",newstring);

        final String imageurl = "http://admin.invupos.com/invuPos/images/banner/" + newstring;

        //Log.e("imageurl",imageurl);

        //new DownloadImage(imageurl,holder.img_category_item).execute();

        if (img_url.isEmpty())
        {
            holder.img_category_item1.setVisibility(View.VISIBLE);
            holder.img_category_item.setVisibility(View.GONE);
        }
        else
        {
            holder.img_category_item.setVisibility(View.VISIBLE);
            holder.img_category_item1.setVisibility(View.GONE);
            Log.e("imageurl",imageurl);
            //Glide.with(activity).load(imageurl).into(holder.img_category_item);

            imageLoader2.displayImage(imageurl, holder.img_category_item, options2);

            /*GlideUrl glideUrl = new GlideUrl(imageurl,new LazyHeaders.Builder()
                    .addHeader("APIKEY","bd_suvlascentralpos")
                    .build());

            Glide.with(activity).load(glideUrl).listener(new RequestListener<GlideUrl, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Log.e("e",e+"");
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.img_category_item);*/

            /*Glide.with(activity).load(imageurl).asBitmap().override(500, 500)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            Log.e("e",e+"");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache,
                                                       boolean isFirstResource) {

                            return false;
                        }
                    }).into(holder.img_category_item);*/
            //new ImageDownloader(holder.img_category_item).execute(imageurl);
        }


        //Glide.with(activity).load(imageurl).into(holder.img_category_item);
       /* try {
            Glide.with(activity).load(new URL(imageurl)).apply(new RequestOptions()).into(holder.img_category_item);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/

        /*if (img_url != null)
        {
            holder.img_category_item1.setVisibility(View.GONE);
            holder.img_category_item.setVisibility(View.VISIBLE);

            img_url.replaceAll(" ", "abc");

            Log.e("img_url",img_url);
            Log.e("img_url","http://admin.invupos.com/invuPos/images/banner/" + img_url);

            String imageurl = "http://admin.invupos.com/invuPos/images/banner/" + img_url;

           // new ImageDownloader(holder.img_category_item).execute(imageurl);

            //new DownloadImage(imageurl,holder.img_category_item).execute();
            //getImage(imageurl,holder.img_category_item);
            *//*Picasso picasso;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


            picasso = new Picasso.Builder(activity)
                    .downloader(new OkHttpDownloader(client))
                    .build();*//*
            //Glide.with(activity).load(imageurl).apply(new RequestOptions().override(100,100)).into(holder.img_category_item);

            *//*Glide.with(activity).load(imageurl).asBitmap().override(500, 500)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            Log.e("e",e+"");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache,
                                                       boolean isFirstResource) {

                            return false;
                        }
                    }).into(holder.img_category_item);*//*
        }
        else
        {
            holder.img_category_item1.setVisibility(View.VISIBLE);
            holder.img_category_item.setVisibility(View.GONE);

        }*/



        /*if (img_url != null)
        {
            holder.img_category_item.setVisibility(View.VISIBLE);
            holder.img_category_item1.setVisibility(View.GONE);
            Picasso.with(activity).load("http://admin.invupos.com/invuPos/images/banner/" + img_url).into(holder.img_category_item);
        }
        else
        {
            holder.img_category_item.setVisibility(View.GONE);
            holder.img_category_item1.setVisibility(View.VISIBLE);
        }*/


        holder.card_menu_category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClicked(position,imageurl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuOrderCategoryItemArrayList.size();
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {

        ImageView iv;

        public ImageDownloader(ImageView iv) {
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return Download.getImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String imageurl;
        ImageView imageView;
        public DownloadImage(String imageurl, ImageView img_category_item) {

            this.imageurl = imageurl;
            this.imageView = img_category_item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
           /* mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();*/
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = imageurl;

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                input.reset();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            Log.e("result", String.valueOf(result));
            imageView.setImageBitmap(result);
            // Close progressdialog
            //mProgressDialog.dismiss();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card_menu_category_item;
        TextView menu_name;
        ImageView img_category_item,img_category_item1;

        public ViewHolder(final View view) {
            super(view);

            menu_name = (TextView)view.findViewById(R.id.menu_name);
            card_menu_category_item = (CardView)view.findViewById(R.id.card_menu_category_item);
            img_category_item = (ImageView)view.findViewById(R.id.img_category_item);
            img_category_item1 = (ImageView) view.findViewById(R.id.img_category_item1);
        }

    }
}
