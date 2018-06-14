package bean;

import java.util.ArrayList;

/**
 * Created by hp on 3/21/2018.
 */

public class MenuOrderCategoryItem {

    String category_item_id,category_item_name,category_item_image,category_item_price;
    ArrayList<String> modifierlist;

    public MenuOrderCategoryItem(String category_item_id,String category_item_name, String category_item_price,String category_item_image,ArrayList<String> modifierlist) {
        this.category_item_id = category_item_id;
        this.category_item_name = category_item_name;
        this.category_item_price = category_item_price;
        this.category_item_image = category_item_image;
        this.modifierlist = modifierlist;
    }

    public String getCategory_item_id() {
        return category_item_id;
    }

    public void setCategory_item_id(String category_item_id) {
        this.category_item_id = category_item_id;
    }

    public String getCategory_item_name() {
        return category_item_name;
    }

    public void setCategory_item_name(String category_item_name) {
        this.category_item_name = category_item_name;
    }

    public String getCategory_item_price() {
        return category_item_price;
    }

    public void setCategory_item_price(String category_item_price) {
        this.category_item_price = category_item_price;
    }

    public String getCategory_item_image() {
        return category_item_image;
    }

    public void setCategory_item_image(String category_item_image) {
        this.category_item_image = category_item_image;
    }

    public ArrayList<String> getModifierlist() {
        return modifierlist;
    }

    public void setModifierlist(ArrayList<String> modifierlist) {
        this.modifierlist = modifierlist;
    }
}
