package bean;

/**
 * Created by hp on 3/20/2018.
 */

public class Storelistorder {

    String apikey,tokenunvu,name,principal,id_frenchies,frenchiese_name,localtime;

    public Storelistorder(String apikey, String tokenunvu, String name, String principal, String id_frenchies, String frenchiese_name, String localtime) {
        this.apikey = apikey;
        this.tokenunvu = tokenunvu;
        this.name = name;
        this.principal = principal;
        this.id_frenchies = id_frenchies;
        this.frenchiese_name = frenchiese_name;
        this.localtime = localtime;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getTokenunvu() {
        return tokenunvu;
    }

    public void setTokenunvu(String tokenunvu) {
        this.tokenunvu = tokenunvu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getId_frenchies() {
        return id_frenchies;
    }

    public void setId_frenchies(String id_frenchies) {
        this.id_frenchies = id_frenchies;
    }

    public String getFrenchiese_name() {
        return frenchiese_name;
    }

    public void setFrenchiese_name(String frenchiese_name) {
        this.frenchiese_name = frenchiese_name;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }
}
