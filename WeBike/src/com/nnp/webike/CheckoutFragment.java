package com.nnp.webike;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutFragment extends Fragment implements View.OnClickListener {
	String combo;
	
	public CheckoutFragment(Bike sb) {
		combo = sb.getLock();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkout_fragment, container,
				false);
		((TextView) view.findViewById(R.id.checkout_lock)).setText(combo);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Button okBtn = (Button) view.findViewById(R.id.checkout_ok);
		okBtn.setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkout_ok:
			getActivity().getSupportFragmentManager().popBackStack();
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_window, new MenuFragment()).addToBackStack(null).commit();
			break;
		}

	}
}
