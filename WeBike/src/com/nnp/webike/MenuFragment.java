package com.nnp.webike;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuFragment extends Fragment implements View.OnClickListener{
	Button searchBtn, leaveBtn, registerBtn, findBikeBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		
		if(container == null){
			return null;
		}
		
		View view = inflater.inflate(R.layout.menu_fragment, container, false);
		
		searchBtn = (Button)(view.findViewById(R.id.bikes_search_btn));
		leaveBtn = (Button)(view.findViewById(R.id.bikes_leave_btn));
		registerBtn = (Button)(view.findViewById(R.id.register_bike_btn));
		findBikeBtn = (Button)(view.findViewById(R.id.find_bike_btn));
		
		((TextView)view.findViewById(R.id.menu_email)).setText(Data.email);
		
		searchBtn.setOnClickListener(this);
		leaveBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		findBikeBtn.setOnClickListener(this);
		
		if(getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean("checkedout", false)){
			searchBtn.setVisibility(View.GONE);
			leaveBtn.setVisibility(View.VISIBLE);
		} else {
			searchBtn.setVisibility(View.VISIBLE);
			leaveBtn.setVisibility(View.GONE);
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		Log.i("webike","click");
		switch(v.getId()){
		case R.id.bikes_search_btn:
			Log.i("webike", "search");
			Data.bikelist.clear();
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new SearchFragment()).addToBackStack(null).commit();
			Data.find = false;
			break;
		case R.id.bikes_leave_btn:
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new LeaveFragment()).addToBackStack(null).commit();
			Log.i("webike", "leave");
			break;
		case R.id.register_bike_btn:
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new RegisterFragment()).addToBackStack(null).commit();
			Log.i("webike", "register");
			break;
		case R.id.find_bike_btn:
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new SearchFragment()).addToBackStack(null).commit();
			Log.i("webike", "find");
			Data.find = true;
			break;
		}
		
		if(getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean("checkedout", false)){
			searchBtn.setVisibility(View.GONE);
			leaveBtn.setVisibility(View.VISIBLE);
		} else {
			searchBtn.setVisibility(View.VISIBLE);
			leaveBtn.setVisibility(View.GONE);
		}
		
	}

}
