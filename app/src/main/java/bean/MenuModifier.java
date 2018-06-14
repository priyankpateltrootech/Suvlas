package bean;

/**
 * Created by hp on 11/27/2017.
 */

public class MenuModifier {

    private String id,name,price,combo_name;

    public MenuModifier(String id, String name, String price, String combo_name) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.combo_name = combo_name;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCombo_name() {
        return combo_name;
    }

    public void setCombo_name(String combo_name) {
        this.combo_name = combo_name;
    }
}
