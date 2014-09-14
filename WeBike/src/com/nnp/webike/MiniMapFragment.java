package com.nnp.webike;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MiniMapFragment extends SupportMapFragment implements
		GoogleMap.OnMarkerClickListener, LocationListener {

	HashMap<Marker, Bike> markers;
	Bike selectedBike;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Location l = ((MainActivity) getActivity()).getLocation();
		float lat = (float) l.getLatitude();
		float lng = (float) l.getLongitude();
		try {
			URL url;
			if (Data.find) {
				url = new URL(
						"http://ec2-54-68-186-161.us-west-2.compute.amazonaws.com/findMyBike.php?user="
								+ Data.email);
			} else {
				url = new URL(
						"http://ec2-54-68-186-161.us-west-2.compute.amazonaws.com/mapdata.php?locationx="
								+ lat + "&locationy=" + lng + "&range=1000");
			}
			new SearchDB().execute(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onViewCreated(view, savedInstanceState);

	}

	class SearchDB extends AsyncTask<URL, Void, Void> {

		@Override
		protected Void doInBackground(URL... params) {
			HttpGet httpGet = new HttpGet(params[0].toString());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			StringBuilder stringBuilder = new StringBuilder();

			try {
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String q = stringBuilder.toString();
			Log.i("webike", q);
			try {
				JSONObject root = new JSONObject(q);
				JSONArray bikes = root.getJSONArray("bikesNearby");

				Log.i("webike", bikes.length() + "");
				for (int i = 0; i < bikes.length(); i++) {
					JSONObject cBike = bikes.getJSONObject(i);
					Bike b = new Bike(cBike.getDouble("locationx"),
							cBike.getDouble("locationy"), true,
							cBike.getString("description"), cBike.getInt("id"));
					if (Data.find) {
						b.setLock(b.getDescription() + " (combo: "
								+ cBike.getString("combo") + ")");
					}
					Data.bikelist.add(b);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			initMap();
			super.onPostExecute(result);
		}

	}

	Circle myLoc;
	Location l;

	public void initMap() {
		final GoogleMap map = getMap();

		map.setOnMarkerClickListener(this);
		map.clear();

		markers = new HashMap<Marker, Bike>();
		for (Bike b : Data.bikelist) {
			Marker m = map.addMarker(new MarkerOptions().position(
					new LatLng(b.getLat(), b.getLng())).title(b.getLock()));
			Log.i("webike", b.getLat() + " " + b.getLng());
			markers.put(m, b);
		}

		Location l = ((MainActivity) getActivity()).getLocation();

		map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition
				.builder()
				.target(new LatLng(l.getLatitude(), l.getLongitude())).zoom(15)
				.build()));

		map.setMyLocationEnabled(true);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (!Data.find)
			getActivity().findViewById(R.id.popup).setVisibility(View.VISIBLE);
		((TextView) (getActivity().findViewById(R.id.search_bikedesc)))
				.setText(markers.get(marker).getDescription());
		selectedBike = markers.get(marker);
		if (Data.find) {
			marker.showInfoWindow();
		}
		return false;
	}

	public Bike getSelectedBike() {
		return selectedBike;
	}

	@Override
	public void onLocationChanged(Location location) {
		l = location;

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
