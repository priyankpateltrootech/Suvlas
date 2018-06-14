package bean;

/**
 * Created by hp on 3/22/2018.
 */

public class CategoryMenuModifier {

    String category_item_name,modifier_id,modifier_name,modifier_price,modifier_quantity,order_count,modifier_category;


    public CategoryMenuModifier(String category_item_name, String modifier_id, String modifier_name, String modifier_price, String modifier_quantity,String order_count,String modifier_category) {
        this.category_item_name = category_item_name;
        this.modifier_id = modifier_id;
        this.modifier_name = modifier_name;
        this.modifier_price = modifier_price;
        this.modifier_quantity = modifier_quantity;
        this.order_count = order_count;
        this.modifier_category = modifier_category;
    }

    public String getCategory_item_name() {
        return category_item_name;
    }

    public void setCategory_item_name(String category_item_name) {
        this.category_item_name = category_item_name;
    }

    public String getModifier_id() {
        return modifier_id;
    }

    public void setModifier_id(String modifier_id) {
        this.modifier_id = modifier_id;
    }

    public String getModifier_name() {
        return modifier_name;
    }

    public void setModifier_name(String modifier_name) {
        this.modifier_name = modifier_name;
    }

    public String getModifier_price() {
        return modifier_price;
    }

    public void setModifier_price(String modifier_price) {
        this.modifier_price = modifier_price;
    }

    public String getModifier_quantity() {
        return modifier_quantity;
    }

    public void setModifier_quantity(String modifier_quantity) {
        this.modifier_quantity = modifier_quantity;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getModifier_category() {
        return modifier_category;
    }

    public void setModifier_category(String modifier_category) {
        this.modifier_category = modifier_category;
    }
}
