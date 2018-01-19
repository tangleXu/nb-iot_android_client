package com.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nb_iotutility.R;

public class Fragment2 extends Fragment implements OnClickListener{
	private View curView;
	private Button mBtnPWR,mBtnEMM,mBtnESM;
	private Class<Fragment3> f3=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		curView = inflater.inflate(R.layout.fragment2, null);
		
		mBtnPWR = (Button) curView.findViewById(R.id.btnPWR);
		mBtnEMM = (Button) curView.findViewById(R.id.btnEMM);
		mBtnESM = (Button) curView.findViewById(R.id.btnESM);
		
		mBtnPWR.setOnClickListener(this);
		mBtnEMM.setOnClickListener(this);
		mBtnESM.setOnClickListener(this);
		
		return curView;
	}

	public void onClick(View arg0) {
		
		if(f3 != null)
		{
			switch(arg0.getId())
			{
			case R.id.btnPWR:
				
				break;
			case R.id.btnEMM:
				
				break;
			case R.id.btnESM:
				
				break;
			default:
				
				break;
			
			}
		}else{
			//f3=Fragment3.class. 
		}
	}	
}