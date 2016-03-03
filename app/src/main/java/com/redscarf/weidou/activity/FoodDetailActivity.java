package com.redscarf.weidou.activity;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.redscarf.weidou.activity.fragment.FoodDetailFragment;
import com.redscarf.weidou.pojo.FoodDetailBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetailActivity extends BaseActivity implements OnMapReadyCallback,
		FoodDetailFragment.OnShowMapListener {


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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_food_detail);
//		this.setActionBarLayout(R.layout.actionbar_food_detail);
		this.registerButton();

	}

	public void registerButton(){
		this.findViewById(R.id.actionbar_food_detail_back).setOnClickListener(new
				OnBackClickListener());
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		List<Address> addr = new ArrayList<Address>();
		try {
			addr = geoCoder.getFromLocationName(food_body.getLocation(), 5);
			lng = new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude());
			if (addr.size() > 0) {

				MarkerOptions options = new MarkerOptions().position(lng)
        				.title(food_body.getTitle())
						.snippet(food_body.getTitle() + "\r\n" + food_body.getSubtitle())
						.flat(true);
				mMap.addMarker(options);
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
				mMap.setMyLocationEnabled(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LatLng lng = new LatLng(0, 0);
			MarkerOptions options = new MarkerOptions().position(lng)
					.title(food_body.getTitle())
					.snippet(food_body.getTitle()+"\r\n"+food_body.getSubtitle())
					.flat(true);
			mMap.addMarker(options);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
			mMap.setMyLocationEnabled(true);
			Toast.makeText(FoodDetailActivity.this, "地理信息获取失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void sendLcation(FoodDetailBody food) {
		food_body = food;
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// getMenuInf later().inflate(R.menu.search, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		return super.onOptionsItemSelected(item);
//	}

//	public void onClick(View v){
//		switch (v.getId()) {
//		case R.id.actionbar_food_detail_back:
//			finish();
//			break;
//		case R.id.btn_food_detail_flag:
//			
//			break;
//		case R.id.btn_food_detail_phone:
//			break;
//		default:
//			break;
//		}
//	}

}
