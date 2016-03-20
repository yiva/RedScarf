package com.redscarf.weidou.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;





import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.redscarf.weidou.util.GlobalApplication;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
//		this.setActionBarLayout(R.layout.actionbar_map);
		datas = this.getIntent().getExtras();
		GlobalApplication.getInstance().addActivity(this);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		Geocoder geoCoder = new Geocoder(this,Locale.getDefault());
		List<Address> addr = new ArrayList<Address>();
		try {
			String location = "";
			location = datas.getString("location");
			addr = geoCoder.getFromLocationName(location, 5);
			lng = new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude());
			if (addr.size() > 0) {

				MarkerOptions options = new MarkerOptions().position(lng)
//        				.title("RedScarf")
//        				.snippet("Population: 4,137,400")
						.flat(true);
				map.addMarker(options);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
				map.setMyLocationEnabled(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LatLng lng = new LatLng(0, 0);
			MarkerOptions options = new MarkerOptions().position(lng)
//    				.title("RedScarf")
//    				.snippet("Population: 4,137,400")
					.flat(true);
			map.addMarker(options);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 16));
			map.setMyLocationEnabled(true);
			Toast.makeText(MapActivity.this, "地理信息获取失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void initView(){

	}


}
