package bean;

/**
 * Created by hp on 3/26/2018.
 */

public class ModifierPrice {


    String id;
    double price;

    public ModifierPrice(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
