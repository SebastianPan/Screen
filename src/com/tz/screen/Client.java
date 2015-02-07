package com.tz.screen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * 远程监控客户端工具类
 * @author Sebastian
 *
 */
public class Client {
	//java入口
	public static void main(String[] args) {
		//show ip 地址输入框
		String input = JOptionPane.showInputDialog("请输入要连接的服务器（包括端口号）:","127.0.0.1:10000");
		
		try {
			//获取服务器主机
			String host = input.substring(0, input.indexOf(":"));
			//获取端口号
			String post = input.substring(input.indexOf(":")+1);
			System.out.println(host + ":" + post);
				
			//连接服务器
			Socket client = new Socket(host, Integer.parseInt(post));
			DataInputStream dis = new DataInputStream(client.getInputStream());
			
			//创建面板
			JFrame jframe = new JFrame();
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			jframe.setTitle("人工智能远程监控系统 by Sebastian");
			jframe.setSize(1024, 768);
			
			//读取服务端分辨率
			double height = dis.readDouble();
			double width = dis.readDouble();
			
			Dimension dimensionServer = new Dimension((int)height, (int)width);
			
			//设置
			jframe.setSize(dimensionServer);
			//将服务端的图片作为背景
			JLabel backImage = new JLabel();
			JPanel panel = new JPanel();
			//需要滚动条
			JScrollPane scrollPane = new JScrollPane(panel);
			panel.setLayout(new FlowLayout());
			
			panel.add(backImage);
			jframe.add(scrollPane);
			jframe.setAlwaysOnTop(true);
			jframe.setVisible(true);
			
			while(true){
				int len = dis.readInt();
				byte[] imageData = new byte[len];
				dis.readFully(imageData);
				
				ImageIcon image = new ImageIcon(imageData);
				backImage.setIcon(image);
				//从新画制面板
				jframe.repaint();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
