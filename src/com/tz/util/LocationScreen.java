package com.tz.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.org.apache.commons.digester.rss.Image;


public class LocationScreen {
	public static void main(String[] args) {
		//构造初始化窗体
		JFrame jfram = new JFrame("人工智能本地监控");
		jfram.setSize(800, 600);
		jfram.setVisible(true);
		jfram.setAlwaysOnTop(true);
		//定义方法直接查询本地操作系统
		Toolkit tk = Toolkit.getDefaultToolkit();
		//获取屏幕大小
		Dimension dm = tk.getScreenSize();
		//图像显示区域
		JLabel imaLabel = new JLabel();
		jfram.add(imaLabel);
		
		//创建一个机器人
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//指定坐标空间的区域
			for(int i = 0; i < 1000; i++){
				//Rectangle rec = new Rectangle(jfram.getWidth(),0,(int)dm.getWidth(),(int)dm.getHeight());
				Rectangle rec = new Rectangle(0,0,(int)dm.getWidth(),(int)dm.getHeight());
				BufferedImage bufImg = robot.createScreenCapture(rec);
				
				imaLabel.setIcon(new ImageIcon(bufImg));
				
				Thread.sleep(5);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
