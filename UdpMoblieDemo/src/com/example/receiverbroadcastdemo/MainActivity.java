package com.example.receiverbroadcastdemo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int BROADCAST_PORT = 10006; 
	public static final int BROADCAST_RES_PORT = 10007;
	public static final int MESSAGE_PORT = 5000;
	public String ServiceIP;
	public boolean isRecived = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(!checkNetworkAvailable(this.getApplicationContext())){
        	Toast.makeText(getApplicationContext(), "网络无法连接，请检查网络设置", Toast.LENGTH_SHORT).show();
		}
		
		new Thread(){
			public void run() {
				sendUdpBroadCastPackage();
			};
		}.start();
		new Thread(){
			public void run() {
				revicedMobileResponse();
			};
		}.start();
	}
	
	public void sendUdpBroadCastPackage(){
		//一直发广播
		while(!isRecived){
			byte[] b;
			try{
				Thread.sleep(3000);
				b="service".getBytes();
				DatagramSocket client = new DatagramSocket();
		        InetAddress addr = InetAddress.getByName("255.255.255.255");
		        DatagramPacket sendPacket
		            = new DatagramPacket(b ,b.length,addr, BROADCAST_PORT);
		        client.send(sendPacket);
		        Log.e("MainActivity",InetAddress.getLocalHost().getHostAddress());
		        client.close();      
			}catch(Exception e){       
				e.printStackTrace();
			}
		}
		//isRecived 为 true 说明收到广播 启动reciveDataUpd（）
		Log.e("MainActivity","----------------------");
		Log.e("MainActivity","revicedMobileResponsed");
		Log.e("MainActivity","----------------------");
	}	
	public void revicedMobileResponse(){ 
		try{
			DatagramSocket server = new DatagramSocket(BROADCAST_RES_PORT);
	        byte[] recvBuf = new byte[100];
	        DatagramPacket recvPacket
	            = new DatagramPacket(recvBuf,recvBuf.length);
	        server.receive(recvPacket);
	        InetAddress addr = recvPacket.getAddress();
	        ServiceIP = addr.toString();
	        isRecived = true;
	        System.out.println(ServiceIP);
	        server.close();
		}catch(Exception e){       
			e.printStackTrace();
		}
	}
	public boolean checkNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
