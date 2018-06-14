package bean;

import java.util.ArrayList;

/**
 * Created by hp on 5/19/2017.
 */

public class MainOrderlistItem {

    public static final int ORDER_HEADER_TYPE = 1;
    public static final int ORDER_ITEM_TYPE = 0;

    public String offer_name;
    public String offer_image;
    public String offer_idescription;
    public String offer_code;
    public String generated_QR_Code;
    public String expire_date;
    public String product_name;
    public int mType;
    public String menu_name;
    public String category_name;


    public MainOrderlistItem(String offer_name, String offer_image, String offer_idescription, String offer_code, String generated_QR_Code, String expire_date, String product_name,String menu_name,String category_name) {
        this.offer_name = offer_name;
        this.offer_image = offer_image;
        this.offer_idescription = offer_idescription;
        this.offer_code = offer_code;
        this.generated_QR_Code = generated_QR_Code;
        this.expire_date = expire_date;
        this.product_name = product_name;
        this.menu_name=menu_name;
        this.category_name = category_name;
    }




    public String getMenu_detail_item() {
        return menu_name;
    }

    public void setMenu_detail_item(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getOffer_idescription() {
        return offer_idescription;
    }

    public void setOffer_idescription(String offer_idescription) {
        this.offer_idescription = offer_idescription;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public String getGenerated_QR_Code() {
        return generated_QR_Code;
    }

    public void setGenerated_QR_Code(String generated_QR_Code) {
        this.generated_QR_Code = generated_QR_Code;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
