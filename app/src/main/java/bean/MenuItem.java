package bean;

/**
 * Created by hp on 5/1/2017.
 */

public class MenuItem {
    public static final int HEADER_TYPE = 0;
    public static final int ITEM_TYPE = 1;
    private String mName;
    private String mDescription;
    private int mType;
    private String menu_id;
    private String cost;
    private String tax;
    private String code;
    private String category_id;
    private String category_tax;
    private String category_order;
    private String category_code;


    public MenuItem(String name, String description, int type) {
        this.mName = name;
        this.mDescription = description;
        this.mType = type;
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
}
