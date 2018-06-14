package bean;

/**
 * Created by hp on 11/27/2017.
 */

public class Menu {

    private String id,name,quantity,price,category_id;

    public Menu(String id, String name, String quantity,String price,String category_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
