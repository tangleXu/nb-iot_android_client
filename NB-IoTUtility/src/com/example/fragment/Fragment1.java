package com.example.fragment;

import java.io.IOException;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.nb_iotutility.R;

public class Fragment1 extends Fragment{
    private View view1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view1 = inflater.inflate(R.layout.fragment1, null);

		
		return view1;
	}	
}
