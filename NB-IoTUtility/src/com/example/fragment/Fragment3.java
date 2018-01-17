package com.example.fragment;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.nb_iotutility.MyImageView;
import com.example.nb_iotutility.R;

public class Fragment3 extends Fragment implements OnClickListener{
	private View curView;
	SoundPool sp;
	HashMap<Integer, Integer> spMap;
	private MyImageView miv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		curView = inflater.inflate(R.layout.fragment3, null);
		ImageButton imgBtn = (ImageButton) curView.findViewById(R.id.img_btn_scan);
		imgBtn.setOnClickListener((OnClickListener) this);
		InitSound();
		
		  miv = (MyImageView) curView.findViewById(R.id.myImgView01);
		return curView;
	}	
	
	public void InitSound(){
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		spMap = new HashMap<Integer, Integer>();
		spMap.put(1,sp.load(this.getActivity(), R.raw.di_btn, 1));
		spMap.put(2,sp.load(this.getActivity(), R.raw.reminder,1));
	}
	public void playSound(int sound, int number) {
		AudioManager am = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);
	float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	float volumnRatio = volumnCurrent / audioMaxVolumn;
	sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number,  1f);
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		playSound(2,0);
		miv.play();
	}
		
}