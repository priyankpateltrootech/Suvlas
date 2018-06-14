package bean;

import java.util.ArrayList;

public class CategoryPrice {

    String category_name,modifier_id,modifier_pric;

    public CategoryPrice(String category_name, String modifier_id, String modifier_pric) {
        this.category_name = category_name;
        this.modifier_id = modifier_id;
        this.modifier_pric = modifier_pric;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getModifier_id() {
        return modifier_id;
    }

    public void setModifier_id(String modifier_id) {
        this.modifier_id = modifier_id;
    }

    public String getModifier_pric() {
        return modifier_pric;
    }

    public void setModifier_pric(String modifier_pric) {
        this.modifier_pric = modifier_pric;
    }
}
