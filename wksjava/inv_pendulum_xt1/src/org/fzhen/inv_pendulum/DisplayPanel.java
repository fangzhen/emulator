package org.fzhen.inv_pendulum;
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
	
	public DisplayPanel(AngleXEmulator emulator){
		this.emulator = emulator;
		try {
//			bg = ImageIO.read(new File("resource/bg1.jpg"));
			bg = ImageIO.read(getClass().getResource("/resource/bg1.jpg"));
//			car = ImageIO.read(new File("resource/car.png"));
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
		double theta;//角度值 单位 弧度 逆时针正
		theta = emulator.getSs().output()[1];
		force = emulator.getGain2().output()[0];
		double df = emulator.getRs().output()[0];		
		int w = this.getSize().width;
		int h = this.getSize().height;
		int iw = bg.getWidth(null);
		int ih = bg.getHeight(null);
		iw = iw*h/ih;
		ih = h;
		double x = emulator.getSs().output()[0];

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
		g.drawString("当前干扰" + df, 10, 30);
		g.drawString("当前位移" + x, 10, 50);
		g.drawString("当前角度" + theta, 10, 70);
		g.setColor(new Color(0x00aa88));
		//g.drawLine(0, h-h/10, w, h-h/10);
		//g.drawRect(w/3, h-w/4-h/10, w/3, w/4);
		g.drawImage(car, w/3, h-w/4-h/10, w/3, w/4, null);
		int px1, py1, px2, py2; //杆两端坐标
		px1 = w/2;
		py1 = h-w/4-h/10+w/10;
		double l = h/2;
		px2 = px1 - (int)(l * Math.sin(theta));
		py2 = py1 - (int)(l * Math.cos(theta));
		g.setStroke(new BasicStroke(4));
		g.drawLine(px1, py1, px2, py2);
		}catch (NullPointerException e){
	//		e.printStackTrace();
		}
	}
	public void setEmulator(AngleXEmulator emulator) {
		this.emulator = emulator;
	}
}
