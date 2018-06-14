package bean;

import java.util.ArrayList;

/**
 * Created by hp on 6/3/2017.
 */

public class StoreItem {

    public double latitude;
    public double longitude;
    public String Store_img;
    public int id;
    public String name;
    public String description;
    public String location;
    public String contact_number;
    public String opening_time;
    public String closing_time;

    public String store_rest_img;
    public String store_res_id;

    ArrayList<Store_img_item> store_img_items;
    public StoreItem() {
    }

    public StoreItem(double latitude, double longitude, String store_img, String name, String description,
                     String location, String contact_number, String opening_time, String closing_time,
                     ArrayList<Store_img_item> store_img_items) {
        this.latitude = latitude;
        this.longitude = longitude;
        Store_img = store_img;
        this.name = name;
        this.description = description;
        this.location = location;
        this.contact_number = contact_number;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.store_img_items = store_img_items;

    }

        public StoreItem(double latitude, double longitude, String store_img, String name, String description, String location, String contact_number, String opening_time, String closing_time) {
        this.latitude = latitude;
        this.longitude = longitude;
        Store_img = store_img;
        this.name = name;
        this.description = description;
        this.location = location;
        this.contact_number = contact_number;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.store_rest_img = store_rest_img;
        this.store_res_id = store_res_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStore_img() {
        return Store_img;
    }

    public void setStore_img(String store_img) {
        Store_img = store_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
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

    public ArrayList<Store_img_item> getStore_img_items() {
        return store_img_items;
    }

    public void setStore_img_items(ArrayList<Store_img_item> store_img_items) {
        this.store_img_items = store_img_items;
    }
}
