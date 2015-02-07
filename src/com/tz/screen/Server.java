package com.tz.screen;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import sun.java2d.ScreenUpdateManager;

/**
 * 
 * 远程监控服务器端 教师端
 * @author Sebastian
 *
 */
public class Server {
	public static void main(String[] args) {
		try {
			//建立服务监听 10000
			ServerSocket ss = new ServerSocket(10000);
			
			while(true){
				System.out.println("等待连接中。。。。");
				Socket client = ss.accept();
				System.out.println("连接成功");
			
				//通过socket获取输出流，转换成输出流
				OutputStream os = client.getOutputStream();
				//转换成二进制数据流
				DataOutputStream dos = new DataOutputStream(os);
				//
				ScreenThread screenThread = new ScreenThread(dos);
				//启动线程
				screenThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("请求失败！");
		}
	}
}


class ScreenThread extends Thread {
	//数据输出流
	private DataOutputStream dataOut;

	public ScreenThread(DataOutputStream dataOut) {
		super();
		this.dataOut = dataOut;
	}
	
	//启动线程屏幕开始传输
	public void run(){
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		
		try {
			//获取教师端分辨率发送到学生端
			dataOut.writeDouble(dm.getHeight());
			dataOut.writeDouble(dm.getWidth());
			dataOut.flush();
			
			//矩形区域大小
			
			Rectangle rec = new Rectangle(dm);
			Robot robot = new Robot();
			while(true){
				//通过机器人截取本地图片
				BufferedImage bufImg = robot.createScreenCapture(rec);
				//用二进制数据流来存储转换后的图片
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//压缩
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
				encoder.encode(bufImg);
				
				//将本地图片截取并抓换成二进制数组
				byte[] data = baos.toByteArray();
				//把数组的长度发送到客户端
				dataOut.writeInt(data.length);
				//先将
				dataOut.write(data);
				dataOut.flush();
				
				Thread.sleep(20);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
