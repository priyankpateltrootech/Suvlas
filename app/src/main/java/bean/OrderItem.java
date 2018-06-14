package bean;

/**
 * Created by hp on 5/13/2017.
 */

public class OrderItem {

    private String food_item_name;
    private int food_item_img;

    public OrderItem(String food_item_name, int food_item_img) {
        this.food_item_name = food_item_name;
        this.food_item_img = food_item_img;
    }

    public int getFood_item_img() {
        return food_item_img;
    }

    public void setFood_item_img(int food_item_img) {
        this.food_item_img = food_item_img;
    }

    public String getFood_item_name() {
        return food_item_name;
    }

    public void setFood_item_name(String food_item_name) {
        this.food_item_name = food_item_name;
    }
}
