package com.example.bt01;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private static final int REQUEST_ENABLE_BT = 0;
	private static final int NET_READ_TIMEOUT_MILLIS = 0;
	private static final int NET_CONNECT_TIMEOUT_FILLIS = 0;
	private  BluetoothSocket mmSocket;
	private BluetoothAdapter mBluetoothAdapter;
	public String mmStrCmd;
	
	public String mStrMCC,mStrMNC,mStrTAC,mStrEARFCN,mStrGCELLID,mStrCAT,mStrSINR,mStrPCI,mStrRSRP,mStrRSRQ,mStrRSSI,mStrBAND;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///BT TEST
        mmStrCmd = "";
        mStrBAND=mStrMCC=mStrMNC=mStrTAC=mStrEARFCN=mStrGCELLID=mStrCAT=mStrSINR=mStrPCI=mStrRSRP=mStrRSRQ=mStrRSSI="";
        
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
		
		
		Button btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.editText1);
				mmStrCmd=et.getText().toString() + "\r\n";
				
				Toast.makeText(getApplicationContext(), "Send INDEX", Toast.LENGTH_LONG).show();
			}
			
		});
		
		Button btn2 = (Button) findViewById(R.id.btnReport);
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					try {
						ReportData();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e("ARIC","Report data failed.");
						e.printStackTrace();
					}

			}});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
					mStrEARFCN = ret = ParseLineInfo(xx, ".*(EARFCN:.+)GCELLID");
					
					Log.e("ARIC","RECV:"+ret);
					mStrGCELLID = ret = ParseLineInfo(xx, ".*(GCELLID:.+)TAC");
					Log.e("ARIC","RECV:"+ret);
					mStrMCC = ret = ParseLineInfo(xx, ".*(TAC:.+)MCC");
					Log.e("ARIC","RECV:"+ret);
					mStrMCC = ret = ParseLineInfo(xx, ".*(MCC:.+)MNC");
					Log.e("ARIC","RECV:"+ret);
					mStrMNC = ret = ParseLineInfo(xx, ".*(MNC:.+)DLBW");
					Log.e("ARIC","RECV:"+ret);
					mStrSINR = ret = ParseLineInfo(xx, ".*(SINR:.+)CAT");
					Log.e("ARIC","RECV:"+ret);
					mStrCAT = ret = ParseLineInfo(xx, ".*(CAT:.+)BAND");
					Log.e("ARIC","RECV:"+ret);
					mStrBAND = ret = ParseLineInfo(xx, ".*(BAND:.+)PCI");
					Log.e("ARIC","RECV:"+ret);
					mStrPCI = ret = ParseLineInfo(xx, ".*(PCI:.+)RSRP");
					Log.e("ARIC","RECV:"+ret);
					mStrRSRP = ret = ParseLineInfo(xx, ".*(RSRP:.+)RSRQ");
					Log.e("ARIC","RECV:"+ret);
					mStrRSSI = ret = ParseLineInfo(xx, ".*(RSSI:.+)[\r|\n| ]");
					Log.e("ARIC","RECV:"+ret);

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
		private int count;
		
		public ConnectThread(BluetoothDevice device)
		{
			//Use a temporary object that is later assigned.
			
			BluetoothSocket tmp = null;
			mmDevice = device;
			count=0;
			
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
			
/*			try {
				in = mmSocket.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
*/			
			
			
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
			}
/*
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
//			try {
//				mmSocket.close();
//			} catch (IOException e) {
// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		
	}

	public void ReportData() throws Exception 
	{
/*
		String strURL="http://baidu.com";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		URI ws = null;
		try {
			ws = new URI(strURL);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		request.setURI(ws);
		
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("ARIC","Response code:" + response.getStatusLine().getStatusCode());
		
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line ="";
		try {
			while((line = rd.readLine())!=null){
				Log.d("ARIC", line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	/*URL url = new URL("http://baidu.com");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
	conn.setConnectTimeout(NET_CONNECT_TIMEOUT_FILLIS);
	conn.setRequestMethod("GET");
	conn.setDoInput(true);
	conn.connect();
	Log.e("ARIC",""+conn.getInputStream());
	Toast.makeText(getApplicationContext(), "ARIC"+conn.getInputStream(), Toast.LENGTH_LONG).show();*/
//    String name="Wang";
//    String age="21";
    
    StringBuilder buf = new StringBuilder("http://baidu.com");  
//    buf.append("?");  
//    buf.append("name="+URLEncoder.encode(name.getText().toString(),"UTF-8")+"&");  
//    buf.append("age="+URLEncoder.encode(age.getText().toString(),"UTF-8"));  
    URL url = new URL(buf.toString());
    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode()==200){  
            Toast.makeText(MainActivity.this, "GET提交成功", Toast.LENGTH_SHORT).show();  
        }  
        else Toast.makeText(MainActivity.this, "GET提交失败", Toast.LENGTH_SHORT).show();  
        
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
