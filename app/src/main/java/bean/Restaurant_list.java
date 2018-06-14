package bean;

/**
 * Created by hp on 8/25/2017.
 */

public class Restaurant_list {

    String name;
    String id;

    public Restaurant_list(String Name,String Id) {
        this.name = Name;
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
