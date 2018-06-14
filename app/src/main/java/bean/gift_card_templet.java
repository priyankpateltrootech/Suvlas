package bean;

import java.util.ArrayList;

/**
 * Created by hp on 5/19/2017.
 */

public class gift_card_templet {

    public String tem_name;
    public String tem_image_id;
    public String tem_image;

    ArrayList<PriceId> price_id;



    public gift_card_templet(String tem_name, String tem_image_id, String tem_image) {
        this.tem_name = tem_name;
        this.tem_image_id = tem_image_id;
        this.tem_image = tem_image;
    }

    public gift_card_templet() {
    }

    public String getTem_name() {
        return tem_name;
    }

    public void setTem_name(String tem_name) {
        this.tem_name = tem_name;
    }

    public String getTem_image() {
        return tem_image;
    }

    public void setTem_image(String tem_image) {
        this.tem_image = tem_image;
    }

    public String getTem_image_id() {
        return tem_image_id;
    }

    public void setTem_image_id(String tem_image_id) {
        this.tem_image_id = tem_image_id;
    }

    public ArrayList<PriceId> getPrice_id() {
        return price_id;
    }

    public void setPrice_id(ArrayList<PriceId> price_id) {
        this.price_id = price_id;
    }
}
