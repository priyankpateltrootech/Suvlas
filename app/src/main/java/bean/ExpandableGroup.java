package bean;

import java.util.ArrayList;

/**
 * Created by hp on 11/27/2017.
 */

public class ExpandableGroup {

    private String Order_Item_Name;
    private ArrayList<ExpandableChild> Items;

    public String getOrder_Item_Name() {
        return Order_Item_Name;
    }

    public void setOrder_Item_Name(String order_Item_Name) {
        Order_Item_Name = order_Item_Name;
    }

    public ArrayList<ExpandableChild> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ExpandableChild> items) {
        Items = items;
    }
}
