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

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LeaveFragment extends Fragment implements View.OnClickListener{
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leave_fragment, container, false);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		Button okBtn = (Button)view.findViewById(R.id.leave_ok);
		Button cancelBtn = (Button)view.findViewById(R.id.leave_cancel);
		
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		((TextView)view.findViewById(R.id.leave_lock)).setText(getActivity().getPreferences(Context.MODE_PRIVATE).getString("combo", "0000"));
		
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	class LeaveTask extends AsyncTask<URL, Void, Void> {

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
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.leave_ok:
			Location l = ((MainActivity)getActivity()).getLocation();
			try {
				URL url = new URL("http://ec2-54-68-186-161.us-west-2.compute.amazonaws.com/leave.php?ID=" + getActivity().getPreferences(Context.MODE_PRIVATE).getInt("id", 0) + "&locationx=" + l.getLatitude() + "&locationy=" + l.getLongitude());
				new LeaveTask().execute(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean("checkedout", false).apply();
			getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt("id", 0).apply();
			getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("combo", "0000").apply();
			break;
		case R.id.leave_cancel:
			
			break;
		}
		
		getActivity().getSupportFragmentManager().popBackStack();
		
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new MenuFragment()).addToBackStack(null).commit();
		
	}
}
