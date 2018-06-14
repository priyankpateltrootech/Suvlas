package common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Download {

    InputStream inputStream = null;

    private static InputStream OpenHttpConnection(String urlString) throws IOException
    {
        InputStream is= null;
        //int response = -1;*/
       // URL url = new URL(urlString);
        //URLConnection conn = url.openConnection();

        /*if(! (conn instanceof HttpURLConnection) )
            throw new IOException("Not an HTTP connection");*/

        try{
            URL url = new URL(urlString);//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection

            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("asdsa", "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage());

            }


            is = c.getInputStream();
            /*response = httpConn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }*/
        }
        catch(Exception ex)
        {
            throw new IOException("Error connecting");
        }

        return is;
    }
    public static Bitmap getImage(String URL)
    {
        Bitmap bitmap = null;
        InputStream in = null;

        try{
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch(IOException e1) {
            Log.d("mye", "Download - getImage: " + e1.getLocalizedMessage());
        }

        return bitmap;
    }
}
