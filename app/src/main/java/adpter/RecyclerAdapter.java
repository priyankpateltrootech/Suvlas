package adpter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suvlas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.ComboMenu;
import bean.ComboModifier;
import bean.ExpandableOrderChild;
import bean.ExpandableOrderGroup;
import bean.Menu;
import bean.MenuModifier;
import bean.MenuOrderBean;
import bean.MenuOrderItem;
import common.SharedPrefs;

import static android.view.LayoutInflater.from;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder>
        implements StickyHeaderHandler {

    ArrayList<MenuOrderItem> menu_item;
    Activity activity;
    int counter = 0;
    JSONArray jsonArray;
    JSONObject object;
    ArrayList<MenuOrderBean> menu_order_bean = new ArrayList<>();
    ArrayList<String> menuidlist;
    ArrayList<String> menu_group_idlist;
    ArrayList<String> menu_child_idlist;
    int menuposition;
    String str_selected_menu_arraylist;
    String str_selected_menu_group_arraylist;
    String str_selected_menu_child_arraylist;
    Gson gson;
    SharedPrefs sharedPrefs;
    ArrayList<MenuOrderBean> menuOrderBeen_shared_pre_array = new ArrayList<>();
    String Jsonobjectmenu;
    ArrayList<Menu> menu = new ArrayList<>();
    ArrayList<MenuModifier> menuModifier = new ArrayList<>();
    ArrayList<Menu> menu_shared_pre_array = new ArrayList<>();
    ArrayList<MenuModifier> menumodifier_shared_pre_array = new ArrayList<>();

    public RecyclerAdapter(ArrayList<MenuOrderItem> menu_item, Activity activity) {
        this.menu_item = menu_item;
        this.activity = activity;
        menu_order_bean = new ArrayList<>();
        menuidlist = new ArrayList<>();
        menu_group_idlist = new ArrayList<>();
        menu_child_idlist = new ArrayList<>();
        gson = new Gson();
        sharedPrefs = new SharedPrefs(activity);

        //menuorder
        menuOrderBeen_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<MenuOrderBean>>() {}.getType());

        if (menuOrderBeen_shared_pre_array != null) {
            menu_order_bean.addAll(menuOrderBeen_shared_pre_array);
            for (int i = 0 ; i< menuOrderBeen_shared_pre_array.size();i++)
            {
                menuidlist.add(menuOrderBeen_shared_pre_array.get(i).getId());
            }
        }

        //menuordergroup
        menu_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<Menu>>() {}.getType());

        if (menu_shared_pre_array != null) {
            menu.addAll(menu_shared_pre_array);
            for (int i = 0 ; i< menu_shared_pre_array.size();i++)
            {
                menu_group_idlist.add(menu_shared_pre_array.get(i).getId());
            }
        }

        //menuorderchild
        menumodifier_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<MenuModifier>>() {}.getType());

        if (menumodifier_shared_pre_array != null) {
            menuModifier.addAll(menumodifier_shared_pre_array);
            for (int i = 0 ; i< menumodifier_shared_pre_array.size();i++)
            {
                menu_child_idlist.add(menumodifier_shared_pre_array.get(i).getId());
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = from(parent.getContext()).inflate(R.layout.menuorder_header, parent, false);
        final BaseViewHolder viewHolder;
        if (viewType == 0) {
            viewHolder = new MyViewHolder(view);
        } else {
            viewHolder = new MyOtherViewHolder(view);
        }
        return viewHolder;
    }

    @Override public void onBindViewHolder(BaseViewHolder holder, final int position) {

        final MenuOrderItem item = menu_item.get(position);

        if (item instanceof StickyHeader) {
            holder.menu_item.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
            holder.headertitleTextView.setText(item.getName());

        } else {
            //holder.menu_item.setVisibility(View.GONE);
            //holder.headertitleTextView.setVisibility(View.GONE);
            holder.relative_menu.setBackgroundResource(R.color.menu_item_color);
            holder.itemtitleTextView.setText(item.getName());
            final String img_url = item.getFood_img();
            img_url.replaceAll(" ", "%20");

            /*Glide.with(activity).load("http://admin.invupos.com/invuPos/images/banner/" + img_url).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.img_icon);*/
            Glide.with(activity).load("http://admin.invupos.com/invuPos/images/banner/" + img_url).into(holder.img_icon);



            holder.img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.e("category_id",item.getCategory_id());
                    final Dialog exit_dialog = new Dialog(activity);
                    exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    exit_dialog.setCancelable(false);
                    exit_dialog.setContentView(R.layout.menu_custom_itemdetails);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(exit_dialog.getWindow().getAttributes());
                    Point size = new Point();
                    activity.getWindowManager().getDefaultDisplay().getSize(size);
                    int display_width = size.x;
                    lp.width = (display_width * 90) / 100;
                    lp.gravity = Gravity.CENTER;
                    exit_dialog.getWindow().setAttributes(lp);

                    Button btn_decrease, btn_increase;
                    final TextView txt_desimal;
                    final TextView txt_itemname, txt_custm_item1, txt_custm_item2, txt_custm_item3, txt_itemname_1, txt_custm_item3_3;

                    txt_itemname = (TextView) exit_dialog.findViewById(R.id.txt_itemname);
                    txt_custm_item1 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item1);
                    txt_custm_item2 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item2);
                    txt_custm_item3 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item3);
                    txt_itemname_1 = (TextView) exit_dialog.findViewById(R.id.txt_itemname_1);
                    txt_custm_item3_3 = (TextView) exit_dialog.findViewById(R.id.txt_custm_item3_3);

                    txt_itemname.setText(item.getName());
                    txt_custm_item3.setText(item.getCost());

                    final float menu_cost= Float.parseFloat(item.getCost());

                    btn_increase = (Button) exit_dialog.findViewById(R.id.btn_increase);
                    btn_decrease = (Button) exit_dialog.findViewById(R.id.btn_decrease);
                    txt_desimal = (TextView) exit_dialog.findViewById(R.id.txt_desimal);

                    if (menuidlist.size()>0)
                    {
                        for (int i = 0 ; i < menuidlist.size();i++)
                        {
                            if (menu_order_bean.get(i).getId().equalsIgnoreCase(item.getMenu_id()))
                            {
                                counter = Integer.parseInt(menu_order_bean.get(i).getQuantity());
                                txt_desimal.setText(counter + "");
                                txt_custm_item3.setText(menu_order_bean.get(i).getPrice());
                                break;
                            }
                            else
                            {
                                counter = 1;
                                txt_desimal.setText(counter + "");
                                txt_custm_item3.setText(item.getCost());
                            }
                        }
                    }
                    btn_increase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            counter++;
                            txt_desimal.setText(counter + "");
                            txt_custm_item3.setText(String.valueOf(counter*menu_cost));

                        }
                    });
                    btn_decrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (counter > 0) {
                                counter--;
                                txt_desimal.setText(counter + "");
                                txt_custm_item3.setText(String.valueOf(counter*menu_cost));
                            }
                        }
                    });
//
                    Button btn_continue = (Button) exit_dialog.findViewById(R.id.btn_continue);

                    Button btn_cancel = (Button) exit_dialog.findViewById(R.id.btn_cancel);

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exit_dialog.dismiss();
                        }
                    });
                    btn_continue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //menuordergeneral

                            Log.e("menuquantity",txt_desimal.getText().toString());
                            if (menuidlist.size()>0)
                            {
                                boolean status = menuidlist.contains(item.getMenu_id());

                                for (int i = 0 ; i < menu_order_bean.size();i++)
                                {
                                    if (menu_order_bean.get(i).getId().equalsIgnoreCase(item.getMenu_id()))
                                    {
                                        menuposition = i;
                                        break;
                                    }
                                }
                                if (status == true)
                                {

                                    if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                    {
                                        menuidlist.remove(menuposition);
                                        menu_order_bean.remove(menuposition);

                                    }
                                    else
                                    {
                                        menuidlist.remove(menuposition);
                                        menu_order_bean.remove(menuposition);
                                        menuidlist.add(menuposition,item.getMenu_id());
                                        menu_order_bean.add(menuposition,new MenuOrderBean(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),item.getFood_img(),txt_custm_item3.getText().toString()));
                                    }

                                }
                                else
                                {
                                    for (int i = 0 ; i < menu_order_bean.size();i++)
                                    {
                                        if (menu_order_bean.get(i).getQuantity().equalsIgnoreCase("0"))
                                        {
                                            menuidlist.remove(i);
                                            menu_order_bean.remove(i);
                                            break;
                                        }
                                        else
                                        {
                                            menu_order_bean.add(new MenuOrderBean(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),item.getFood_img(),txt_custm_item3.getText().toString()));
                                            menuidlist.add(item.getMenu_id());
                                            break;
                                        }
                                    }

                                }
                            }
                            else
                            {
                                if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                {

                                }
                                else
                                {
                                    menu_order_bean.add(new MenuOrderBean(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),item.getFood_img(),txt_custm_item3.getText().toString()));
                                    menuidlist.add(item.getMenu_id());
                                }


                            }

                            //menuorderexpandgroup
                            if (menu_group_idlist.size()>0)
                            {
                                boolean status = menu_group_idlist.contains(item.getMenu_id());

                                for (int i = 0 ; i < menu.size();i++)
                                {
                                    if (menu.get(i).getId().equalsIgnoreCase(item.getMenu_id()))
                                    {
                                       menuposition = i;
                                        break;
                                    }
                                }
                                if (status == true)
                                {

                                        if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                        {
                                            menu_group_idlist.remove(menuposition);
                                            menu.remove(menuposition);
                                        }
                                        else
                                        {
                                            menu_group_idlist.remove(menuposition);
                                            menu.remove(menuposition);
                                            menu.add(menuposition,new Menu(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),txt_custm_item3.getText().toString(),item.getCategory_id()));
                                            menu_group_idlist.add(menuposition,item.getMenu_id());
                                        }



                                }
                                else
                                {
                                    for (int i = 0 ; i< menu.size();i++)
                                    {
                                        if (menu.get(i).getQuantity().equalsIgnoreCase("0"))
                                        {
                                            menu_group_idlist.remove(i);
                                            menu.remove(i);
                                            break;
                                        }
                                        else
                                        {
                                            menu.add(new Menu(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),txt_custm_item3.getText().toString(),item.getCategory_id()));
                                            menu_group_idlist.add(item.getMenu_id());
                                            break;
                                        }
                                    }

                                }
                            }
                            else
                            {
                                if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                {

                                }
                                else
                                {
                                    menu.add(new Menu(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),txt_custm_item3.getText().toString(),item.getCategory_id()));
                                    menu_group_idlist.add(item.getMenu_id());
                                }


                            }

                            //menuorderexpandchildren
                            if (menu_child_idlist.size()>0)
                            {
                                boolean status = menu_child_idlist.contains(item.getMenu_id());

                                for (int i = 0 ; i < menuModifier.size();i++)
                                {
                                    if (menuModifier.get(i).getId().equalsIgnoreCase(item.getMenu_id()))
                                    {
                                        menuposition = i;
                                        break;
                                    }
                                }
                                if (status == true)
                                {
                                        if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                        {
                                            menu_child_idlist.remove(menuposition);
                                            menuModifier.remove(menuposition);
                                        }
                                        else
                                        {
                                            menu_child_idlist.remove(menuposition);
                                            menuModifier.remove(menuposition);
                                            menu_child_idlist.add(menuposition,item.getMenu_id());
                                            menuModifier.add(menuposition,new MenuModifier(item.getMenu_id(),item.getName(),txt_custm_item3.getText().toString(),item.getName()));
                                        }


                                }
                                else
                                {
                                    for (int i = 0 ; i< menu.size();i++)
                                    {
                                        if (menu.get(i).getQuantity().equalsIgnoreCase("0"))
                                        {
                                            menu_child_idlist.remove(i);
                                            menuModifier.remove(i);
                                            break;
                                        }
                                        else
                                        {
                                            menuModifier.add(new MenuModifier(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),item.getName()));
                                            menu_child_idlist.add(item.getMenu_id());
                                            break;
                                        }
                                    }

                                }
                            }
                            else
                            {
                                if (txt_desimal.getText().toString().equalsIgnoreCase("0"))
                                {

                                }
                                else
                                {
                                    menuModifier.add(new MenuModifier(item.getMenu_id(),item.getName(),txt_desimal.getText().toString(),item.getName()));
                                    menu_child_idlist.add(item.getMenu_id());
                                }
                            }

                            Log.e("menuidlist",menuidlist.size()+"");
                            Log.e("menuidgrouplist",menu_group_idlist.size()+"");
                            Log.e("menuidchildlist",menu_child_idlist.size()+"");
                            Log.e("menu_order_bean",menu_order_bean.size()+"");
                            Log.e("menu",menu.size()+"");
                            Log.e("menumodifier",menuModifier.size()+"");

                            str_selected_menu_arraylist = gson.toJson(menu_order_bean);
                            sharedPrefs.save_Custom_array(str_selected_menu_arraylist);

                            str_selected_menu_group_arraylist = gson.toJson(menu);
                            sharedPrefs.save_Custom_menu_group_array(str_selected_menu_group_arraylist);

                            str_selected_menu_child_arraylist = gson.toJson(menuModifier);
                            sharedPrefs.save_Custom_menu_child_array(str_selected_menu_child_arraylist);

                            JSONObject jo = null;


                            try {

                                //Log.e("menu_order_bean_size_try",menu_order_bean.size()+"");
                                jo = new JSONObject();

                                for (int i = 0 ; i < menu_order_bean.size();i++)
                                {
                                    jsonArray = new JSONArray();
                                    object = new JSONObject();
                                    object.put("id",menu_order_bean.get(i).getId());
                                    object.put("name",menu_order_bean.get(i).getName());
                                    object.put("quantity",menu_order_bean.get(i).getQuantity());
                                    jsonArray.put(object);
                                    jo.put(menu_order_bean.get(i).getName(),jsonArray);
                                }

                                Log.e("jsonobject",jo+"");
                                Jsonobjectmenu = gson.toJson(jo);
                                //Gson gson = new Gson();
                                sharedPrefs.save_Custom_menu_array(jo+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Log.e("jsonarray",jsonArray+"");
                            exit_dialog.dismiss();

                        }
                    });
                    exit_dialog.show();

                }
            });
        }
    }

    @Override public int getItemCount() {
        return menu_item.size();
    }

    @Override public int getItemViewType(int position) {
        return position;
    }

    @Override public List<?> getAdapterData() {
        return menu_item;
    }

    private static final class MyViewHolder extends BaseViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static final class MyOtherViewHolder extends BaseViewHolder {

        MyOtherViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        TextView headertitleTextView,itemtitleTextView;
        RelativeLayout menu_header,menu_item;
        RelativeLayout relative_menu;
        ImageView img_add,img_icon;
        View view1;

        BaseViewHolder(View itemView) {
            super(itemView);
            headertitleTextView = (TextView) itemView.findViewById(R.id.headertitleTextView);
            itemtitleTextView = (TextView) itemView.findViewById(R.id.itemtitleTextView);
            menu_item = (RelativeLayout)itemView.findViewById(R.id.menu_item);
            relative_menu = (RelativeLayout)itemView.findViewById(R.id.relative_menu);
            img_add = (ImageView)itemView.findViewById(R.id.img_add);
            img_icon = (ImageView)itemView.findViewById(R.id.img_icon);
            view1 = (View)itemView.findViewById(R.id.view1);
        }
    }
}
