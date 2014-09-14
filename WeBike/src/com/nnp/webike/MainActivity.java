package com.nnp.webike;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends FragmentActivity implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	MenuFragment menuFragment;	
	LocationManager mLocationManager;
	//Location location;
	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
    boolean mUpdatesRequested;

	
	public MenuFragment getMenuFragment() {
		return menuFragment;
	}
	
	public Location getLocation(){
		return mCurrentLocation;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		menuFragment = new MenuFragment();
		
		FragmentManager mFragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction().replace(
				R.id.frag_window, menuFragment);
		ft.commit();

		//mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//mLocationManager.requestLocationUpdates(
			//	LocationManager.NETWORK_PROVIDER, 0, 0, this);
		mLocationClient = new LocationClient(this, this, this);
		mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        
        Intent intent = getIntent();
		Data.email = intent.getStringExtra("email_id");

	}
	
	@Override
	public void onBackPressed() {
		for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++){
			getFragmentManager().popBackStack();
		}
		
		FragmentManager mFragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction().replace(
				R.id.frag_window, menuFragment);
		ft.commit();
	}
	
	@Override
	protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
	
	@Override
	protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
	public LocationManager getLocationManager() {
		return mLocationManager;
	}

	@Override
	public void onLocationChanged(Location location) {
		this.mCurrentLocation = location;

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        9000);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showDialog(connectionResult.getErrorCode());
        }

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		 mLocationClient.requestLocationUpdates(mLocationRequest, this);
		 mCurrentLocation = mLocationClient.getLastLocation();
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
