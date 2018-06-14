package bean;

/**
 * Created by hp on 12/1/2017.
 */

public class OfferOrder {

    String id,price;

    public OfferOrder(String id, String price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
