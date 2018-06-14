package bean;

/**
 * Created by hp on 5/1/2017.
 */

public class MenuOrderItem {
    public static final int HEADER_TYPE = 0;
    public static final int ITEM_TYPE = 1;
    private String food_img;
    private String mName;
    private String mDescription;
    private int mType;

    private String menu_id;
    private String code;
    private String cost;
    private String tax;
    private String category_id;
    private String category_tax;
    private String category_code;
    private String category_order;

    public MenuOrderItem(String category_id,String menu_id,String food_img, String mName, String mDescription,String cost, int mType) {
        this.category_id = category_id;
        this.menu_id = menu_id;
        this.food_img = food_img;
        this.mName = mName;
        this.mDescription = mDescription;
        this.cost = cost;
        this.mType = mType;

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_tax() {
        return category_tax;
    }

    public void setCategory_tax(String category_tax) {
        this.category_tax = category_tax;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_order() {
        return category_order;
    }

    public void setCategory_order(String category_order) {
        this.category_order = category_order;
    }
}

