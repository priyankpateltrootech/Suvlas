package bean;

/**
 * Created by hp on 11/27/2017.
 */

public class CategoryMenu {

    private String category_id,category_name,category_item_id,category_item_name,category_item_quantity,category_item_price,order_count,category_item_image;

    public CategoryMenu(String category_id, String category_name, String category_item_id, String category_item_name, String category_item_quantity, String category_item_price,String order_count,String category_item_image) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_item_id = category_item_id;
        this.category_item_name = category_item_name;
        this.category_item_quantity = category_item_quantity;
        this.category_item_price = category_item_price;
        this.order_count = order_count;
        this.category_item_image = category_item_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getCategory_item_quantity() {
        return category_item_quantity;
    }

    public void setCategory_item_quantity(String category_item_quantity) {
        this.category_item_quantity = category_item_quantity;
    }

    public String getCategory_item_price() {
        return category_item_price;
    }

    public void setCategory_item_price(String category_item_price) {
        this.category_item_price = category_item_price;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getCategory_item_image() {
        return category_item_image;
    }

    public void setCategory_item_image(String category_item_image) {
        this.category_item_image = category_item_image;
    }
}
