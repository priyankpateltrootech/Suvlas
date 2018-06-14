package common;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chirag on 1/5/2017.
 */

public class CallingMethod {

    //GET network request
    public static String GET(OkHttpClient client, HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String GET_Menu(OkHttpClient client, String url, String ApiKey,Context mContext) throws IOException {
        Log.e("URL== GET",  url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("APIKEY", ApiKey)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String GET_Store(OkHttpClient client, String url, Context mContext) throws IOException {
        Log.e("URL== GET",  url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("APIKEY", "bd_suvlascentralpos")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String GET(OkHttpClient client, String url) throws IOException {
        Log.e("URL== GET",  url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    //POST network request
    public static String POST(OkHttpClient client, HttpUrl url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String POST_instagram(OkHttpClient client, String url, RequestBody body) throws IOException {
        Log.e("URL== POST",  url);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        Log.e("URL== POST", Comman_url.Suvlas_url + url);
        Request request = new Request.Builder()
                .url(Comman_url.Suvlas_url + url)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String payment_POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        Log.e("URL== POST", url);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String POST_API(OkHttpClient client, String url, RequestBody body, Context mContext) throws IOException {
        Log.e("URL== POST_API", Comman_url.Suvlas_url + url);

        SharedPrefs sharedPrefs = new SharedPrefs(mContext);
        Log.e("API_TOKEN", sharedPrefs.get_Api_token());

        Request request = new Request.Builder()
                .url(Comman_url.Suvlas_url + url)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("api_token", sharedPrefs.get_Api_token())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}