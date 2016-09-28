package com.redscarf.weidou.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.redscarf.weidou.activity.fragment.FoodDetailFragment;
import com.redscarf.weidou.pojo.FoodDetailBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetailActivity extends BaseActivity implements
        FoodDetailFragment.OnShowMapListener,OnMapReadyCallback {

    private int MSG_SUCCESS = 1;
    private int MSG_FAIL = 2;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == MSG_SUCCESS) {
                    setUpMapIfNeeded();
                } else if (msg.what == MSG_FAIL) {
                    setDefaultLocation();
//                Toast.makeText(FoodDetailActivity.this, "地理信息获取失败", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ExceptionUtil.printAndRecord(TAG, ex);
            }
        }
    };

    protected static final String TAG = "basic-location-sample";

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    private LatLng lng;
    private FoodDetailBody food_body;
    private GoogleMap mMap;
    private List<Address> addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_food_detail);
        GlobalApplication.getInstance().addActivity(this);
        this.initView();

    }

    @Override
    public void initView() {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.food_map);
        fragment.getMapAsync(this);
    }

    @Override
    public void sendLcation(FoodDetailBody food) {
        food_body = food;
        try {
            if (food_body != null) {
                setUpMapIfNeeded();
            } else {
                setDefaultLocation();
            }
        }catch (Exception ex) {
            ExceptionUtil.printAndRecord(TAG, ex);
        }

    }

//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.food_map);
//                    .getMapAsync(this);
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
//    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(FoodDetailActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) !=
                PackageManager
                        .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FoodDetailActivity.this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void setUpMapIfNeeded() {
        lng = new LatLng(Double.parseDouble(food_body.getPost_lat()),
                Double.parseDouble(food_body.getPost_lng()));
        MarkerOptions options = new MarkerOptions().position(lng)
                .title(food_body.getTitle())
                .snippet(food_body.getTitle() + "\r\n" + food_body.getSubtitle())
                .flat(true);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
    }
    private void setDefaultLocation() {
        // TODO Auto-generated catch block
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.food_map))
                    .getMapAsync(this);
        }
        LatLng lng = new LatLng(51.528308, -0.3817765);
        MarkerOptions options = new MarkerOptions().position(lng).flat(true);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }
}
