package bean;

/**
 * Created by hp on 6/7/2017.
 */

public class Store_img_item {

    public String store_rest_img;
    public String store_res_id;

    public Store_img_item(String store_rest_img, String store_res_id) {
        this.store_rest_img = store_rest_img;
        this.store_res_id = store_res_id;
    }

    public String getStore_rest_img() {
        return store_rest_img;
    }

    public void setStore_rest_img(String store_rest_img) {
        this.store_rest_img = store_rest_img;
    }

    public String getStore_res_id() {
        return store_res_id;
    }

    public void setStore_res_id(String store_res_id) {
        this.store_res_id = store_res_id;
    }
}
