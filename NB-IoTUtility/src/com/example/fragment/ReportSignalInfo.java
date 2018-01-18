/**
 * 
 */
package com.example.fragment;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.nb_iotutility.MainActivity;

import android.content.Context;
import android.os.Message;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class ReportSignalInfo {
	private String m_vpsUrl="http://58.247.178.229:8088/cgi-bin/report_nbinfo.py";
	private MainActivity ma;
	public String mStrMCC,mStrMNC,mStrTAC,mStrEARFCN,mStrGCELLID,mStrCAT,mStrSINR,mStrPCI,mStrRSRP,mStrRSRQ,mStrRSSI,mStrBAND;
	private String mDevName,mDevAddr,mLot,mLat,mIP,mAttach;
	
	public ReportSignalInfo(String sURL){
		m_vpsUrl = sURL;
		clsData();
	}
	
	public ReportSignalInfo(){
		clsData();
	}
	
	public ReportSignalInfo(Context mainActivity) {
		ma = (MainActivity) mainActivity;
		clsData();
	}
	public void clsData(){
		 mStrBAND=mStrMCC=mStrMNC=mStrTAC=mStrEARFCN=mStrGCELLID=mStrCAT=mStrSINR=mStrPCI=mStrRSRP=mStrRSRQ=mStrRSSI="";
		 mLot=mLat=mIP=mAttach=mDevAddr=mDevName="";
	}
	public void setDevName(String devName, String devAddr, String lot,String lat)
	{
		mDevName = devName;
		mDevAddr = devAddr;
		mLot = lot;
		mLat = lat;
	}
	public void setEPS(String eIP, String eAttach)
	{
		mIP = eIP;
		mAttach = eAttach;
	}
	public void setPHY(String band,String earfcn,String cat,String pci){
		mStrBAND = band;
		mStrEARFCN = earfcn;
		mStrCAT = cat;
		mStrPCI = pci;
	}
	public void setNetworkInfo(String mcc,String mnc,String tac,String cellid, String sinr,String rsrp,String rsrq,String rssi){
		mStrMCC = mcc;
		mStrMNC = mnc;
		mStrTAC = tac;
		mStrGCELLID = cellid;
		mStrSINR=sinr;
		mStrRSRP = rsrp;
		mStrRSRQ = rsrq;
		mStrRSSI = rssi;
	}
	public void sendRequestWithHttpClient()
	{
        new Thread(new Runnable() {
            
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
             //   if(mDevName!="")
             //   {
             //   	m_vpsUrl+="&dev="+mDevName;
             //   	Log.e("ARIC",m_vpsUrl);
             //   }
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpPost httpGet = new HttpPost(m_vpsUrl);
                ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();  
                if (mDevName != null) {  
                        pairs.add(new BasicNameValuePair("dev", mDevName));  
                        pairs.add(new BasicNameValuePair("dev_addr", mDevAddr));
                        pairs.add(new BasicNameValuePair("lot", mLot));
                        pairs.add(new BasicNameValuePair("lat", mLat));
                        pairs.add(new BasicNameValuePair("ip", mIP));
                        pairs.add(new BasicNameValuePair("attach", mAttach));
                        //mStrMCC,mStrMNC,mStrTAC,mStrEARFCN,mStrGCELLID,mStrCAT,mStrSINR,mStrPCI,mStrRSRP,mStrRSRQ,mStrRSSI,mStrBAND;
                        pairs.add(new BasicNameValuePair("fcn", mStrEARFCN));
                        pairs.add(new BasicNameValuePair("gid", mStrGCELLID));
                        pairs.add(new BasicNameValuePair("ta", mStrTAC));
                        pairs.add(new BasicNameValuePair("mcc", mStrMCC));
                        pairs.add(new BasicNameValuePair("mnc", mStrMNC));
                        pairs.add(new BasicNameValuePair("sinr", mStrSINR));
                        pairs.add(new BasicNameValuePair("cat", mStrCAT));
                        pairs.add(new BasicNameValuePair("band", mStrBAND));
                        pairs.add(new BasicNameValuePair("pci", mStrPCI));
                        pairs.add(new BasicNameValuePair("rsrp", mStrRSRP));
                        pairs.add(new BasicNameValuePair("rsrq", mStrRSRQ));
                        pairs.add(new BasicNameValuePair("rssi", mStrRSSI));
                        
                } 
               try {
            	    httpGet.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = ma.SHOW_RESPONSE;
                        message.obj = response.toString();
                        ma.handler.sendMessage(message);
                    }
                    
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
                
            }
        }).start();//这个start()方法不要忘记了       
	}
}
