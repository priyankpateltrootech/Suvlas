package com.suvlas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import adpter.CustomViewPagerAdapter_StoreLocation;
import adpter.SearchAdapter;
import adpter.SpinnerAdapter;
import bean.StoreItem;
import bean.Store_img_item;
import common.CallingMethod;
import common.CirclePageIndicator;
import common.CommanMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_message;
import common.Comman_url;
import common.MCrypt;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by hp on 5/9/2017.
 */

public class StoreActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    //Defining Variables
    public Toolbar toolbar;
    GoogleApiClient googleApiClient;
    Button btn_new_order;
    TextView text_status, text_map, txt_availablerest;
    public static EditText txt_search;
    //public static Spinner spinner_search;
    RelativeLayout rel_top, rel_map, rel_scnd, rel_scnd2;
    ImageView img_back, search_button;
    MCrypt mCrypt;
    ArrayList<StoreItem> store_item = new ArrayList<>();
    public static ArrayList<Store_img_item> store_img_item = new ArrayList<>();
    String name, contact_number, opening_time_store, closing_time_store;
    public static CustomViewPagerAdapter_StoreLocation mAdapter;
    Comman_Dialog comman_dialog;
    public static CirclePageIndicator indicator;
    public static GoogleMap mMap;
    private double longitude;
    private double latitude;
    public static ViewPager viewPager;
    private Marker mMarker;
    SearchAdapter searchAdapter;
    public static ListView listview;
    CameraUpdate cameraUpdate;
    SortPlaces sortPlaces;
    ArrayList<String> restaurant_name_list;
    String spinnerselecteditem;
    public static TextView spinner_search;
    RelativeLayout relative_search;
    //TextView textview;
    String locationstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    private void findviewID() {
        text_status = (TextView) findViewById(R.id.txt_status);
        text_map = (TextView) findViewById(R.id.text_map);
        rel_top = (RelativeLayout) findViewById(R.id.rel_top);
        rel_map = (RelativeLayout) findViewById(R.id.rel_map);
        rel_scnd = (RelativeLayout) findViewById(R.id.rel_scnd);
        rel_scnd2 = (RelativeLayout) findViewById(R.id.rel_scnd2);
        btn_new_order = (Button) findViewById(R.id.btn_new_order);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_availablerest = (TextView) findViewById(R.id.txt_availablerest);
        viewPager = (ViewPager) findViewById(R.id.hot_deal_view_pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        spinner_search = (TextView) findViewById(R.id.spinner_search);
        search_button = (ImageView) findViewById(R.id.search_button);
        //spinner_search = (Spinner)findViewById(R.id.spinner_search);
        relative_search = (RelativeLayout)findViewById(R.id.relative_search);

        listview = (ListView) findViewById(R.id.listview);

       /* textview = (TextView)findViewById(R.id.textview);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("text","textview");
            }
        });*/
        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String s = adapterView.getAdapter().getItem(i).toString();
                mMap.setOnMarkerClickListener(StoreActivity.this);
                StoreItem store = (StoreItem) adapterView.getItemAtPosition(i);
                String s = store.getName();
                Log.e("nameaaaaaaaaaaaa",s);
                Log.e("size", String.valueOf(store.getStore_img_items().size()));
                mAdapter = new CustomViewPagerAdapter_StoreLocation(StoreActivity.this,store.getStore_img_items());
                viewPager.setAdapter(mAdapter);
                indicator.setViewPager(viewPager);


                //((StoreItem)listview.getAdapter().getItem(i)).getClosing_time();

            }
         });*/
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        text_map.setOnClickListener(this);
        search_button.setOnClickListener(this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        spinner_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setVisibility(View.VISIBLE);
                Log.e("hi","hi");
            }
        });

        relative_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(this);
        restaurant_name_list = new ArrayList<>();
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else
        {
            Log.e("locaiton","location");
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            locationstatus = "on";
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            moveMap();
        }
        else
        {
            locationstatus = "off";
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {

        Log.e("locationstatus",locationstatus);
        String msg = latitude + ", " + longitude;
        LatLng latLng = new LatLng(latitude, longitude);

        //Log.e("currentlatlng", msg);

        for (StoreItem p : store_item) {
            //Log.e("Places before sorting", "Place: " + p.getName());
            //Log.e("Places after sorting", "Place: " + p.getLatitude());
            //Log.e("Places after sorting", "Place: " + p.getLongitude());
        }

        //sorting places using nearby location
        sortPlaces = new SortPlaces(latLng);
        Collections.sort(store_item, sortPlaces);

        for (StoreItem p : store_item) {
            //Log.e("Places before sorting", "Place: " + p.getName());
            //Log.e("Places after sorting", "Place: " + p.getLatitude());
            //Log.e("Places after sorting", "Place: " + p.getLongitude());
        }

        //viewpager bind with adapter
        for (int i = 0; i < store_item.size(); i++) {
            //Log.e("Places before sorting forsadasdasdsad", "Place: " + store_item.get(0).getName());
            //Log.e("Places before sorting forsadasdasdsad", "Place: " + store_item.get(0).getLatitude());
            //Log.e("Places before sorting forsadasdasdsad", "Place: " + store_item.get(0).getLongitude());
            LatLng nearlatLng = new LatLng(store_item.get(0).getLatitude(), store_item.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(nearlatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            //Log.e("Places before sorting forsadasdasdsad", "Place: " + store_item.get(0).getStore_img_items().size());

            store_img_item =  new ArrayList<Store_img_item>();
            store_img_item.addAll(store_item.get(0).getStore_img_items());
            //Log.e("sizestoreimages", String.valueOf(store_img_item.size()));
            mAdapter = new CustomViewPagerAdapter_StoreLocation(StoreActivity.this, store_img_item);
            viewPager.setAdapter(mAdapter);
            indicator.setViewPager(viewPager);
            mAdapter.notifyDataSetChanged();
            break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        //LatLng latLng = new LatLng(latitude, longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        //call store location api
        if (CommanMethod.isInternetOn(StoreActivity.this)) {
            store_item = new ArrayList<>();
            store_img_item = new ArrayList<>();
            new Store_Location().execute();
        } else {
            comman_dialog.Show_alert(Comman_message.Dont_internet);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        //Moving the map
        moveMap();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;
            case R.id.search_button:
               // spinner_search.setVisibility(View.VISIBLE);
                //spinner_search.performClick();
                listview.setVisibility(View.VISIBLE);
                break;

        }
    }

    //create and put marker on map
    public Marker createMarker(double latitude, double longitude, final String storeImg, String name, String description, String contact_number, String opening_time, String closing_time_store, ArrayList<Store_img_item> store_img_items, ArrayList<StoreItem> store_item) {
        this.name = name;
        this.contact_number = contact_number;
        this.opening_time_store = opening_time;
        this.closing_time_store = closing_time_store;
        this.store_img_item = store_img_items;

        //Log.e("store", String.valueOf(store_item.size()));
        store_img_item = new ArrayList<>();
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .snippet(description)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.c_marker))
                .title(name));
        mMarker.setTag(store_img_items);
        //store_img_item = (ArrayList<Store_img_item>) mMarker.getTag();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        return mMarker;
    }


    //marker onclick
    @Override
    public boolean onMarkerClick(Marker marker) {
        store_img_item = (ArrayList<Store_img_item>) marker.getTag();

        /*if (rel_scnd2.getVisibility() == View.VISIBLE) {
            rel_scnd.setVisibility(View.VISIBLE);
            rel_scnd2.setVisibility(View.GONE);
        }*/
        //viewpager bind with adapter
        mAdapter = new CustomViewPagerAdapter_StoreLocation(StoreActivity.this, store_img_item);
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);
        //Log.e("marker", marker.getPosition() + "");
        return false;
    }

    //create infowindow
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        ImageView image;
        TextView restaurent_name, address, opening_time, closing_time, telefone_number;
        RelativeLayout rel_top;
        String name, contact_number, opening_time_store, closing_time_store;
        double latitude, longitude;
        private View view;

        public CustomInfoWindowAdapter(double latitude, double longitude, String contact_number, String opening_time, String closing_time) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.contact_number = contact_number;
            this.opening_time_store = opening_time;
            this.closing_time_store = closing_time;
            view = getLayoutInflater().inflate(R.layout.custom_map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker2) {
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            String markerlat = String.valueOf(marker.getPosition().latitude);
            String markerlong = String.valueOf(marker.getPosition().longitude);

            //Log.e("markerlatitude", String.valueOf(marker.getPosition().latitude));

            image = ((ImageView) view.findViewById(R.id.img_msg));
            restaurent_name = (TextView) view.findViewById(R.id.restaurent_name);
            address = (TextView) view.findViewById(R.id.address);
            opening_time = (TextView) view.findViewById(R.id.opening_time);
            closing_time = (TextView) view.findViewById(R.id.closing_time);
            telefone_number = (TextView) view.findViewById(R.id.telefone_number);
            rel_top = (RelativeLayout) view.findViewById(R.id.rel_top);

            restaurent_name.setText(marker.getTitle());
            address.setText(marker.getSnippet());
            for (int i = 0; i < store_item.size(); i++) {

                if (markerlat.equalsIgnoreCase(String.valueOf(store_item.get(i).getLatitude())) && markerlong.equalsIgnoreCase(String.valueOf(store_item.get(i).getLongitude()))) {
                    opening_time.setText("Lun - Jue  " + store_item.get(i).getOpening_time());
                    closing_time.setText("Vie - Dom  " + store_item.get(i).getClosing_time());
                    telefone_number.setText(store_item.get(i).getContact_number());
                }
            }
            return view;
        }
    }

    private class Store_Location extends AsyncTask<String, Void, String> {

        String Responce_jason = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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
                //Log.e("err_store_list", e.toString());
            }

            return Responce_jason;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //Log.e("err_store_list2", result + "");
            try {
                if (result != null) {
                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Stores");
                        //Log.e("data_array", String.valueOf(data_array));

                        for (int i = 0; i < data_array.length(); i++) {



                            JSONObject json_data = data_array.getJSONObject(i);

                            double latitude_data = Double.parseDouble(json_data.getString("latitude"));
                            double longitude_data = Double.parseDouble(json_data.getString("longitude"));
                            String store_logo = json_data.getString("store_logo");
                            String restaurent_name = json_data.getString("name");
                            String description = json_data.getString("description");
                            String location = json_data.getString("location");
                            String contact_number = json_data.getString("contact_number");
                            String opening_time = json_data.getString("mon_opening_time");
                            String closing_time = json_data.getString("mon_closing_time");

                            restaurant_name_list.add(restaurent_name);

                            JSONArray jsonarray_imagestore = json_data.getJSONArray("stores_images");
                            for (int j = 0; j < jsonarray_imagestore.length(); j++) {
                                JSONObject jsonObject_imge_store = jsonarray_imagestore.getJSONObject(j);

                                String store_image = jsonObject_imge_store.getString("image_file");
                                String stores_id = jsonObject_imge_store.getString("stores_id");
                                //Log.e("store_image", store_image);
                                store_img_item.add(new Store_img_item(store_image, stores_id));
                            }
                            //Log.e("store_img_item", String.valueOf(store_img_item.size()));
                            store_item.add(new StoreItem(latitude_data, longitude_data, store_logo, restaurent_name, description,
                                    location, contact_number, opening_time, closing_time, store_img_item));

                            //Log.e("storesizee", String.valueOf(store_item.size()));
                            //Log.e("store", store_item.toString());
                            //Log.e("storecontact", store_item.get(0).getName());

                            createMarker(store_item.get(i).getLatitude(), store_item.get(i).getLongitude(), store_item.get(i).getStore_img(),
                                    store_item.get(i).getName(), store_item.get(i).description, store_item.get(i).getContact_number(), store_item.get(i).getOpening_time(),
                                    store_item.get(i).getClosing_time(), store_item.get(i).getStore_img_items(), store_item);
                            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(store_item.get(i).getLatitude(), store_item.get(i).getLongitude(), store_item.get(i).getContact_number(), store_item.get(i).getOpening_time(),
                                    store_item.get(i).getClosing_time()));
                            mMap.setOnInfoWindowClickListener(StoreActivity.this);
                        }

                        //edittext bind with searchadapter
                        searchAdapter = new SearchAdapter(StoreActivity.this, store_item);
                        listview.setAdapter(searchAdapter);


                        txt_availablerest.setText("Restaurante" +" "+ store_item.size());
                        getCurrentLocation();
                    }
                }
            } catch (JSONException e) {
                //Log.e("err_store_list", e.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StoreActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}

