package bean;

public class OrderModifier {

    String menu_modifier_name,menu_modifier_cost,menu_modifier_id,category_item_name;

    public OrderModifier(String menu_modifier_name, String menu_modifier_cost, String menu_modifier_id,String category_item_name) {
        this.menu_modifier_name = menu_modifier_name;
        this.menu_modifier_cost = menu_modifier_cost;
        this.menu_modifier_id = menu_modifier_id;
        this.category_item_name = category_item_name;
    }

    public String getMenu_modifier_name() {
        return menu_modifier_name;
    }

    public void setMenu_modifier_name(String menu_modifier_name) {
        this.menu_modifier_name = menu_modifier_name;
    }

    public String getMenu_modifier_cost() {
        return menu_modifier_cost;
    }

    public void setMenu_modifier_cost(String menu_modifier_cost) {
        this.menu_modifier_cost = menu_modifier_cost;
    }

    public String getMenu_modifier_id() {
        return menu_modifier_id;
    }

    public void setMenu_modifier_id(String menu_modifier_id) {
        this.menu_modifier_id = menu_modifier_id;
    }

    public String getCategory_item_name() {
        return category_item_name;
    }

    public void setCategory_item_name(String category_item_name) {
        this.category_item_name = category_item_name;
    }
}
