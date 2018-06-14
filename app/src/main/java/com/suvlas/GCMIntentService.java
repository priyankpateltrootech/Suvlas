package com.suvlas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import common.SharedPrefs;


public class GCMIntentService extends GcmListenerService {

    private static final String TAG = "GCMIntentService";
    static String push_type = "";
    public static int numMessages = 0;
    public static int NOTIFICATION_ID = 0;
    SharedPrefs sharedPrefs;
    String title;

    /*String from, Bundle data*/
    @Override
    public void onMessageReceived(String from, Bundle data) {
        /*String response = data.getString("default");*/
        Log.e("MESSAGE", "Msg Coming==" + data);

        //push_type = data.getString("push_type");
        String alert = data.getString("alert");

        try {
            JSONObject jsonObject = new JSONObject(alert);
            title = jsonObject.getString("title");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String notificationmessage = data.getString("message");
        String notificationtype = data.getString("type");
        String id = data.getString("offer_id");

        String notification_description =data.getString("notification_description");
        String offer_description =data.getString("offer_description");

        //generate notification
        generateNotification(getApplicationContext(),notificationmessage,title,notificationtype,id,notification_description,offer_description);
        Log.e("generateNotification", "-->> " + " YES = " + push_type);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message, String notificationtitle, String notificationtype,
                                      String notificationofferid,String notification_description ,String offer_description) {

        int icon = R.mipmap.ic_launcher;

        Log.e("message",message);
        Log.e("notificationtitle",notificationtitle);
        Log.e("notificationtype",notificationtype);
//        Log.e("notificationofferid",notificationofferid);
        String title = "";

        title = getString(R.string.app_name);

        long when = System.currentTimeMillis();
       // Intent notificationIntent = null;
        Bitmap user_image = null;

        /*try {
            Bitmap img_user = BitmapFactory.decodeStream((InputStream) new URL(user_pic).getContent());
            user_image = getRoundedShape(img_user);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //Send message
        if (notificationtype.equalsIgnoreCase("message"))
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //set intent
            Intent notificationIntent = new Intent(context,MessageActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            int num= (int) System.currentTimeMillis();
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, num, notificationIntent,0);
            //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationtitle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_description))
                    .setContentText(notification_description)
                    .setWhen(when)
                    //.setColor(context.getResources().getColor(R.color.colorAccent))
                    .setSmallIcon(icon)
                    .setContentIntent(resultPendingIntent).setNumber(0).setAutoCancel(true);
            Log.e("numMessages_AFTER", "-->> " + numMessages);
        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        n.setStyle(inboxStyle);*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //n.setColor();
                n.setSmallIcon(icon);
            } else {
                // Lollipop specific setColor method goes here.
                Log.e("COLOR", "-->>" + Build.VERSION.SDK_INT);
                n.setSmallIcon(icon);
                //n.setColor(context.getResources().getColor(R.color.colorAccent));
            }
            notificationManager.notify((int) when, n.build());
        }
        else if (notificationtype.equalsIgnoreCase("Order received."))
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //set intent
            Intent notificationIntent = new Intent(context,MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            int num= (int) System.currentTimeMillis();
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, num, notificationIntent,0);
            //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationtitle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_description))
                    .setContentText(notification_description)
                    .setWhen(when)
                    //.setColor(context.getResources().getColor(R.color.colorAccent))
                    .setSmallIcon(icon)
                    .setContentIntent(resultPendingIntent).setNumber(0).setAutoCancel(true);
            Log.e("numMessages_AFTER", "-->> " + numMessages);
        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        n.setStyle(inboxStyle);*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //n.setColor();
                n.setSmallIcon(icon);
            } else {
                // Lollipop specific setColor method goes here.
                Log.e("COLOR", "-->>" + Build.VERSION.SDK_INT);
                n.setSmallIcon(icon);
                //n.setColor(context.getResources().getColor(R.color.colorAccent));
            }
            notificationManager.notify((int) when, n.build());
        }
        else if (notificationtype.equalsIgnoreCase("Earned Rewarded Point"))
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //set intent
            Intent notificationIntent = new Intent(context,MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            int num= (int) System.currentTimeMillis();
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, num, notificationIntent,0);
            //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationtitle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_description))
                    .setContentText(notification_description)
                    .setWhen(when)
                    //.setColor(context.getResources().getColor(R.color.colorAccent))
                    .setSmallIcon(icon)
                    .setContentIntent(resultPendingIntent).setNumber(0).setAutoCancel(true);
            Log.e("numMessages_AFTER", "-->> " + numMessages);
        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        n.setStyle(inboxStyle);*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //n.setColor();
                n.setSmallIcon(icon);
            } else {
                // Lollipop specific setColor method goes here.
                Log.e("COLOR", "-->>" + Build.VERSION.SDK_INT);
                n.setSmallIcon(icon);
                //n.setColor(context.getResources().getColor(R.color.colorAccent));
            }
            notificationManager.notify((int) when, n.build());
        }
        else
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //set intent
            Intent notificationIntent = new Intent(context,OfferDetailsActivity.class);
            notificationIntent.putExtra("screen","notification");
            notificationIntent.putExtra("notificationtype",notificationtype);
            notificationIntent.putExtra("offer_id",notificationofferid);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            int num= (int) System.currentTimeMillis();
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, num, notificationIntent, 0);
            //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationtitle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(offer_description))
                    .setContentText(offer_description)
                    .setWhen(when)
                    //.setColor(context.getResources().getColor(R.color.colorAccent))
                    .setSmallIcon(icon)
                    .setContentIntent(resultPendingIntent).setNumber(0).setAutoCancel(true);
            Log.e("numMessages_AFTER", "-->> " + numMessages);
        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        n.setStyle(inboxStyle);*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //n.setColor();
                n.setSmallIcon(icon);
            } else {
                // Lollipop specific setColor method goes here.
                Log.e("COLOR", "-->>" + Build.VERSION.SDK_INT);
                n.setSmallIcon(icon);
                //n.setColor(context.getResources().getColor(R.color.colorAccent));
            }
            notificationManager.notify((int) when, n.build());
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 125;
        int targetHeight = 125;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
        return targetBitmap;
    }


}
