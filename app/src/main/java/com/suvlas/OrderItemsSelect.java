package com.suvlas;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adpter.OrderItemAdapter;
import adpter.RecyclerAdapter;
import bean.MenuOrderBean;
import bean.OrderItem;
import common.SharedPrefs;

/**
 * Created by hp on 4/24/2017.
 */

public class OrderItemsSelect extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener {

    //Defining Variables
    public Toolbar toolbar;
    public String latlong;
    public String latlong2;
    public String name;
    GoogleApiClient googleApiClient;
    View rootView;
    Button btn_new_order;
    TabLayout tab;
    AppBarLayout apbar;
    TextView text_status, text_setting;
    RelativeLayout rel_top, rel_map;
    ImageView img_back, img_icon4;
    RelativeLayout rel_park, rel_yog, rel_coca;
    int counter = 0;
    SupportMapFragment mapFragment;
    RecyclerView recycler_view;
    GridLayoutManager lm;
    OrderItemAdapter orderItemAdapter;
    private GoogleMap mMap;
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private ImageButton buttonCurrent;
    ArrayList<MenuOrderBean> menuOrderBeen_shared_pre_array = new ArrayList<>();
    Gson gson;
    SharedPrefs sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.order_item_select, container, false);
            mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Bundle bundle = getArguments();
            latlong = bundle.getString("latitude");
            latlong2 = bundle.getString("longitude");
            name=bundle.getString("name");
            Log.e("latlong", latlong);

            sharedPrefs = new SharedPrefs(getActivity());
            gson = new Gson();

            // initialize all component
            findviewID();

            // onclick listener method for required components
            set_listeners();
            //set_font();

            //initialize component
            init();

            /*if (menuOrderBeen_shared_pre_array.size()>0)
            {
                menuOrderBeen_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<MenuOrderBean>>() {}.getType());
                Log.e("sizeeeeeeee", String.valueOf(menuOrderBeen_shared_pre_array.size()));
            }*/



            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return rootView;
    }


    private void findviewID() {
//        tab = (TabLayout) getActivity().findViewById(R.id.tabs);
        text_status = (TextView) getActivity().findViewById(R.id.txt_status);
        rel_top = (RelativeLayout) getActivity().findViewById(R.id.rel_top);
        rel_map = (RelativeLayout) getActivity().findViewById(R.id.rel_map);
        btn_new_order = (Button) rootView.findViewById(R.id.btn_new_order);
        img_icon4 = (ImageView) rootView.findViewById(R.id.img_icon4);
        img_back = (ImageView) rootView.findViewById(R.id.img_back);
//        rel_park=(RelativeLayout)rootView.findViewById(R.id.rel_park);
//        rel_yog=(RelativeLayout)rootView.findViewById(R.id.rel_yog);
//        rel_coca=(RelativeLayout)rootView.findViewById(R.id.rel_coca);
        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    }

    private void set_listeners() {
        btn_new_order.setOnClickListener(this);
        img_icon4.setOnClickListener(this);
        img_back.setOnClickListener(this);
//        rel_park.setOnClickListener(this);
//        rel_yog.setOnClickListener(this);
//        rel_coca.setOnClickListener(this);
    }


    private void init() {
        List<OrderItem> orderitem = getitem();

        //recyclerview bind with adapter
        /*lm = new GridLayoutManager(getActivity(), 1);
        recycler_view.setLayoutManager(lm);
        orderItemAdapter = new OrderItemAdapter(getActivity(), orderitem);
        recycler_view.setAdapter(orderItemAdapter);*/
//        Point size = new Point();
//        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
//        int display_width = size.x;
//        int display_height = size.y;
//        Log.e("Width", display_width + "==" + display_width);
//        Log.e("Height", display_height + "==" + display_height);
//        rel_map.getLayoutParams().height = (int) (display_height /2);

    }

    private List<OrderItem> getitem() {

        List<OrderItem> allItems = new ArrayList<OrderItem>();
        allItems.add(new OrderItem("Park GeeroCombo", R.drawable.order_item_1));
        allItems.add(new OrderItem("Yogur picante extr", R.drawable.order_item_2));
        allItems.add(new OrderItem("Coca Cola", R.drawable.order_item_3));
        return allItems;
    }

//    public void onMapSearch(View view) {
////        EditText locationSearch = (EditText) rootView.findViewById(R.id.editText);
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
        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        double double_lat = Double.parseDouble(latlong);
        double double_llong = Double.parseDouble(latlong2);
        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(double_lat, double_llong);
        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title(name)); //Adding a title

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
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
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

//        if(v == buttonCurrent) {
//            getCurrentLocation();
//            moveMap();
        switch (v.getId()) {

            case R.id.img_icon4:
                Intent intent = new Intent(getActivity(), MenuOrder.class);
                startActivity(intent);

                break;
            case R.id.btn_new_order:
                //Intent intent2 = new Intent(getActivity(), MenuOrder.class);
                sharedPrefs.save_Custom_array("");
                //startActivity(intent2);
                break;
            case R.id.img_back:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                Intent intent_back = new Intent(getActivity(), NewOrderActivity.class);
//                startActivity(intent_back);
                break;
//            case R.id.rel_park:
//                ChooseItem();
////                Intent intent_back = new Intent(getActivity(), NewOrderActivity.class);
////                startActivity(intent_back);
//                break;
//            case R.id.rel_yog:
//                ChooseItem();
////
//                break;
//            case R.id.rel_coca:
//                ChooseItem();
////
//                break;

//            }
        }
    }

    private void ChooseItem() {
        final Dialog exit_dialog = new Dialog(getActivity());
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.setCancelable(true);
        exit_dialog.setContentView(R.layout.menu_custom_itemdetails);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(exit_dialog.getWindow().getAttributes());
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int display_width = size.x;
        int display_height = size.y;
        lp.width = (display_width * 90) / 100;
        //lp.height = (display_height * 80) / 100;
        lp.gravity = Gravity.CENTER;
        //lp.verticalMargin = 10;
        exit_dialog.getWindow().setAttributes(lp);

        Button btn_decrease, btn_increase;
        final TextView txt_desimal;

        btn_increase = (Button) exit_dialog.findViewById(R.id.btn_increase);
        btn_decrease = (Button) exit_dialog.findViewById(R.id.btn_decrease);
        txt_desimal = (TextView) exit_dialog.findViewById(R.id.txt_desimal);
        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                txt_desimal.setText(counter + "");
                Log.e("hello", String.valueOf(counter + ""));
//                showText = true;
            }
        });
        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter--;
                    txt_desimal.setText(counter + "");
//                    showText = true;
                }
            }
        });
//
        Button btn_continue = (Button) exit_dialog.findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exit_dialog.dismiss();
//                Fragment fragment2 = new OrderItemsSelect();
//                FragmentManager fm = mActivity.getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(R.id.fram_menuorder, fragment2);
//                ft.commit();


            }
        });
        exit_dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("resume","resume");

        Log.e("sharedpreferences",sharedPrefs.get_Custom_array());

        if (sharedPrefs.get_Custom_array() != null)
        {
            menuOrderBeen_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<MenuOrderBean>>() {}.getType());

            if (menuOrderBeen_shared_pre_array != null)
            {
                Log.e("sizeeeeeeeeresumeordr", String.valueOf(menuOrderBeen_shared_pre_array.size()));
                recycler_view.setVisibility(View.VISIBLE);
                lm = new GridLayoutManager(getActivity(), 1);
                recycler_view.setLayoutManager(lm);
                orderItemAdapter = new OrderItemAdapter(getActivity(), menuOrderBeen_shared_pre_array);
                recycler_view.setAdapter(orderItemAdapter);
            }
        }
        /*menuOrderBeen_shared_pre_array = gson.fromJson(sharedPrefs.get_Custom_array(), new TypeToken<ArrayList<MenuOrderBean>>() {}.getType());

        if (menuOrderBeen_shared_pre_array.size()>0)
        {
            Log.e("sizeeeeeeeeresumeordr", String.valueOf(menuOrderBeen_shared_pre_array.size()));
        }*/
        /*if (RecyclerAdapter.menu_order_bean.size()>0)
        {
            Log.e("sizeorder",RecyclerAdapter.menu_order_bean.size()+"");
            recycler_view.setVisibility(View.VISIBLE);
            lm = new GridLayoutManager(getActivity(), 1);
            recycler_view.setLayoutManager(lm);
            orderItemAdapter = new OrderItemAdapter(getActivity(), RecyclerAdapter.menu_order_bean);
            recycler_view.setAdapter(orderItemAdapter);
        }*/
    }
}


