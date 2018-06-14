package bean;

/**
 * Created by hp on 3/22/2018.
 */

public class MenuOrderCategory {

    String Category_id,Category_name;

    public MenuOrderCategory(String category_id, String category_name) {
        Category_id = category_id;
        Category_name = category_name;
    }

    public String getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(String category_id) {
        Category_id = category_id;
    }

    public String getCategory_name() {
        return Category_name;
    }

    public void setCategory_name(String category_name) {
        Category_name = category_name;
    }
}
