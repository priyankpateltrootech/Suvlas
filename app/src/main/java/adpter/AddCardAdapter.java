package adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.oned.ITFReader;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;
import com.suvlas.AddtoCartActivity;
import com.suvlas.ComboMenuDialog;
import com.suvlas.MenuModifierDialog;
import com.suvlas.OrderResumeActivity;
import com.suvlas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import bean.CategoryMenu;
import bean.CategoryMenuModifier;
import bean.CategoryPrice;
import bean.ComboMenu;
import bean.ComboModifier;
import bean.Menucombomodifiercart;
import bean.ModifierPrice;
import bean.OrderMenuModifier;
import bean.OrderModifier;
import common.SharedPrefs;

/**
 * Created by hp on 6/8/2017.
 */

public class AddCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    Context context;
    ArrayList<OrderMenuModifier> orderMenuModifierArrayList;
    ArrayList<String> menumodifieridlist;
    ArrayList<String> categorymenuidlist;
    String category_id, category_name, category_item_id, category_item_name, category_item_prices, category_item_price_sharepre, restaurant_id, restaurant_name, restaurant_apikey;
    ArrayList<CategoryMenu> categoryMenuArrayList = new ArrayList<>();
    ArrayList<CategoryMenu> categoryMenuArrayList_shared_pre_array = new ArrayList<>();
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList = new ArrayList<>();
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList1 = new ArrayList<>();
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList2 = new ArrayList<>();
    ArrayList<CategoryMenuModifier> categoryMenuModifierArrayList_shared_pre_array = new ArrayList<>();
    Gson gson;
    String str_selected_categorymenu_arraylist, str_selected_categorymenuitem_arraylist;
    SharedPrefs sharedPrefs;
    int counter = 0;
    String category_item_quantity;
    TextView textView;
    String screeninit = "start";
    ArrayList<ModifierPrice> modifierPriceArrayList;
    ArrayList<OrderModifier> selectedorderModifierArrayList;
    ArrayList<CategoryPrice> categoryPriceArrayList;
    ArrayList<String> categoryitemarraylist;
    ArrayList<String> categorymodifieridarraylist;
    double modifier_final_price = 0, modifier_single_price = 0;
    String minimum_quantity;
    ArrayList<String> categorymodifieridarraylistshared_pre = new ArrayList<>();
    ArrayList<String> modifiercategoryarraylist;
    String category_item_image;
    ImageLoader imageLoader2;
    DisplayImageOptions options2;

    public AddCardAdapter(Context context, String restaurant_id, String restaurant_name, String restaurant_apikey, String category_id, String category_name, String category_item_id, String category_item_name, String category_item_price, String imageurl,ArrayList<OrderMenuModifier> orderMenuModifierArrayList) {
        this.context = context;
        this.restaurant_id = restaurant_id;
        this.restaurant_name = restaurant_name;
        this.restaurant_apikey = restaurant_apikey;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_item_id = category_item_id;
        this.category_item_name = category_item_name;
        this.category_item_prices = category_item_price;
        this.category_item_image = imageurl;
        this.orderMenuModifierArrayList = orderMenuModifierArrayList;
        menumodifieridlist = new ArrayList<>();
        categorymenuidlist = new ArrayList<>();
        modifierPriceArrayList = new ArrayList<>();
        gson = new Gson();
        sharedPrefs = new SharedPrefs(context);
        setHasStableIds(true);
        //Log.e("asdas", sharedPrefs.get_CategoryMenu_array());

        categorymodifieridarraylist = new ArrayList<>();

        categoryitemarraylist = new ArrayList<>();

        categoryPriceArrayList = new ArrayList<>();

        selectedorderModifierArrayList = new ArrayList<>();

        modifiercategoryarraylist = new ArrayList<>();
        //sharedPrefs.save_CategoryModifierId_array("");

        categoryMenuArrayList_shared_pre_array = gson.fromJson(sharedPrefs.get_CategoryMenu_array(), new TypeToken<ArrayList<CategoryMenu>>() {
        }.getType());

        if (categoryMenuArrayList_shared_pre_array != null) {
            categoryMenuArrayList.addAll(categoryMenuArrayList_shared_pre_array);

            for (int i = 0; i < categoryMenuArrayList_shared_pre_array.size(); i++) {
                categorymenuidlist.add(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_id());
                if (categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name().equalsIgnoreCase(category_item_name)) {
                    category_item_price_sharepre = categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price();
                    //counter = Integer.parseInt(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_quantity());
                }

            }
        }

        categoryMenuModifierArrayList_shared_pre_array = gson.fromJson(sharedPrefs.get_CategoryMenuItem_array(), new TypeToken<ArrayList<CategoryMenuModifier>>() {
        }.getType());

        if (categoryMenuModifierArrayList_shared_pre_array != null) {
            for (int i = 0; i < categoryMenuModifierArrayList_shared_pre_array.size(); i++) {
                menumodifieridlist.add(categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_id());
                categoryMenuModifierArrayList.add(new CategoryMenuModifier(categoryMenuModifierArrayList_shared_pre_array.get(i).getCategory_item_name(), categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_id(), categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_name(), categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_price(), categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_quantity(), categoryMenuModifierArrayList_shared_pre_array.get(i).getOrder_count(),categoryMenuModifierArrayList_shared_pre_array.get(i).getModifier_category()));
            }

        }

        final File cacheDir = StorageUtils.getOwnCacheDirectory(context, "UniversalImageLoader/Cache/images");

        //ImageLoader.getInstance().destroy();

        imageLoader2 = ImageLoader.getInstance();

        ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtocart_recycler_header_layout, null);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtocart_recycler_footer_layout, null);
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtocart_recycler_item_layout, null);
            return new ItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            imageLoader2.displayImage(category_item_image, headerViewHolder.img_order_menu, options2);

            //Picasso.with(context).load(category_item_image).into(headerViewHolder.img_order_menu);
            headerViewHolder.txt_menu_combo_header.setText(orderMenuModifierArrayList.get(position).getMenu_item_name());
            //headerViewHolder.txt_item_quantity.setText("1");
            if (categoryMenuArrayList_shared_pre_array != null) {
                for (int i = 0; i < categoryMenuArrayList_shared_pre_array.size(); i++) {
                    if (categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_name().equalsIgnoreCase(category_item_name)) {

                        if (screeninit.equalsIgnoreCase("start")) {
                            //headerViewHolder.txt_category_price.setText(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price());
                            headerViewHolder.txt_category_price.setText(category_item_prices);
                            screeninit = "end";
                        } else {
                            if (AddtoCartActivity.recyclerscroll.equalsIgnoreCase("scrolldown")) {

                            } else {
                                headerViewHolder.txt_category_price.setText(category_item_prices);
                                headerViewHolder.txt_category_price.setText(categoryMenuArrayList_shared_pre_array.get(i).getCategory_item_price());
                            }
                        }


                        // headerViewHolder.txt_item_quantity.setText(category_item_quantity);
                    } else {
                        if (screeninit.equalsIgnoreCase("start")) {
                            headerViewHolder.txt_category_price.setText(category_item_prices);
                            screeninit = "end";
                        } else {
                            if (AddtoCartActivity.recyclerscroll.equalsIgnoreCase("scrolldown")) {

                            } else {
                                headerViewHolder.txt_category_price.setText(category_item_prices);
                            }
                        }
                    }
                }

            } else {
                if (screeninit.equalsIgnoreCase("start")) {
                    headerViewHolder.txt_category_price.setText(category_item_prices);
                    screeninit = "end";
                } else {
                    if (AddtoCartActivity.recyclerscroll.equalsIgnoreCase("scrolldown")) {

                    } else {
                        headerViewHolder.txt_category_price.setText(category_item_prices);
                    }
                }

            }
            textView = headerViewHolder.txt_category_price;
            category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();

            headerViewHolder.btn_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counter++;
                    headerViewHolder.txt_item_quantity.setText(counter + "");
                    double category_price = Double.parseDouble(category_item_prices);
                    double category_with_modifier_price = category_price + modifier_final_price;
                    double category_total = counter * category_with_modifier_price;
                    Log.e("category_price", category_price + "");
                    Log.e("category_with_modifier_price", category_with_modifier_price + "");
                    Log.e("category_total", category_total + "");
                    category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();
                    headerViewHolder.txt_category_price.setText(String.valueOf(category_total));
                    /*if (counter>2)
                    {
                        *//*if (categoryPriceArrayList.size()<2)
                        {*//*
                            headerViewHolder.txt_item_quantity.setText(counter + "");
                            double category_price = Double.parseDouble(category_item_prices);
                            double category_with_modifier_price = category_price + modifier_single_price;
                            double category_total = counter * category_with_modifier_price;
                            Log.e("category_price1",category_price+"");
                            Log.e("category_with_modifier_price1",category_with_modifier_price+"");
                            Log.e("category_total1",category_total+"");
                            category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();
                            headerViewHolder.txt_category_price.setText(String.valueOf(category_total));
                        *//*}else
                        {
                            headerViewHolder.txt_item_quantity.setText(counter + "");
                            double category_price = Double.parseDouble(category_item_prices);
                            double category_with_modifier_price = category_price + modifier_final_price;
                            double category_total = counter * category_with_modifier_price;
                            Log.e("category_price2",category_price+"");
                            Log.e("category_with_modifier_price2",category_with_modifier_price+"");
                            Log.e("category_total2",category_total+"");
                            category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();
                            headerViewHolder.txt_category_price.setText(String.valueOf(category_total));
                        }*//*
                    }
                    else
                    {
                        headerViewHolder.txt_item_quantity.setText(counter + "");
                        double category_price = Double.parseDouble(category_item_prices);
                        double category_with_modifier_price = category_price + modifier_final_price;
                        double category_total = counter * category_with_modifier_price;
                        Log.e("category_price",category_price+"");
                        Log.e("category_with_modifier_price",category_with_modifier_price+"");
                        Log.e("category_total",category_total+"");
                        category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();
                        headerViewHolder.txt_category_price.setText(String.valueOf(category_total));
                    }*/

                }
            });

            headerViewHolder.btn_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counter > 1) {
                        counter--;
                        headerViewHolder.txt_item_quantity.setText(counter + "");
                        double category_price = Double.parseDouble(category_item_prices);
                        double category_with_modifier_price = category_price + modifier_final_price;
                        double category_total = counter * category_with_modifier_price;
                        headerViewHolder.txt_category_price.setText(String.valueOf(category_total));
                        category_item_quantity = headerViewHolder.txt_item_quantity.getText().toString();
                    }
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.btn_add_to_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("minimum_quantity", minimum_quantity);
                    if (minimum_quantity.equalsIgnoreCase("1")) {
                        if (categoryitemarraylist.size() > 0) {
                            if (categoryMenuArrayList.size() > 0) {
                                int count = categoryMenuArrayList.size();

                                int order_count = count++;
                                categorymenuidlist.add(category_item_id);
                                /*categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, String.valueOf(final_price),String.valueOf(order_count)));*/
                                categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, textView.getText().toString(), String.valueOf(order_count),category_item_image));
                            } else {
                                categorymenuidlist.add(category_item_id);
                                categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, textView.getText().toString(), "0",category_item_image));
                            }


                            str_selected_categorymenu_arraylist = gson.toJson(categoryMenuArrayList);
                            sharedPrefs.save_CategoryMenu_array(str_selected_categorymenu_arraylist);
                            // Log.e("categorymenuadapterb", categoryMenuArrayList.size() + "");

                            Log.e("categoryMenuModifierArrayList2sssss",categoryMenuModifierArrayList.size()+"");
                            categoryMenuModifierArrayList2.addAll(categoryMenuModifierArrayList);
                            Log.e("categoryMenuModifierArrayList21",categoryMenuModifierArrayList2.size()+"");
                            Log.e("categoryMenuModifierArrayList2kkkk",categoryMenuModifierArrayList1.size()+"");
                            categoryMenuModifierArrayList2.addAll(categoryMenuModifierArrayList1);
                            Log.e("categoryMenuModifierArrayList22",categoryMenuModifierArrayList2.size()+"");


                            str_selected_categorymenuitem_arraylist = gson.toJson(categoryMenuModifierArrayList2);
                            //Log.e("categoryMenuModifierArrayListsize", categoryMenuModifierArrayList.size() + "");
                            sharedPrefs.save_CategoryMenuItem_array(str_selected_categorymenuitem_arraylist);
                            Intent i = new Intent(context, OrderResumeActivity.class);
                            i.putExtra("restaurant_id", restaurant_id);
                            i.putExtra("restaurant_name", restaurant_name);
                            i.putExtra("restaurant_apikey", restaurant_apikey);
                            i.putExtra("imageurl",category_item_image);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } else {
                            Toast.makeText(context, "Please Select Atlest 1 Modifier", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        if (categoryMenuArrayList.size() > 0) {
                            int count = categoryMenuArrayList.size();

                            int order_count = count++;
                            categorymenuidlist.add(category_item_id);
                            /*categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, String.valueOf(final_price),String.valueOf(order_count)));*/
                            categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, textView.getText().toString(), String.valueOf(order_count),category_item_image));
                        } else {
                            categorymenuidlist.add(category_item_id);
                            categoryMenuArrayList.add(new CategoryMenu(category_id, category_name, category_item_id, category_item_name, category_item_quantity, textView.getText().toString(), "0",category_item_image));
                        }


                        str_selected_categorymenu_arraylist = gson.toJson(categoryMenuArrayList);
                        sharedPrefs.save_CategoryMenu_array(str_selected_categorymenu_arraylist);
                        // Log.e("categorymenuadapterb", categoryMenuArrayList.size() + "");

                        Log.e("categoryMenuModifierArrayList2sssss",categoryMenuModifierArrayList.size()+"");
                        categoryMenuModifierArrayList2.addAll(categoryMenuModifierArrayList);
                        Log.e("categoryMenuModifierArrayList21",categoryMenuModifierArrayList2.size()+"");
                        Log.e("categoryMenuModifierArrayList2kkkk",categoryMenuModifierArrayList1.size()+"");
                        categoryMenuModifierArrayList2.addAll(categoryMenuModifierArrayList1);
                        Log.e("categoryMenuModifierArrayList22",categoryMenuModifierArrayList2.size()+"");
                        str_selected_categorymenuitem_arraylist = gson.toJson(categoryMenuModifierArrayList2);
                        //Log.e("categoryMenuModifierArrayListsize", categoryMenuModifierArrayList.size() + "");
                        sharedPrefs.save_CategoryMenuItem_array(str_selected_categorymenuitem_arraylist);
                        Intent i = new Intent(context, OrderResumeActivity.class);
                        i.putExtra("restaurant_id", restaurant_id);
                        i.putExtra("restaurant_name", restaurant_name);
                        i.putExtra("restaurant_apikey", restaurant_apikey);
                        i.putExtra("imageurl",category_item_image);
                        context.startActivity(i);
                    }
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            final OrderMenuModifier currentItem = getItem(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.txt_menu_combo_modifier_name.setText(currentItem.getModifire_combo_name());


            if (currentItem.getMinmum_quantity().equalsIgnoreCase("1")) {
                itemViewHolder.img_circle.setVisibility(View.VISIBLE);
                minimum_quantity = currentItem.getMinmum_quantity();
            }
            itemViewHolder.linear_recycler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.e("peice", menumodifieridlist.size()+"");
                    MenuModifierDialog menuModifierDialog = new MenuModifierDialog(context, minimum_quantity, orderMenuModifierArrayList.get(position).getModifire_combo_name(), orderMenuModifierArrayList.get(position).getOrderModifierArrayList(),categorymodifieridarraylist);
                    menuModifierDialog.setCancelable(false);
                    menuModifierDialog.show();

                    menuModifierDialog.setDialogResult(new MenuModifierDialog.OnMyDialogResult() {
                        @Override
                        public void finish(ArrayList<OrderModifier> selectedorderModifierArrayList, ArrayList<String> removemodifieridlist) {


                            double sum = 0;
                            double price = 0;
                            double modifier_category_price = 0;

                            if (removemodifieridlist.size() > 0) {

                                for (int i = 0; i < categoryPriceArrayList.size(); i++) {
                                    if (removemodifieridlist.contains(categoryPriceArrayList.get(i).getModifier_id())) {
                                        categoryPriceArrayList.remove(i);
                                    }
                                }

                                Log.e("categorymodifieridarraylistbefore", categorymodifieridarraylist.size() + "");
                                categorymodifieridarraylist.removeAll(removemodifieridlist);
                                Log.e("categorymodifieridarraylistafter", categorymodifieridarraylist.size() + "");

                                for (int j = 0; j < categoryMenuModifierArrayList.size(); j++) {
                                    if (removemodifieridlist.contains(categoryMenuModifierArrayList.get(j).getModifier_id())) {
                                        categoryMenuModifierArrayList.remove(j);
                                    }
                                }

                                if (categoryPriceArrayList.size() == 0) {
                                    categoryitemarraylist.clear();
                                }
                            }

                            for (int i = 0; i < selectedorderModifierArrayList.size(); i++) {

                                Log.e("modifier_name", selectedorderModifierArrayList.get(i).getMenu_modifier_name());

                                if (categoryitemarraylist.size() > 0) {
                                    if (categoryitemarraylist.contains(selectedorderModifierArrayList.get(i).getCategory_item_name())) {

                                        for (int j = 0; j < categoryPriceArrayList.size(); j++) {
                                            if (categoryPriceArrayList.get(j).getCategory_name().equalsIgnoreCase(selectedorderModifierArrayList.get(i).getCategory_item_name())) {
                                                categoryPriceArrayList.get(j).setCategory_name(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                                categoryPriceArrayList.get(j).setModifier_id(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                                categoryPriceArrayList.get(j).setModifier_pric(selectedorderModifierArrayList.get(i).getMenu_modifier_cost());
                                                Log.e("modifier_name", selectedorderModifierArrayList.get(i).getMenu_modifier_name());
                                                categorymodifieridarraylist.remove(j);
                                                categorymodifieridarraylist.add(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                            }
                                        }

                                    } else {
                                        categorymodifieridarraylist.add(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                        categoryitemarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                        categoryPriceArrayList.add(new CategoryPrice(selectedorderModifierArrayList.get(i).getCategory_item_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost()));
                                    }
                                } else {
                                    categorymodifieridarraylist.add(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                    categoryitemarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                    categoryPriceArrayList.add(new CategoryPrice(selectedorderModifierArrayList.get(i).getCategory_item_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost()));
                                }


                                for (int k = 0; k < categoryPriceArrayList.size(); k++) {
                                    modifier_single_price = Double.parseDouble(categoryPriceArrayList.get(k).getModifier_pric());
                                    sum += Double.parseDouble(categoryPriceArrayList.get(k).getModifier_pric());
                                }

                                modifier_final_price = sum;

                                modifier_category_price = Double.parseDouble(category_item_quantity) * sum;

                                if (categoryMenuArrayList.size() > 0) {
                                    int count = categoryMenuArrayList.size();
                                    int order_count = count++;

                                    if (categoryMenuModifierArrayList1.size()>0)
                                    {
                                        if (modifiercategoryarraylist.contains(selectedorderModifierArrayList.get(i).getCategory_item_name()))
                                        {
                                            for (int l =0 ; l < categoryMenuModifierArrayList1.size();l++)
                                            {
                                                Log.e("getCategory_item_name",categoryMenuModifierArrayList1.get(l).getModifier_name());
                                                Log.e("getCategory_item_name",categoryMenuModifierArrayList1.get(l).getModifier_category());
                                                Log.e("selectedgetCategory_item_name",selectedorderModifierArrayList.get(i).getCategory_item_name());
                                                if (categoryMenuModifierArrayList1.get(l).getModifier_category().equalsIgnoreCase(selectedorderModifierArrayList.get(i).getCategory_item_name())) {
                                                    categoryMenuModifierArrayList1.get(l).setCategory_item_name(category_item_name);
                                                    categoryMenuModifierArrayList1.get(l).setModifier_id(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                                    categoryMenuModifierArrayList1.get(l).setModifier_name(selectedorderModifierArrayList.get(i).getMenu_modifier_name());
                                                    categoryMenuModifierArrayList1.get(l).setModifier_price(selectedorderModifierArrayList.get(i).getMenu_modifier_cost());
                                                    categoryMenuModifierArrayList1.get(l).setModifier_quantity(category_item_quantity);
                                                    categoryMenuModifierArrayList1.get(l).setOrder_count(String.valueOf(order_count));
                                                    categoryMenuModifierArrayList1.get(l).setModifier_category(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                                }
                                            }
                                        }
                                        else
                                        {
                                            modifiercategoryarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                            categoryMenuModifierArrayList1.add(new CategoryMenuModifier(category_item_name, selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost(), category_item_quantity, String.valueOf(order_count),selectedorderModifierArrayList.get(i).getCategory_item_name()));
                                            Log.e("modifiercategoryarraylist",modifiercategoryarraylist.size()+"");
                                        }

                                        Log.e("categoryMenuModifierArrayList1",categoryMenuModifierArrayList1.size()+"");
                                    }
                                    else
                                    {
                                        modifiercategoryarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                        categoryMenuModifierArrayList1.add(new CategoryMenuModifier(category_item_name, selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost(), category_item_quantity, "0",selectedorderModifierArrayList.get(i).getCategory_item_name()));
                                    }

                                } else {

                                    if (categoryMenuModifierArrayList1.size()>0)
                                    {
                                        if (modifiercategoryarraylist.contains(selectedorderModifierArrayList.get(i).getCategory_item_name()))
                                        {
                                            for (int k =0 ; k < categoryMenuModifierArrayList1.size();k++)
                                            {
                                                Log.e("getCategory_item_name",categoryMenuModifierArrayList1.get(k).getCategory_item_name());
                                                Log.e("selectedgetCategory_item_name",selectedorderModifierArrayList.get(i).getCategory_item_name());
                                                if (categoryMenuModifierArrayList1.get(k).getModifier_category().equalsIgnoreCase(selectedorderModifierArrayList.get(i).getCategory_item_name())) {
                                                    categoryMenuModifierArrayList1.get(k).setCategory_item_name(category_item_name);
                                                    categoryMenuModifierArrayList1.get(k).setModifier_id(selectedorderModifierArrayList.get(i).getMenu_modifier_id());
                                                    categoryMenuModifierArrayList1.get(k).setModifier_name(selectedorderModifierArrayList.get(i).getMenu_modifier_name());
                                                    categoryMenuModifierArrayList1.get(k).setModifier_price(selectedorderModifierArrayList.get(i).getMenu_modifier_cost());
                                                    categoryMenuModifierArrayList1.get(k).setModifier_quantity(category_item_quantity);
                                                    categoryMenuModifierArrayList1.get(k).setOrder_count("0");
                                                    categoryMenuModifierArrayList1.get(k).setModifier_category(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                                }
                                            }
                                        }
                                        else
                                        {
                                            modifiercategoryarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                            categoryMenuModifierArrayList1.add(new CategoryMenuModifier(category_item_name, selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost(), category_item_quantity, "0",selectedorderModifierArrayList.get(i).getCategory_item_name()));
                                        }
                                    }
                                    else
                                    {
                                        modifiercategoryarraylist.add(selectedorderModifierArrayList.get(i).getCategory_item_name());
                                        categoryMenuModifierArrayList1.add(new CategoryMenuModifier(category_item_name, selectedorderModifierArrayList.get(i).getMenu_modifier_id(), selectedorderModifierArrayList.get(i).getMenu_modifier_name(), selectedorderModifierArrayList.get(i).getMenu_modifier_cost(), category_item_quantity, "0",selectedorderModifierArrayList.get(i).getCategory_item_name()));
                                    }


                                }

                            }

                            price = Double.parseDouble(category_item_prices) + modifier_category_price;

                            textView.setText(String.valueOf(price));

                        }
                    });
                }
            });


        }
    }


    private OrderMenuModifier getItem(int position) {
        return orderMenuModifierArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderMenuModifierArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == orderMenuModifierArrayList.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txt_menu_combo_header, txt_item_quantity, txt_category_price;
        Button btn_decrease, btn_increase;
        ImageView img_order_menu;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.txt_menu_combo_header = (TextView) itemView.findViewById(R.id.txt_menu_combo_header);
            this.txt_item_quantity = (TextView) itemView.findViewById(R.id.txt_item_quantity);
            this.txt_category_price = (TextView) itemView.findViewById(R.id.txt_category_price);

            img_order_menu = (ImageView)itemView.findViewById(R.id.img_order_menu);

            btn_decrease = (Button) itemView.findViewById(R.id.btn_decrease);
            btn_increase = (Button) itemView.findViewById(R.id.btn_increase);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        Button btn_add_to_order;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.btn_add_to_order = (Button) itemView.findViewById(R.id.btn_add_to_order);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txt_menu_combo_modifier_name;
        LinearLayout linear_recycler;
        ImageView img_circle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.txt_menu_combo_modifier_name = (TextView) itemView.findViewById(R.id.txt_menu_combo_modifier_name);
            this.linear_recycler = (LinearLayout) itemView.findViewById(R.id.linear_recycler);
            this.img_circle = (ImageView) itemView.findViewById(R.id.img_circle);
        }
    }


}
