package common;

import android.net.Uri;

import java.io.File;

public class Comman_url {

    public static final String Suvlas_url = "http://suvlas.trootechprojects.com/api/";

    //public static final String Suvlas_url = "http://192.168.0.25:8001/api/";

    public static final String api_key = "f5d2cd2e-2347-4c63-bc48-2de834b533f7";
    public static final String SENDER_ID = "634727873667";

    public static String crop_path_gallery = "";

    public static final String INSTAGRAM_CLIENT_ID = "dc1c2f96956e48ada775b1784e9e748a";
    public static final String INSTAGRAM_CLIENT_SECRET = "e5d44837e35f428bb8b37db7a6e1ad74";
    public static final String INSTAGRAM_CALLBACK_URL = "https://www.suvlas.com";
    public static final String INSTAGRAM_AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String INSTAGRAM_TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String INSTAGRAM_API_URL = "https://api.instagram.com/v1";


    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
}