package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpService {
	//接收手机端发送udp所用的端口号
	public static final int BROADCAST_PORT = 10006; 
	//发送响应udp包给手机端所用的端口号
	public static final int BROADCAST_RES_PORT = 10007;
	//接受手机端触点数据所用的端口号
	public static final int MESSAGE_PORT = 5000; 
	//用于记录手机端的ip
	public static String mobileIP;
	public static void main(String[] args) {
		revicedByMobileAndRespond();
	}
	public static void revicedByMobileAndRespond(){ 
		try{
			DatagramSocket server = new DatagramSocket(BROADCAST_PORT);
	        byte[] recvBuf = new byte[100];
	        DatagramPacket recvPacket
	            = new DatagramPacket(recvBuf,recvBuf.length);
	        server.receive(recvPacket);
	        InetAddress addr = recvPacket.getAddress();
//	        int port = recvPacket.getPort();
	        mobileIP = addr.toString();
	        System.out.println(mobileIP);
	        //得到手机的ip 根据手机的ip把数据返回
	        byte[] sendBuf;
	        sendBuf = mobileIP.getBytes();
	        DatagramPacket sendPacket 
	            = new DatagramPacket(sendBuf,sendBuf.length,addr,BROADCAST_RES_PORT);
	        server.send(sendPacket);
	        server.close(); 
	        System.out.println("----------");
	        reciveUpdData();
	        System.out.println("----------");
		}catch(Exception e){       
			e.printStackTrace();
		}
	}
	public static void reciveUpdData(){
		while (true) {
			try {
				DatagramSocket server = new DatagramSocket(MESSAGE_PORT);
		        byte[] recvBuf = new byte[100];
		        DatagramPacket recvPacket
		            = new DatagramPacket(recvBuf,recvBuf.length);
		        server.receive(recvPacket);
		        String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
		        System.out.println(recvStr);
		        server.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
