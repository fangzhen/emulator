package org.fzhen.inv_pendulum2l;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel{
	private Image bg;
	private Image car;
	private AngleXEmulator emulator;
	private double l1;
	private double l2;
	
	public DisplayPanel(AngleXEmulator emulator){
		this.emulator = emulator;
		try {
//			bg = ImageIO.read(new File("bg1.jpg"));
//			car = ImageIO.read(new File("car.png"));
			bg = ImageIO.read(getClass().getResource("/resource/bg1.jpg"));
			car = ImageIO.read(getClass().getResource("/resource/car.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	@Override
	public void paintComponent(Graphics g1){
		if (emulator == null)
			return;
		try{
		Graphics2D g = (Graphics2D)g1;
		super.paintComponent(g);
		double force;//推力 右正
		double theta1, theta2;//角度值 单位 弧度 逆时针正
		theta1 = emulator.getSs().output()[1];
		theta2 = emulator.getSs().output()[2];
		double x = emulator.getSs().output()[0];
		force = emulator.getGain4().output()[0];
//		double df = emulator.getRs().output()[0];//干扰
		int w = this.getSize().width;
		int h = this.getSize().height;
		int iw = bg.getWidth(null);
		int ih = bg.getHeight(null);
		iw = iw*h/ih;
		ih = h;

		int left2;
		int left1 = (-(int)(x*50))%iw;
		if (left1 < 0){
			left2 = left1 + iw;
		}else{
			left2 = left1 - iw;
		}
		g.drawImage(bg, left1, 0, iw, ih, null);
		g.drawImage(bg, left2, 0, iw, ih, null);
		
		g.setColor(Color.YELLOW);
		g.drawString("当前推力" + force, 10, 10);
//		g.drawString("当前干扰" + df, 10, 30);
		g.drawString("当前位移" + x, 10, 50);
		g.drawString("当前角度1" + theta1, 10, 70);
		g.drawString("当前角度2" + theta2, 10, 70);
		g.setColor(new Color(0x00aa88));
		//g.drawLine(0, h-h/10, w, h-h/10);
		//g.drawRect(w/3, h-w/4-h/10, w/3, w/4);
		g.drawImage(car, w/3, h-w/4-h/10, w/3, w/4, null);
		int px1, py1, px2, py2, px3, py3; //杆两端坐标
		px1 = w/2;
		py1 = h-w/4-h/10+w/10;
		double l1 = h/4;
		double l2 = h/4;
		px2 = px1 - (int)(l1 * Math.sin(theta1));
		py2 = py1 - (int)(l1 * Math.cos(theta1));
		px3 = px2 - (int)(l2 * Math.sin(theta2));
		py3 = py2 - (int)(l2 * Math.cos(theta2));
		g.setStroke(new BasicStroke(4));
		g.drawLine(px1, py1, px2, py2);
		g.setColor(new Color(0x00aa88));
		g.drawLine(px2, py2, px3, py3);
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.ORANGE);
		g.drawOval(px2-3, py2-3, 6, 6);
		}catch (NullPointerException e){
//			e.printStackTrace();
		}
	}
	public void setEmulator(AngleXEmulator emulator) {
		this.emulator = emulator;
	}
	public void setL(double l1, double l2) {
		this.l1 = l1;
		this.l2 = l2;
	}
}
