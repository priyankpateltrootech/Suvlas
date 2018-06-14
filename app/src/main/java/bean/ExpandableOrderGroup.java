package bean;


import java.util.ArrayList;

/**
 * Created by hp on 11/27/2017.
 */

public class ExpandableOrderGroup {

    private String id,name,quantity,price;
    private ArrayList<ExpandableOrderChild> expandableOrderChild;

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

    public ArrayList<ExpandableOrderChild> getExpandableOrderChild() {
        return expandableOrderChild;
    }

    public void setExpandableOrderChild(ArrayList<ExpandableOrderChild> expandableOrderChild) {
        this.expandableOrderChild = expandableOrderChild;
    }
}
