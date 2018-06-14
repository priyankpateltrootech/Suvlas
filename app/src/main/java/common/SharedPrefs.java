package common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 3/8/2017.
 */

public class SharedPrefs {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String SHARED = "Suvlas";

    public static final String User_id = "id";
    public static final String User_Name = "username";
    public static final String Email_id = "email";
    public static final String DOB = "date_of_birth";
    public static final String Gender = "gender";
    public static final String Api_token = "Apikey";
    public static final String Templat_id = "Templat_id";
    public static final String Amount_id = "Amount_id";
    public static final String Message_id = "Message_id";
    public static final String Custom_array = "Custom_array";
    public static final String Barcode = "Barcode";
    public static final String Custom_menu_array = "Custom_menu_array";
    public static final String Custom_combo_group_array = "custom_combo_group_array";
    public static final String Custom_combo_item_array = "custom_combo_item_array";
    public static final String Custom_menu_group_array = "custom_menu_group_array";
    public static final String Custom_menu_child_array = "custom_menu_child_array";
    public static final String CategoryMenu_array = "CategoryMenu_array";
    public static final String CategoryModifierId_array = "CategoryModifierId_array";
    public static final String CategoryModifierName_array = "CategoryModifierName_array";
    public static final String CategoryMenuItem_array = "CategoryMenuItem_array";

    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public  String get_User_Name() {
        return sharedPreferences.getString(User_Name, "");
    }
    public void save_User_name(String User_value) {
        editor.putString(User_Name, User_value);
        editor.commit();
    }

    public  String get_Email_id() {
        return sharedPreferences.getString(Email_id, "");
    }
    public void save_Email_id(String Email_value) {
        editor.putString(Email_id, Email_value);
        editor.commit();
    }

    public  String get_DOB() {
        return sharedPreferences.getString(DOB, "");
    }

    public void save_DOB(String dob) {
        editor.putString(DOB, dob);
        editor.commit();
    }
    public  String get_Gender() {
        return sharedPreferences.getString(Gender, "");
    }
    public void save_Gender(String gender) {
        editor.putString(Gender, gender);
        editor.commit();
    }
    public String get_Api_token() {
        return sharedPreferences.getString(Api_token, "");
    }

    public void save_Api_token(String apitoken) {
        editor.putString(Api_token, apitoken);
        editor.commit();
    }
    public String get_User_id() {
        return sharedPreferences.getString(User_id, "");
    }

    public void save_User_id(String Id_value) {
        editor.putString(User_id, Id_value);
        editor.commit();
    }

    public String get_Barcode_id() {
        return sharedPreferences.getString(Barcode, "");
    }

    public void save_Barcode_id(String Id_value) {
        editor.putString(Barcode, Id_value);
        editor.commit();
    }
    public String get_Templat_id() {
        return sharedPreferences.getString(Templat_id, "");
    }

    public void save_Templat_id(String templat_id) {
        editor.putString(Templat_id, templat_id);
        editor.commit();
    }

    public  String get_Amount_id() {
        return sharedPreferences.getString(Amount_id, "");
    }
    public void save_Amount_id(String amount_id) {
        editor.putString(Amount_id, amount_id);
        editor.commit();
    }

    public  String get_Message_id() {
        return sharedPreferences.getString(Message_id, "");
    }
    public void save_Message_id(String message_id) {
        editor.putString(Message_id, message_id);
        editor.commit();
    }

    public String get_CategoryModifierId_array() {
        return sharedPreferences.getString(CategoryModifierId_array, "");
    }

    public void save_CategoryModifierId_array(String categorymodifierid_array) {
        editor.putString(CategoryModifierId_array, categorymodifierid_array);
        editor.commit();
    }



    public String get_CategoryMenu_array() {
        return sharedPreferences.getString(CategoryMenu_array, "");
    }

    public void save_CategoryMenu_array(String categorymenu_array) {
        editor.putString(CategoryMenu_array, categorymenu_array);
        editor.commit();
    }

    public String get_CategoryMenuItem_array() {
        return sharedPreferences.getString(CategoryMenuItem_array, "");
    }

    public void save_CategoryMenuItem_array(String categorymenuitem_array) {
        editor.putString(CategoryMenuItem_array, categorymenuitem_array);
        editor.commit();
    }

    public String get_Custom_array() {
        return sharedPreferences.getString(Custom_array, "");
    }

    public void save_Custom_array(String custom_array) {
        editor.putString(Custom_array, custom_array);
        editor.commit();
    }

    public String get_Custom_menu_array() {
        return sharedPreferences.getString(Custom_menu_array, "");
    }

    public void save_Custom_menu_array(String custom_menu_array) {
        editor.putString(Custom_menu_array, custom_menu_array);
        editor.commit();
    }

    public String get_Custom_combo_group_array() {
        return sharedPreferences.getString(Custom_combo_group_array, "");
    }

    public void save_Custom_combo_group_array(String custom_combo_group_array) {
        editor.putString(Custom_combo_group_array, custom_combo_group_array);
        editor.commit();
    }

    public String get_Custom_combo_item_array() {
        return sharedPreferences.getString(Custom_combo_item_array, "");
    }

    public void save_Custom_combo_item_array(String custom_combo_item_array) {
        editor.putString(Custom_combo_item_array, custom_combo_item_array);
        editor.commit();
    }

    public String get_Custom_menu_group_array() {
        return sharedPreferences.getString(Custom_menu_group_array, "");
    }

    public void save_Custom_menu_group_array(String custom_menu_group_array) {
        editor.putString(Custom_menu_group_array, custom_menu_group_array);
        editor.commit();
    }

    public String get_Custom_menu_child_array() {
        return sharedPreferences.getString(Custom_menu_child_array, "");
    }

    public void save_Custom_menu_child_array(String custom_menu_child_array) {
        editor.putString(Custom_menu_child_array, custom_menu_child_array);
        editor.commit();
    }
}
