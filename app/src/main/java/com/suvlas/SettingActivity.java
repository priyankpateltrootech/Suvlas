package com.suvlas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import adpter.SpinnerStoreAdapter;
import bean.Restaurant_list;
import common.CallingMethod;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_message;
import common.Comman_url;
import common.MCrypt;
import common.Request_loader;
import common.SharedPrefs;
import de.hdodenhof.circleimageview.CircleImageView;
import io.doorbell.android.Doorbell;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by hp on 5/12/2017.
 */

public class SettingActivity extends AppCompatActivity {

    ImageView img_back, img_edit_profile, img_edit_name,img_edit_birthdate,img_favouritecollection,img_favouritecollectiontxt;
    TextView text_logout, txt_shareapp, txt_rateapp, txt_location_add, txt_date,txt_feedback;
    EditText edittxt_name, txt_name;
    Button btn_yes, btn_no, btn_save, btn_yes1, btn_no1, btn_on, btn_off;
    Request_loader loader;
    Comman_Dialog comman_dialog;
    MCrypt mCrypt;
    SharedPrefs sharedPrefs;
    CircleImageView img_profile_pic;
    private Uri file;
    String picturePath = "", createdate = "";
    public static String TAG_Notification_Status = "";
    String profile_pic;
    Geocoder geocoder;
    List<Address> addresses;
    LinearLayout liner_first;
    Dialog exit_dialog;
    TextView txt_birthdate;
    int cal_year, cal_month, cal_day;
    ArrayList<Restaurant_list> restaurant_name_list;
    Spinner spinner_favouritecollection;
    String  spinnerselectedstorename;
    RelativeLayout relative_spinner,relative_text;
    TextView text_favouritecollection;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File("mnt/sdcard/suvlas");
        try {
            if (mediaStorageDir.mkdir()) {
                Log.e("Directory created", "yes");
            } else {
                Log.e("Directory is not", "no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();
    }

    private void findviewID() {

        img_edit_birthdate = (ImageView)findViewById(R.id.img_edit_birthdate);
        img_favouritecollection = (ImageView)findViewById(R.id.img_favouritecollection);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
//        txt_name=(TextView) findViewById(R.id.txt_name);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_location_add = (TextView) findViewById(R.id.txt_location_add);
        txt_shareapp = (TextView) findViewById(R.id.txt_shareapp);
        text_logout = (TextView) findViewById(R.id.text_logout);
        sharedPrefs = new SharedPrefs(SettingActivity.this);
        btn_save = (Button) findViewById(R.id.btn_save);
        txt_date = (TextView) findViewById(R.id.txt_date);
        img_profile_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
        txt_rateapp = (TextView) findViewById(R.id.txt_rateapp);
        img_edit_profile = (ImageView) findViewById(R.id.img_edit_profile);
        img_edit_name = (ImageView) findViewById(R.id.img_edit_name);
        liner_first = (LinearLayout) findViewById(R.id.liner_first);
        txt_feedback = (TextView) findViewById(R.id.txt_feedback);
        txt_birthdate = (TextView)findViewById(R.id.txt_birthdate);
        text_favouritecollection = (TextView)findViewById(R.id.text_favouritecollection);

        img_favouritecollectiontxt = (ImageView)findViewById(R.id.img_favouritecollectiontxt);
        spinner_favouritecollection = (Spinner)findViewById(R.id.spinner_favouritecollection);

        relative_spinner = (RelativeLayout)findViewById(R.id.relative_spinner);
        relative_text = (RelativeLayout)findViewById(R.id.relative_text);
    }

    private void set_listeners() {

        img_favouritecollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_favouritecollection.performClick();
            }
        });

        img_favouritecollectiontxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_text.setVisibility(View.GONE);
                relative_spinner.setVisibility(View.VISIBLE);
                spinner_favouritecollection.performClick();
            }
        });
        img_edit_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select_birth_date();
            }
        });
        txt_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String share_string = "Suvlas Application" + "\n" + "https://www.suvlas.com/";
                ;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, share_string);

                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

        txt_rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show rating dialog
                RateThisApp.showRateDialog(SettingActivity.this, R.style.RateDailog);
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TAG_Notification_Status = "on";
                btn_yes.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
                btn_yes.setPadding(24, 24, 24, 24);
                btn_no.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
                btn_no.setPadding(24, 24, 24, 24);

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TAG_Notification_Status = "off";
                btn_yes.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
                btn_yes.setPadding(24, 24, 24, 24);
                btn_no.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
                btn_no.setPadding(24, 24, 24, 24);

            }
        });

//        btn_on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn_on.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
//                btn_on.setPadding(24,24,24,24);
//                btn_off.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
//                btn_off.setPadding(24,24,24,24);
//
//            }
//        });
//        btn_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                btn_on.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
//                btn_on.setPadding(24,24,24,24);
//                btn_off.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
//                btn_off.setPadding(24,24,24,24);
//            }
//        });
        img_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_name.setFocusableInTouchMode(true);
                txt_name.setFocusable(true);
                txt_name.setCursorVisible(true);

            }
        });
        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //display upload photo dialog
                Upload_Photo();
            }
        });

        text_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display logout dialog
                Call_logout_dialog();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //call updateprofile api
                new Profile_Detail_Upload().execute(txt_name.getText().toString());
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        txt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*FeedbackDialog feedbackDialog = new FeedbackDialog(SettingActivity.this);
                feedbackDialog.show();*/

                int appId = 7156; // Replace with your application's ID
                String apiKey = "pbzOjguNYHsl8X8VDOfCqkTLT78FOlQVnJAGHXWBzQ05GJCogZqc54bkynExbLJz"; // Replace with your application's API key
                Doorbell doorbellDialog = new Doorbell(SettingActivity.this, appId, apiKey); // Create the Doorbell object


                if (sharedPrefs.get_Email_id().isEmpty())
                    doorbellDialog.setEmail("name@example.com"); // Prepopulate the email address field
                else
                    doorbellDialog.setEmail(sharedPrefs.get_Email_id()); // Prepopulate the email address field

                Log.e("name",sharedPrefs.get_User_Name());
                doorbellDialog.setTitle("Contacto");
                doorbellDialog.setMessageHint("¿Qué tienes en mente?");
                doorbellDialog.setEmailHint("Introduce tu correo electrónico");
                doorbellDialog.setNegativeButtonText("Cancelar");
                doorbellDialog.setPositiveButtonText("Enviar");
                doorbellDialog.setName(sharedPrefs.get_User_Name()); // Set the name of the user (if known)
                doorbellDialog.addProperty("loggedIn", true); // Optionally add some properties
                doorbellDialog.addProperty("username", sharedPrefs.get_User_Name());
                doorbellDialog.addProperty("loginCount", 123);
                //doorbellDialog.setEmailVisibility(View.GONE); // Hide the email field, since we've filled it in already
                doorbellDialog.setPoweredByVisibility(View.GONE); // Hide the "Powered by Doorbell.io" text

                // Callback for when a message is successfully sent
                doorbellDialog.setOnFeedbackSentCallback(new io.doorbell.android.callbacks.OnFeedbackSentCallback() {
                    @Override
                    public void handle(String message) {
                        // Show the message in a different way, or use your own message!
                        Toast.makeText(SettingActivity.this, getResources().getString(R.string.doobell_msg), Toast.LENGTH_LONG).show();
                    }
                });

                // Callback for when the dialog is shown
                doorbellDialog.setOnShowCallback(new io.doorbell.android.callbacks.OnShowCallback() {
                    @Override
                    public void handle() {
//                        Toast.makeText(activity, "Dialog shown", Toast.LENGTH_LONG).show();
                    }
                });

                doorbellDialog.show();
            }
        });
    }

    private void Upload_Photo() {

        //Display display = this.getWindowManager().getDefaultDisplay();
        final Dialog cp_dialog = new Dialog(SettingActivity.this);
        cp_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cp_dialog.setContentView(R.layout.upload_idproof);
        cp_dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(cp_dialog.getWindow().getAttributes());

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;

        lp.width = (display_width * 90) / 100;
        //lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        cp_dialog.getWindow().setAttributes(lp);

        LinearLayout lin_camera = (LinearLayout) cp_dialog.findViewById(R.id.lin_camera);
        LinearLayout lin_gallery = (LinearLayout) cp_dialog.findViewById(R.id.lin_gallery);

        TextView txt_cancel = (TextView) cp_dialog.findViewById(R.id.txt_cancel);

        lin_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (check_camera_Permission()) {
                        cp_dialog.dismiss();
                        Open_camera();
                    } else {
                        cp_dialog.dismiss();
                        request_camera_Permission();
                    }
                } else {
                    cp_dialog.dismiss();
                    Open_camera();
                }
            }
        });

        lin_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (check_gallery_Permission()) {
                        cp_dialog.dismiss();
                        Select_Image();
                    } else {
                        cp_dialog.dismiss();
                        request_gallery_Permission();
                    }
                } else {
                    cp_dialog.dismiss();
                    Select_Image();
                }
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_dialog.dismiss();
            }
        });
        cp_dialog.show();
    }

    //image pick using camera
    private void Open_camera() {
        Log.e("CAMERA_OPEN", "CAMERA_OPEN");
        file = null;
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = Uri.fromFile(getOutputMediaFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, Comman_api_name.Take_Photo);
        } catch (Exception e) {
            Log.e("Camera_error", "Camera_error");
        }
    }

    //image pick using gallery
    private void Select_Image() {
        Log.e("IMAGE_OPEN", "IMAGE_OPEN");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Comman_api_name.Gallery);
    }

    private boolean check_camera_Permission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void request_camera_Permission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 11);
    }

    private boolean check_gallery_Permission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void request_gallery_Permission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 22);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11:
                Log.e("Camera", "Camera");
                if (grantResults.length > 0) {

                    boolean camera_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (camera_Accepted) {
                        Open_camera();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow Camera Permission to access the Camera",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, 11);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;

            case 22:
                Log.e("Gallery", "Gallery");
                if (grantResults.length > 0) {

                    boolean Gallery_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (Gallery_Accepted) {
                        Select_Image();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow Storage Permission to access the Gallery",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 22);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SettingActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Comman_api_name.Gallery) {

                Uri selectedImageUri = data.getData();
                String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                Cursor cursor2 = getContentResolver().query(selectedImageUri, filePathColumn2, null, null, null);
                cursor2.moveToFirst();

                int columnIndex1 = cursor2.getColumnIndex(filePathColumn2[0]);
                picturePath = cursor2.getString(columnIndex1);
                cursor2.close();

                Glide.with(SettingActivity.this).load(picturePath).into(img_profile_pic);
                Log.e("path----", picturePath);

            } else if (requestCode == Comman_api_name.Take_Photo) {

                try {
                    picturePath = file.getPath();
                    Log.e("Image Camera Path", "" + picturePath);
                    if (!picturePath.equalsIgnoreCase("")) {
                        Glide.with(SettingActivity.this).load(picturePath).into(img_profile_pic);
                    }
                } catch (Exception e) {
                    Log.e("Erroe_camera", e.toString());
                }
            }
        }
    }

    private void Call_logout_dialog() {
        exit_dialog = new Dialog(SettingActivity.this);
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.setContentView(R.layout.logout_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(exit_dialog.getWindow().getAttributes());

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;

        lp.width = (display_width * 90) / 100;
        //lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        exit_dialog.getWindow().setAttributes(lp);

        Button btn_cancel = (Button) exit_dialog.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) exit_dialog.findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call logout api
                new logout().execute();



            }
        });

        exit_dialog.show();
    }

    private void init() {

        restaurant_name_list = new ArrayList<>();

        new Store_Location().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mCrypt = new MCrypt();
        loader = new Request_loader(this);
        sharedPrefs = new SharedPrefs(this);
        comman_dialog = new Comman_Dialog(this);
        RateThisApp.init(new RateThisApp.Config(3, 5));
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        Calendar date = Calendar.getInstance();

        cal_year = date.get(Calendar.YEAR);
        cal_month = date.get(Calendar.MONTH) + 1;
        cal_day = date.get(Calendar.DAY_OF_MONTH);

        //call editprofilew api
        if (CommanMethod.isInternetOn(this)) {
            new My_card_detail().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
    }

    private class My_card_detail extends AsyncTask<String, Void, String> {
        String Responce_jason = "";
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("userID", sharedPrefs.get_User_id());
                Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.My_card_userprofiledetail, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_settingpaged", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.e("result_settingpage", result + "");

            try {
                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);
                    Log.e("mycard", String.valueOf(main_obj));
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {
                        JSONObject data_array = main_obj.getJSONObject("data");

                        String name = data_array.getString("name");
                        String notification_status = data_array.getString("notification");
                        createdate = data_array.getString("created_at");

                        String date_Of_birth = data_array.getString("date_of_birth");

                        txt_birthdate.setText(date_Of_birth);
                        String lati = data_array.getString("latitude");
                        String longi = data_array.getString("longitude");

                        String store_id = data_array.getString("store_id");

                        String favourite_location_id = store_id;
                        for (int i = 0; i < spinner_favouritecollection.getCount(); i++) {
                            if (restaurant_name_list.get(i).getId().equals(favourite_location_id)) {
                                text_favouritecollection.setText(restaurant_name_list.get(i).getName());
                                spinner_favouritecollection.setSelection(i);
                            }
                        }

                        Log.e("lati",lati);
                        Log.e("longi",longi);

                        double latitude = 0.0,longitude = 0.0;
                        if (!lati.equalsIgnoreCase("") && !longi.equalsIgnoreCase(""))
                        {
                             latitude = Double.parseDouble(lati);
                             longitude = Double.parseDouble(longi);
                        }


                        profile_pic = data_array.getString("profile_image_url");
                        if (!profile_pic.equalsIgnoreCase("")) {
                            Glide.with(SettingActivity.this).load(profile_pic).into(img_profile_pic);
                        }
                        txt_name.setText(name);
                        TAG_Notification_Status = notification_status;
                        Log.e("status", TAG_Notification_Status);
                        if (TAG_Notification_Status.equalsIgnoreCase("off")) {
                            btn_yes.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
                            btn_yes.setPadding(24, 24, 24, 24);
                            btn_no.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
                            btn_no.setPadding(24, 24, 24, 24);
                        } else {
                            btn_yes.setBackground(getResources().getDrawable(R.drawable.setting_btn_on));
                            btn_yes.setPadding(24, 24, 24, 24);
                            btn_no.setBackground(getResources().getDrawable(R.drawable.setting_btn_off));
                            btn_no.setPadding(24, 24, 24, 24);
                        }
                        try {

                            if (latitude != 0.0 && longitude != 0.0) {

                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                txt_location_add.setText(address+ ", " +city);

                                Log.e("loca",address+""+city);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String[] parts = createdate.split(" ");
                        String date = parts[0];
                        SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = df_date.parse(date);
                            df_date = new SimpleDateFormat("MMMM yyyy");
                            String final_date = df_date.format(newDate);
                            Log.e("final_date", final_date);

                            txt_date.setText(final_date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }
            } catch (JSONException e) {
                Log.e("result_settingpage", e.toString());
            }
            loader.hidepDialog();
        }
    }

    private void Select_birth_date() {

        final com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog();
        dpd.setOnDateSetListener(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                int month = monthOfYear + 1;

                String Sele_month = "";
                String Sele_day = "";

                if (month < 10) {
                    Sele_month = "0" + month;
                } else {
                    Sele_month = String.valueOf(month);
                }

                if (dayOfMonth < 10) {
                    Sele_day = "0" + dayOfMonth;
                } else {
                    Sele_day = String.valueOf(dayOfMonth);
                }

                //String date = Sele_day + "-" + Sele_month + "-" + year;

                String date = year + "-" + Sele_month + "-" + Sele_day;
//                String date = Sele_month + "/" + Sele_day + "/" + year;

                if (year < cal_year) {
                    txt_birthdate.setText(date);
                    dpd.dismiss();

                } else if (year == cal_year) {
                    if (month < cal_month) {
                        txt_birthdate.setText(date);
                        dpd.dismiss();
                    } else if (month == cal_month) {
                        if (dayOfMonth < cal_day) {
                            txt_birthdate.setText(date);
                            dpd.dismiss();
                        } else {
                            comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                        }
                    } else {
                        comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                    }
                } else {
                    comman_dialog.Show_alert((getResources().getString(R.string.not_validdate)));
                }
            }
        });
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    private class Profile_Detail_Upload extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();
                File sourceFile = new File(picturePath);

                Log.e("File...::::", "" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE = picturePath.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image/jpeg");

                String name_escape = StringEscapeUtils.escapeHtml4(params[0]);

                String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                MultipartBody.Builder buildernew;
                if (filename.equalsIgnoreCase("")) {
                    buildernew = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                            .addFormDataPart("name", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)))
                            .addFormDataPart("username", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)))
                            .addFormDataPart("notification", MCrypt.bytesToHex(mCrypt.encrypt(TAG_Notification_Status)))
                            .addFormDataPart("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselectedstorename)))
                            .addFormDataPart("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(txt_birthdate.getText().toString())))
                            .addFormDataPart("Apikey", Comman_url.api_key);

                } else {
                    buildernew = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                            .addFormDataPart("name", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)))
                            .addFormDataPart("username", MCrypt.bytesToHex(mCrypt.encrypt(name_escape)))
                            .addFormDataPart("notification", MCrypt.bytesToHex(mCrypt.encrypt(TAG_Notification_Status)))
                            .addFormDataPart("profileimage", filename, RequestBody.create(MediaType.parse("image/jpeg"), sourceFile))
                            .addFormDataPart("favorite_location", MCrypt.bytesToHex(mCrypt.encrypt(spinnerselectedstorename)))
                            .addFormDataPart("date_of_birth", MCrypt.bytesToHex(mCrypt.encrypt(txt_birthdate.getText().toString())))
                            .addFormDataPart("Apikey", Comman_url.api_key);
                }
                RequestBody requestBody = buildernew.build();

                //Log.e("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())));
                //Log.e("username", MCrypt.bytesToHex(mCrypt.encrypt(params[0])));
                //Log.e("notification", MCrypt.bytesToHex(mCrypt.encrypt(TAG_Notification_Status)));
                //Log.e("profileimage", " + " + RequestBody.create(MEDIA_TYPE, sourceFile));
                Responce_jason = CallingMethod.POST(client, Comman_api_name.upload_user_detail, requestBody);

            } catch (Exception e) {
                Log.e("errrrrr", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.e("exception_simage_upload", result + "");

            try {

                if (result != null) {

                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {
                        JSONArray data_array = main_obj.getJSONArray("data");

                        JSONObject user_data = data_array.getJSONObject(0);

                        String name = user_data.getString("name");
                        String profile_pic = user_data.getString("profile_image_url");

                        Log.e("image", profile_pic);
                        Log.e("name", name);
                        txt_name.setText(name);
                        Glide.with(SettingActivity.this).load(profile_pic).into(img_profile_pic);
                        CommanMethod.snackBar(liner_first, "User Detail updated Sucessfully !");
                        txt_name.setFocusableInTouchMode(false);
                        txt_name.setFocusable(false);
                        txt_name.setCursorVisible(false);

                    } else {
                        comman_dialog.Show_alert(main_obj.getString("message"));
                    }
                }

            } catch (Exception e) {
                Log.e("exception_simage_upload", e.toString());
            }
            loader.hidepDialog();
        }
    }

    private class logout extends AsyncTask<String, Void, String> {
        String Responce_jason = "";
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //loader.showpDialog();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("userID", MCrypt.bytesToHex(mCrypt.encrypt(sharedPrefs.get_User_id())))
                        .add("Apikey", Comman_url.api_key)
                        .build();

                Log.e("userID", sharedPrefs.get_User_id());
                Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Logout, reqbody);

            } catch (Exception e) {
                Log.e("errrrrr_settingpaged", e.toString());
            }
            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.e("result_settingpage", result + "");

            try {
                JSONObject logoutjson = new JSONObject(result);

                if (logoutjson.getString("code").equalsIgnoreCase("200"))
                {
                    exit_dialog.dismiss();
                    sharedPrefs.save_User_id("");
                    sharedPrefs.save_User_name("");
                    sharedPrefs.save_Gender("");
                    sharedPrefs.save_DOB("");
                    sharedPrefs.save_Email_id("");

                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    exit_dialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private class Store_Location extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
           // loader.showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = new FormBody.Builder()
                        .add("Apikey", Comman_url.api_key)
                        .build();

                //Log.e("Apikey", Comman_url.api_key);

                Responce_jason = CallingMethod.POST(client, Comman_api_name.Get_store_location, reqbody);

            } catch (Exception e) {
                Log.e("err_store_list", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.e("err_store_list2location", result + "");

            //Toast.makeText(getBaseContext(),"store location response",Toast.LENGTH_SHORT).show();
            if (result != null) {
                try {
                    JSONObject main_obj = new JSONObject(result);
                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Stores");
                        //Log.e("data_array", String.valueOf(data_array));

                        for (int i = 0; i < data_array.length(); i++) {

                            JSONObject json_data = data_array.getJSONObject(i);

                            String restaurent_name = json_data.getString("name");
                            String restaurant_id = json_data.getString("id");
                            restaurant_name_list.add(new Restaurant_list(restaurent_name, restaurant_id));
                        }

                        Log.e("sizeada", String.valueOf(restaurant_name_list.size()));

                        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(SettingActivity.this, restaurant_name_list);
                        spinner_favouritecollection.setAdapter(customSpinnerAdapter);

                        /*SpinnerStoreAdapter spinnerStoreAdapter = new SpinnerStoreAdapter(SettingActivity.this,R.layout.setting_favourite_location,restaurant_name_list);
                        spinner_favouritecollection.setAdapter(spinnerStoreAdapter);*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
               // loader.hidepDialog();
            }
        }
    }

    public class CustomSpinnerAdapter extends BaseAdapter {

        Context context;
        ArrayList<Restaurant_list> restaurant_lists;

        public CustomSpinnerAdapter(Context context, ArrayList<Restaurant_list> restaurant_lists) {
            this.context = context;
            this.restaurant_lists = restaurant_lists;
        }

        @Override
        public int getCount() {
            return restaurant_lists.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.setting_favourite_location, parent, false);

            TextView store_name = (TextView) itemView.findViewById(R.id.store_name);
            store_name.setText(restaurant_lists.get(position).getName());

            spinner_favouritecollection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    spinnerselectedstorename = restaurant_lists.get(position).getId();
                    Log.e("spinnserid", restaurant_lists.get(position).getId());
                    text_favouritecollection.setText(restaurant_lists.get(position).getName());
                    relative_spinner.setVisibility(View.GONE);
                    relative_text.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return itemView;
        }
    }
}