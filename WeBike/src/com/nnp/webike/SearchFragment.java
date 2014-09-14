package com.nnp.webike;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SearchFragment extends Fragment implements OnClickListener {
	MiniMapFragment mapFrag;
	GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMap();
	}

	private void initMap() {
		if (map == null) {
			mapFrag = new MiniMapFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.map_window, mapFrag).commit();

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.search_fragment, container, false);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Button checkoutBtn = (Button) getView().findViewById(
				R.id.search_checkout);
		checkoutBtn.setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	class CheckoutTask extends AsyncTask<URL, Void, Void> {

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

			try {
				JSONObject jObject = new JSONObject(stringBuilder.toString());
				mapFrag.getSelectedBike().setLock(jObject.getString("combo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mapFrag.getSelectedBike().avail = false;
			getActivity().getPreferences(Context.MODE_PRIVATE).edit()
					.putBoolean("checkedout", true).apply();
			getActivity().getPreferences(Context.MODE_PRIVATE).edit()
					.putInt("id", mapFrag.getSelectedBike().getID()).apply();
			getActivity().getPreferences(Context.MODE_PRIVATE).edit()
					.putString("combo", mapFrag.getSelectedBike().getLock())
					.apply();
			getActivity().getFragmentManager().popBackStack();
			getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.frag_window,
							new CheckoutFragment(mapFrag.getSelectedBike()))
					.commit();
		}
	}

	@Override
	public void onClick(View v) {
		if (!Data.find) {
			switch (v.getId()) {
			case R.id.search_checkout:
				Location l = ((MainActivity) getActivity()).getLocation();
				Log.i("webike", l.getLatitude() + " " + l.getLongitude());
				if (Math.abs(l.getLatitude()
						- mapFrag.getSelectedBike().getLat()) < 0.00035
						&& Math.abs(l.getLongitude()
								- mapFrag.getSelectedBike().getLng()) < 0.00035) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setTitle("Checkout");
					builder.setMessage("Are you sure?");

					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									URL url;
									try {
										url = new URL(
												"http://ec2-54-68-186-161.us-west-2.compute.amazonaws.com/checkout.php?"
														+ "user="
														+ Data.email
														+ "&ID="
														+ mapFrag
																.getSelectedBike()
																.getID());
										new CheckoutTask().execute(url);
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									dialog.dismiss();

									// http checkout procedure
								}

							});

					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing
									dialog.dismiss();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("You are too far from this bike.")
							.setCancelable(false);

					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();
				}
				break;
			}

		}

	}
}
