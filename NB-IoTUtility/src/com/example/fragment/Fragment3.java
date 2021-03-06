package com.example.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.ReportSignalInfo;
import com.example.nb_iotutility.MyImageView;
import com.example.nb_iotutility.R;

public class Fragment3 extends Fragment implements OnClickListener{
	private View curView;
	SoundPool sp;
	HashMap<Integer, Integer> spMap;
	private MyImageView miv;
	private static final int REQUEST_ENABLE_BT = 0;
	private static final int NET_READ_TIMEOUT_MILLIS = 0;
	private static final int NET_CONNECT_TIMEOUT_FILLIS = 0;
	protected static final int UPDATE_SIGNAL_INFO = 1;
	
	private  BluetoothSocket mmSocket;
	private BluetoothAdapter mBluetoothAdapter;
	public String mmStrCmd;
	public ReportSignalInfo rsi;
	public String mStrMCC,mStrMNC,mStrTAC,mStrEARFCN,mStrGCELLID,mStrCAT,mStrSINR,mStrPCI,mStrRSRP,mStrRSRQ,mStrRSSI,mStrBAND;
    private TextView mtvMCC,mtvMNC,mtvEARFCN,mtvGCELLID,mtvTAC,mtvCAT,mtvSINR,mtvBIND,mtvPCI,mtvRSRP,mtvRSRQ,mtvRSSI;
    public Handler f3_handler;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		curView = inflater.inflate(R.layout.fragment3, null);
		
        mmStrCmd = "";
        mStrBAND=mStrMCC=mStrMNC=mStrTAC=mStrEARFCN=mStrGCELLID=mStrCAT=mStrSINR=mStrPCI=mStrRSRP=mStrRSRQ=mStrRSSI="";

		ImageButton imgBtn = (ImageButton) curView.findViewById(R.id.img_btn_scan);
		imgBtn.setOnClickListener((OnClickListener) this);
		InitSound();
		InitBluetooth();
		InitInfoView();
		
		miv = (MyImageView) curView.findViewById(R.id.myImgView01);
		rsi = new ReportSignalInfo(this.getActivity());

		f3_handler = new Handler() {

    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			switch(msg.what){
    			case UPDATE_SIGNAL_INFO:
    				//String response = (String)msg.obj;
    				//mtvMCC,mtvMNC,mtvEARFCN,mtvGCELLID,mtvTAC,mtvCAT,mtvSINR,mtvBIND,mtvPCI,mtvRSRP,mtvRSRQ,mtvRSSI;
    				mtvMCC.setText(mStrMCC);
    				mtvMNC.setText(mStrMNC);
    				mtvEARFCN.setText(mStrEARFCN);
    				mtvGCELLID.setText(mStrGCELLID);
    				mtvTAC.setText(mStrTAC);
    				mtvCAT.setText(mStrCAT);
    				mtvSINR.setText(mStrSINR);
    				mtvBIND.setText(mStrBAND);
    				mtvPCI.setText(mStrPCI);
    				mtvRSRP.setText(mStrRSRP);
    				mtvRSRQ.setText(mStrRSRQ);
    				mtvRSSI.setText(mStrRSSI);
    				
    				break;
    			default:
    				break;
    				
    			}
    		}
    	};
    	
		return curView;
	}
	public void InitInfoView(){
		//mtvMCC,mtvMNC,mtvEARFCN,mtvGCELLID,mtvTAC,mtvCAT,mtvSINR,mtvBIND,mtvPCI,mtvRSRP,mtvRSRQ,mtvRSSI;
		mtvMCC = (TextView) curView.findViewById(R.id.tvMCC);
		mtvMNC = (TextView) curView.findViewById(R.id.tvMNC);
		mtvEARFCN = (TextView) curView.findViewById(R.id.tvEARFCN);
		mtvGCELLID = (TextView) curView.findViewById(R.id.tvGCELLID);
		mtvTAC = (TextView) curView.findViewById(R.id.tvTAC);
		mtvCAT = (TextView) curView.findViewById(R.id.tvCAT);
		mtvSINR = (TextView) curView.findViewById(R.id.tvSINR);
		mtvBIND = (TextView) curView.findViewById(R.id.tvBIND);
		mtvRSRP = (TextView) curView.findViewById(R.id.tvRSRP);
		mtvRSRQ = (TextView) curView.findViewById(R.id.tvRSRQ);
		mtvRSSI = (TextView) curView.findViewById(R.id.tvRSSI);
		mtvPCI = (TextView) curView.findViewById(R.id.tvPCI);
	}
	public void InitBluetooth(){
        ///BT TEST
        
        Log.d("ARIC","onCreate:Begin get default adapter.");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if(mBluetoothAdapter == null){
        	//Device not support bluetooth.
        	Log.d("ARIC","Device not support bluetooth.");
        }else{
        	Log.d("ARIC","Device support bluetooth.");
        }
        
        if(!mBluetoothAdapter.isEnabled()){
        	Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        	startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        
		//Query paired devices.
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		 // If there are paired devices
		 if (pairedDevices.size() > 0) {
		     // Loop through paired devices
			 for (BluetoothDevice device : pairedDevices) {
			     // Add the name and address to an array adapter to show in a ListView
			 //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			 Log.d("ARIC", device.getName() + "==>> " + device.getAddress());
			 if(device.getName().equalsIgnoreCase("TM-770")){ //("HUAWEI U8818")){
				  //ConnectThread connectBtThread = new ConnectThread(device);
				  //connectBtThread.start();
				  Log.e("ARIC", "Found HUAWEI U8818");
			    	 }
			     }
		 }
		 //10:C6:1F:57:2F:F1
		 //GUID:24c71ae4-27e4-4194-b6b1-1fb27f962887
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:06:69:00:1D:A6");//("10:C6:1F:57:2F:F1");
		ConnectThread connectBtThread = new ConnectThread(device);
		connectBtThread.start();		
	}
	
	private class RecvThread extends Thread {
		private int count;
		public RecvThread(){
			count =0;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			InputStream in = null;
			try {
				in = mmSocket.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(mmSocket.isConnected()){
				byte[] buffer = new byte[1024];					
				int temp=0;
				
				if(count == 0){
					try {
						count = in.available();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if( count !=0 ){
					byte[] bt = new byte[count];
					int readCount = 0;
					while(readCount < count){
						try {
							readCount += in.read(bt, readCount, count-readCount);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					String xx = new String(bt);
					Log.d("ARIC", xx);
					////Parse Information.
					String ret;
					ret = ParseLineInfo(xx, ".*(EARFCN:.+)GCELLID");
					if(ret.indexOf("EARFCN")>=0)
						mStrEARFCN = ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(GCELLID:.+)TAC");
					if(ret.indexOf("GCELLID")>=0)
						mStrGCELLID = ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(TAC:.+)MCC");
					if(ret.indexOf("TAC")>=0)
						mStrTAC =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(MCC:.+)MNC");
					if(ret.indexOf("MCC")>=0)
						mStrMCC =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(MNC:.+)DLBW");
					if(ret.indexOf("MNC")>=0)
						mStrMNC =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(SINR:.+)CAT");
					if(ret.indexOf("SINR")>=0)
						mStrSINR =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(CAT:.+)BAND");
					if(ret.indexOf("CAT")>=0)
						mStrCAT =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(BAND:.+)PCI");
					if(ret.indexOf("BAND")>=0)
						mStrBAND =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(PCI:.+)RSRP");
					if(ret.indexOf("PCI")>=0)
						mStrPCI =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(RSRP:.+)RSRQ");
					if(ret.indexOf("RSRP")>=0)
						mStrRSRP =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(RSRQ:.+)RSSI");
					if(ret.indexOf("RSRQ")>=0)
						mStrRSRQ =  ret;
					Log.e("ARIC","RECV:"+ret);
					ret = ParseLineInfo(xx, ".*(RSSI:.+)[\r|\n| ]");
					if(ret.indexOf("RSSI")>=0)
						mStrRSSI =  ret;
					Log.e("ARIC","RECV:"+ret);
					//mtvMCC,mtvMNC,mtvEARFCN,mtvGCELLID,mtvTAC,mtvCAT,mtvSINR,mtvBIND,mtvPCI,mtvRSRP,mtvRSRQ,mtvRSSI;
					 Message message = new Message();
                     message.what = UPDATE_SIGNAL_INFO;
                     message.obj = "Update signal";
                     f3_handler.sendMessage(message);
			        
					playSound(2,0);
					count =0;
				}
				
				//byte[] ttmp = new byte[temp];
				//System.arraycopy(buffer,0,ttmp,0,temp);
				//String ssRecv = new String(ttmp);
				//Log.d("ARIC",ssRecv);
				
				try {
					Thread.sleep(200,10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
private class ConnectThread extends Thread{
		
		private  BluetoothDevice mmDevice;
		private int count,cxt;
		
		public ConnectThread(BluetoothDevice device)
		{
			//Use a temporary object that is later assigned.
			
			BluetoothSocket tmp = null;
			mmDevice = device;
			count=0;
			cxt = 0;
			
			try {
				tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")); //24c71ae4-27e4-4194-b6b1-1fb27f962887"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mmSocket = tmp;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			try {
				mmSocket.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					mmSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			new RecvThread().start();
			
			OutputStream os = null;
			//InputStream in = null;
			
			try {
				 os = mmSocket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			while(true)
			{
				if(mmStrCmd !="")
				{
					byte[] data_tx=mmStrCmd.getBytes();
					try {
						os.write(data_tx);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//mmTVInfo.setText("Finished CMD:"+mmStrCmd);
					Log.d("ARIC","Finished CMD:"+mmStrCmd);
					//Toast.makeText(getApplicationContext(), "Finished CMD:"+mmStrCmd, Toast.LENGTH_LONG).show();
					mmStrCmd="";
				}
					
				try {
					Thread.sleep(300,30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(cxt > 200)
				{
				
			        
					cxt = 0;
				}
			}
			
		}
		
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
		
		mmStrCmd="INDEX\r\n";
		
		Toast.makeText(this.getActivity().getApplicationContext(), "Send scan signal!", Toast.LENGTH_LONG).show();
        rsi.setDevName("NB-PoC001","ERICSSON SH WILD-C","31.2364733460","121.3627737870");
        rsi.setNetworkInfo(mStrMCC,mStrMNC, mStrTAC, mStrGCELLID, mStrSINR, mStrRSRP, mStrRSRQ, mStrRSSI);
        rsi.setPHY(mStrBAND, mStrEARFCN, mStrCAT, mStrPCI);
        rsi.setEPS("0.0.0.0", "Pending");		
        rsi.sendRequestWithHttpClient();
		miv.play();
	}
	public String ParseLineInfo(String line, String regex_str) {
		// TODO Auto-generated method stub
		
		//Parse EARFDN
		Pattern p = Pattern.compile(regex_str);
		Matcher m = p.matcher(line);
		
		boolean rs = m.find();
		if(rs){
			return m.group(1)+"";
		}
		
		//for(int i=1;i<=m.groupCount();i++){
		//	Log.e("ARIC","RECV:"+m.group(i));
		//}
		return "NA";
		
	}		
}