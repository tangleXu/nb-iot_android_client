package com.example.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.example.nb_iotutility.R;

@SuppressLint("SetJavaScriptEnabled") public class Fragment4 extends Fragment{
	
	private View cur_view;
	private WebView myWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		 cur_view = inflater.inflate(R.layout.fragment4, null);
		 
		myWebView = (WebView)cur_view.findViewById(R.id.webView1);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		myWebView.loadUrl("http://58.247.178.229:8088/b_map.html");
		 
		 return cur_view;
	}	
}