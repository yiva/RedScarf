package com.redscarf.weidou.activity;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetailActivity extends BaseActivity implements
        FoodDetailFragment.OnShowMapListener {

    private int MSG_SUCCESS = 1;
    private int MSG_FAIL = 2;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SUCCESS) {
                setUpMapIfNeeded();
            }else if(msg.what == MSG_FAIL){
                setDefaultLocation();
//                Toast.makeText(FoodDetailActivity.this, "地理信息获取失败", Toast.LENGTH_LONG).show();
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
    protected Location mLastLocation;

    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    int PLACE_PICKER_REQUEST = 1;

    private Bundle datas;
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
        this.findViewById(R.id.actionbar_food_detail_back).setOnClickListener(new
                OnBackClickListener());
    }

    @Override
    public void sendLcation(FoodDetailBody food) {
        food_body = food;
        if (food_body != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Geocoder geoCoder = new Geocoder(FoodDetailActivity.this, Locale.getDefault());
                    Message message = new Message();
                    try {
                        String location = food_body.getSubtitle()+"/n";
                        location = location.split("/n")[0];
                        addr = geoCoder.getFromLocationName(location, 5);
                        if (addr.size() > 0) {
                            message = Message.obtain(mainHandler, MSG_SUCCESS);
                        }else{
                            message = Message.obtain(mainHandler, MSG_FAIL);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        message = Message.obtain(mainHandler, MSG_FAIL);
                    }finally {
                        mainHandler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.food_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        lng = new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude());
            MarkerOptions options = new MarkerOptions().position(lng)
                    .title(food_body.getTitle())
                    .snippet(food_body.getTitle() + "\r\n" + food_body.getSubtitle())
                    .flat(true);
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
            mMap.setMyLocationEnabled(true);
    }

    private void setDefaultLocation() {
        // TODO Auto-generated catch block
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.food_map))
                    .getMap();
        }
        LatLng lng = new LatLng(51.528308, -0.3817765);
        MarkerOptions options = new MarkerOptions().position(lng).flat(true);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
        mMap.setMyLocationEnabled(true);
    }

}
