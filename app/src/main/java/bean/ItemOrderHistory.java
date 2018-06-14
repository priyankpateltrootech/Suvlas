package bean;

import java.util.ArrayList;

/**
 * Created by hp on 3/6/2017.
 */

public class ItemOrderHistory {

    private String food_name;
    private String food_time;
    private String food_img;
    ArrayList<HistoryOrderModifier> historyOrderModifierArrayList;


    public ItemOrderHistory(String food_name, String food_time, String food_img,ArrayList<HistoryOrderModifier> historyOrderModifierArrayList) {
        this.food_name = food_name;
        this.food_time = food_time;
        this.food_img = food_img;
        this.historyOrderModifierArrayList = historyOrderModifierArrayList;
    }


    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_time() {
        return food_time;
    }

    public void setFood_time(String food_time) {
        this.food_time = food_time;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public ArrayList<HistoryOrderModifier> getHistoryOrderModifierArrayList() {
        return historyOrderModifierArrayList;
    }

    public void setHistoryOrderModifierArrayList(ArrayList<HistoryOrderModifier> historyOrderModifierArrayList) {
        this.historyOrderModifierArrayList = historyOrderModifierArrayList;
    }
}


