package bean;

import java.util.ArrayList;

/**
 * Created by hp on 11/23/2017.
 */

public class MenuCombo {

    String combo_id,combo_name;
    ArrayList<MenuComboModifier> menu_combo_modifier;

    public MenuCombo(String combo_id, String combo_name, ArrayList<MenuComboModifier> menu_combo_modifier) {
        this.combo_id = combo_id;
        this.combo_name = combo_name;
        this.menu_combo_modifier = menu_combo_modifier;
    }

    public String getCombo_id() {
        return combo_id;
    }

    public void setCombo_id(String combo_id) {
        this.combo_id = combo_id;
    }

    public String getCombo_name() {
        return combo_name;
    }

    public void setCombo_name(String combo_name) {
        this.combo_name = combo_name;
    }

    public ArrayList<MenuComboModifier> getMenu_combo_modifier() {
        return menu_combo_modifier;
    }

    public void setMenu_combo_modifier(ArrayList<MenuComboModifier> menu_combo_modifier) {
        this.menu_combo_modifier = menu_combo_modifier;
    }
}
