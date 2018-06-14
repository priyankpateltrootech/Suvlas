package com.suvlas;

/**
 * Created by hp on 3/9/2017.
 */


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.StoreItem;
import common.CallingMethod;
import common.Comman_Dialog;
import common.Comman_api_name;
import common.Comman_url;
import common.MCrypt;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.suvlas.R.id.map;


public class SearchActivity extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener,
        View.OnClickListener {

    //Defining Variables
    public Toolbar toolbar;
    public Marker marker;
    ImageView img_back, img_res_food;
    TabLayout tab;
    RelativeLayout rel_top;
    TextView text_status;
    GoogleApiClient googleApiClient;
    View rootView;
    Button btn_new_order;
    MCrypt mCrypt;
    int page_count = 0;
    ArrayList<StoreItem> store_item = new ArrayList<>();
    StoreItem item;
    String name, contact_number, opening_time_store, closing_time_store, store_img;
    Bundle bundle;
    Comman_Dialog comman_dialog;
    private GoogleMap mMap;
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private ImageButton buttonCurrent;
    private Marker mMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.neworderfragment, container, false);

        // initialize all component
        findviewID();

        // onclick listener method for required components
        set_listeners();
        //set_font();

        //initialize component
        init();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return rootView;
    }


    private void findviewID() {

        btn_new_order = (Button) rootView.findViewById(R.id.btn_new_order);
        img_back = (ImageView) rootView.findViewById(R.id.img_back);
    }

    private void set_listeners() {
        img_back.setOnClickListener(this);
        btn_new_order.setOnClickListener(this);
    }
//    public void onMapSearch(View view) {
//        EditText locationSearch = (EditText) rootView.findViewById(R.id.editText);
//        String location = locationSearch.getText().toString();
//        List<Address> addressList = null;
//
//        if (location != null || !location.equals("")) {
//            Geocoder geocoder = new Geocoder(getActivity());
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        }
//    }

    private void init() {
        mCrypt = new MCrypt();
        comman_dialog = new Comman_Dialog(getActivity());
//        item = new StoreItem();
//        new Store_Location().execute();

    }

    private void backmenu() {
        android.support.v4.app.FragmentManager fm = this.getChildFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
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
//        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//        mMap.setOnInfoWindowClickListener(this);
//        final Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude)));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.info_card)));

        //Adding marker to map
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng) //setting position
//                .draggable(true) //Making the marker draggable
//                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Displaying current coordinates in toast
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.setOnMarkerDragListener(this);
//        mMap.setOnMapLongClickListener(this);


    }

    @Override
    public void onConnected(Bundle bundle) {
//        item = new StoreItem();
        store_item = new ArrayList<>();

        //call store location api
        new Store_Location().execute();

//        getCurrentLocation();
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
//                Intent intent_order = new Intent(getContext(), MainActivity.class);
//                startActivity(intent_order);

                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.btn_new_order:
                comman_dialog.Show_alert("please Select Restaurant");
//                Fragment fragment = new OrderItemsSelect();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fram_map, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
        }

//        if (v == buttonCurrent) {
//            getCurrentLocation();
//            moveMap();
//        }
    }


    //infowindow onclick
    @Override
    public void onInfoWindowClick(Marker marker) {
        bundle = new Bundle();
        bundle.putString("latitude", String.valueOf(marker.getPosition().latitude));
        bundle.putString("longitude", String.valueOf(marker.getPosition().longitude));
        bundle.putString("name", String.valueOf(marker.getTitle()));


        Fragment fragment = new OrderItemsSelect();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fram_map, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //create marker
    protected Marker createMarker(double latitude, double longitude, String storeImg, String name, String contact_number, String opening_time, String closing_time_store) {

        this.name = name;
        this.contact_number = contact_number;
        this.opening_time_store = opening_time;
        this.closing_time_store = closing_time_store;
        this.store_img = store_img;

        Log.e("name", name);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(name));
        mMap.setOnInfoWindowClickListener(this);

        mMarker= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name));
//        mMarker.setTag(latitude);
//        mMarker.setSnippet(String.valueOf(longitude));
        return mMarker;
    }


    //create custom infowindow
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        ImageView image;
        TextView restaurent_name, address, opening_time, closing_time;
        String url = null;
        RelativeLayout rel_top;
        String name = "";
        private View view;

        public CustomInfoWindowAdapter(String name) {
            this.name = name;
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker2) {


//            String url = null;
//            if (getActivity().marker2 != null
//                    && getActivity().marker.isInfoWindowShown()) {
//                Near_by_me.marker.hideInfoWindow();
//                Near_by_me.marker.showInfoWindow();
//                url = markers.get(marker.getId());
//
////                    Glide.with(Near_by_me.this)
////                            .load(url).placeholder(R.drawable.default_images).into(image);
//
//            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            SearchActivity.this.marker = marker;
            Log.e("CustomInfoWindowAdapter", name);

            image = ((ImageView) view.findViewById(R.id.img_msg));
            restaurent_name = (TextView) view.findViewById(R.id.restaurent_name);
            address = (TextView) view.findViewById(R.id.address);
            opening_time = (TextView) view.findViewById(R.id.opening_time);
            closing_time = (TextView) view.findViewById(R.id.closing_time);
            rel_top = (RelativeLayout) view.findViewById(R.id.rel_top);

            restaurent_name.setText(marker.getTitle());
//            contact_no.setText(contact_number);
            opening_time.setText("Lun - Jue  " + opening_time_store);
            closing_time.setText("Vie - Dom  " + closing_time_store);

//            Near_by_me.marker = marker;
//            String distance = "0";
//            if (marker.getId() != null && markers != null && markers.size() > 0) {
//                if (markers.get(marker.getId()) != null &&
//                        markers.get(marker.getId()) != null) {
//
//                }
//            }
//            try {
//                image.setImageBitmap(markers_Iamge.get(marker.getId()));
//            } catch (Exception e) {
//
//            }
//
//            final String finalUrl = url;
//            url = markers.get(marker.getId());
//            offer_discout.setText(markers_discout.get(marker.getId()));
//            Log.e("url", "" + url);
//
//
//            final String title = marker.getTitle();
//            Log.e("title",""+title);
//            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
//            if (title != null) {
//                titleUi.setText(title);
//            } else {
//                titleUi.setText("");
//            }
//
//            final String snippet = marker.getSnippet();
//            final TextView snippetUi = ((TextView) view
//                    .findViewById(R.id.snippet));
//            if (snippet != null) {
//                snippetUi.setText(snippet);
//            } else {
//                snippetUi.setText("");
//            }
//            distance = markers_distance.get(marker.getId());
//            Log.e("distance",""+distance);
//
//            txt_km.setText(distance + " k.m.");
//
//
//                Glide.with(Near_by_me.this)
//                        .load(url).placeholder(R.drawable.default_images).into(image);
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
//                        .add("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("10")))
//                        .add("page_offset", MCrypt.bytesToHex(mCrypt.encrypt(page_count + "")))
                        .add("Apikey", Comman_url.api_key)
                        .build();

//                Log.e("page_limit", "10");
//                Log.e("page_offset11", page_count + "");
                Log.e("Apikey", Comman_url.api_key);

//                Log.e("page_limit", MCrypt.bytesToHex(mCrypt.encrypt("10")));
//                Log.e("page_offset11", MCrypt.bytesToHex(mCrypt.encrypt(page_count + "")));
                Log.e("Apikey", Comman_url.api_key);

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

            Log.e("err_store_list2", result + "");

            try {
                if (result != null) {
                    JSONObject main_obj = new JSONObject(result);

                    if (main_obj.getString("code").equalsIgnoreCase("200")) {

                        JSONArray data_array = main_obj.getJSONArray("Stores");
                        Log.e("data_array", String.valueOf(data_array));

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
                            Log.e("va;lue", String.valueOf(store_logo));


                            store_item.add(new StoreItem(latitude_data, longitude_data, store_logo, restaurent_name, description, location, contact_number, opening_time, closing_time));

                                longitude = Double.parseDouble(String.valueOf(store_item.get(i).getLongitude()));
                                latitude = Double.parseDouble(String.valueOf(store_item.get(i).getLatitude()));

                                Log.e("latitude", String.valueOf(latitude_data));
                                Log.e("longitude", String.valueOf(longitude_data));

                                createMarker(store_item.get(i).getLatitude(), store_item.get(i).getLongitude(), store_item.get(i).getStore_img(),
                                        store_item.get(i).getName(), store_item.get(i).getContact_number(), store_item.get(i).getOpening_time(), store_item.get(i).getClosing_time());
//                            }
                        }
                        getCurrentLocation();
                    }

                }
            } catch (JSONException e) {
                Log.e("err_Offer_list", e.toString());
            }

        }
    }
}
