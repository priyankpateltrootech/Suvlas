package bean;

/**
 * Created by hp on 6/8/2017.
 */

public class PriceId {

    public String Price_id;
    public String Price;

    public PriceId(String price_id, String price) {
        Price_id = price_id;
        Price = price;
    }

    public String getPrice_id() {
        return Price_id;
    }

    public void setPrice_id(String price_id) {
        Price_id = price_id;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
