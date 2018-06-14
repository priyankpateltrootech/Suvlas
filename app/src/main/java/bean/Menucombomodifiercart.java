package bean;

/**
 * Created by hp on 11/23/2017.
 */

public class Menucombomodifiercart {

    String combo_name,combo_modifier_id,combo_modifier_name,combo_modifier_cost;

    public Menucombomodifiercart(String combo_name, String combo_modifier_id, String combo_modifier_name, String combo_modifier_cost) {
        this.combo_name = combo_name;
        this.combo_modifier_id = combo_modifier_id;
        this.combo_modifier_name = combo_modifier_name;
        this.combo_modifier_cost = combo_modifier_cost;
    }

    public String getCombo_name() {
        return combo_name;
    }

    public void setCombo_name(String combo_name) {
        this.combo_name = combo_name;
    }

    public String getCombo_modifier_id() {
        return combo_modifier_id;
    }

    public void setCombo_modifier_id(String combo_modifier_id) {
        this.combo_modifier_id = combo_modifier_id;
    }

    public String getCombo_modifier_name() {
        return combo_modifier_name;
    }

    public void setCombo_modifier_name(String combo_modifier_name) {
        this.combo_modifier_name = combo_modifier_name;
    }

    public String getCombo_modifier_cost() {
        return combo_modifier_cost;
    }

    public void setCombo_modifier_cost(String combo_modifier_cost) {
        this.combo_modifier_cost = combo_modifier_cost;
    }
}
