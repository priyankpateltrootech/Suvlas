package bean;

import java.util.ArrayList;

/**
 * Created by hp on 3/21/2018.
 */

public class OrderMenuModifier {

    String menu_item_img,menu_item_name,modifire_combo_name,minmum_quantity;
    ArrayList<OrderModifier> orderModifierArrayList;

    public OrderMenuModifier(String menu_item_img, String menu_item_name, String modifire_combo_name,String minmum_quantity,ArrayList<OrderModifier> orderModifierArrayList) {
        this.menu_item_img = menu_item_img;
        this.menu_item_name = menu_item_name;
        this.modifire_combo_name = modifire_combo_name;
        this.minmum_quantity = minmum_quantity;
        this.orderModifierArrayList = orderModifierArrayList;
    }

    public String getMenu_item_img() {
        return menu_item_img;
    }

    public void setMenu_item_img(String menu_item_img) {
        this.menu_item_img = menu_item_img;
    }

    public String getMenu_item_name() {
        return menu_item_name;
    }

    public void setMenu_item_name(String menu_item_name) {
        this.menu_item_name = menu_item_name;
    }

    public String getModifire_combo_name() {
        return modifire_combo_name;
    }

    public void setModifire_combo_name(String modifire_combo_name) {
        this.modifire_combo_name = modifire_combo_name;
    }

    public String getMinmum_quantity() {
        return minmum_quantity;
    }

    public void setMinmum_quantity(String minmum_quantity) {
        this.minmum_quantity = minmum_quantity;
    }

    public ArrayList<OrderModifier> getOrderModifierArrayList() {
        return orderModifierArrayList;
    }

    public void setOrderModifierArrayList(ArrayList<OrderModifier> orderModifierArrayList) {
        this.orderModifierArrayList = orderModifierArrayList;
    }
}
