package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpService {
    //接收手机端发送udp所用的端口号
    public static final int BROADCAST_PORT = 10006; 
    //发送响应udp包给手机端所用的端口号
    public static final int BROADCAST_RES_PORT = 10007;
    //用于记录手机端的ip
    public static String mobileIP;
    public static void main(String[] args) {
        revicedByMobileAndRespond();
    }
    public static void revicedByMobileAndRespond(){ 
        try{

        	// 获取手机发送到局域网的广播
            DatagramSocket server = new DatagramSocket(BROADCAST_PORT);
            byte[] recvBuf = new byte[100];
            DatagramPacket recvPacket
                = new DatagramPacket(recvBuf, recvBuf.length);
            server.receive(recvPacket);
            InetAddress addr = recvPacket.getAddress();
            mobileIP = addr.toString();
            System.out.println(mobileIP);

            // 利用前面获取到的广播，获取手机IP，并发送udp包到手机，这样手机就知道PC机的IP。
            byte[] sendBuf;
            sendBuf = mobileIP.getBytes();
            DatagramPacket sendPacket 
                = new DatagramPacket(sendBuf, sendBuf.length, addr,BROADCAST_RES_PORT);
            server.send(sendPacket);
            server.close(); 

        }catch(Exception e){       
            e.printStackTrace();
        }
    }
}
