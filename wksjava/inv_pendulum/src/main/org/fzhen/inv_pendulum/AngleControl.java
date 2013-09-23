package org.fzhen.inv_pendulum;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.fzhen.emulator.Emulator;
import org.fzhen.emulator.component.Adder;
import org.fzhen.emulator.component.Derivative;
import org.fzhen.emulator.component.Gain;
import org.fzhen.emulator.component.Mux;
import org.fzhen.emulator.component.RandomSignal;
import org.fzhen.emulator.component.Saturation;
import org.fzhen.emulator.component.StateSpace;
import org.fzhen.emulator.component.mFlc.MFlcElement;
import org.fzhen.emulator.component.mFlc.MFlcItem;
import org.fzhen.emulator.component.mFlc.MFlcOutElement;
import org.fzhen.emulator.component.mFlc.MamdaniFLC;
import org.fzhen.emulator.component.mFlc.TrapeGravDefuzzifier;
import org.fzhen.emulator.component.mFlc.TrimWrapper;

public class AngleControl {
	private static AngleEmulator emulator = new AngleEmulator();
	public static void main(String[] args){
		emulator.init();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				MainFrame mf = new MainFrame(emulator);
				mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mf.setVisible(true);
			}
		});
	}
}

class MainFrame extends JFrame{
	private AngleEmulator emulator;
	public MainFrame(AngleEmulator emulator){
		this.emulator = emulator;
		DisplayPanel panel = new DisplayPanel(emulator);
		add(panel);
		setSize(200,300);
		Timer timer = new Timer(1, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
				MainFrame.this.emulator.getEmulator().emulate(1e-3);
				//emulator.emulator(1) //is not correct  compile error
			}
		});
		timer.start();
	}
	public AngleEmulator getEmulator() {
		return emulator;
	}
	public void setEmulator(AngleEmulator emulator) {
		this.emulator = emulator;
	}
}

class ParaPanel extends JPanel{
	public ParaPanel(){
	}
}

class DisplayPanel extends JPanel{
	private AngleEmulator emulator;
	public DisplayPanel(AngleEmulator emulator){
		this.emulator = emulator;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double force;//推力 右正
		double theta;//角度值 单位 弧度 逆时针正
		theta = emulator.getSs().output()[0];
		force = emulator.getGain3().output()[0];
		double df = emulator.getRs().output()[0];
		g.drawString("当前推力" + force, 10, 10);
		g.drawString("当前干扰" + df, 10, 30);
		int w = this.getSize().width;
		int h = this.getSize().height;
		g.drawLine(0, h-h/10, w, h-h/10);
		g.drawRect(w/3, h-w/4-h/10, w/3, w/4);
		int px1, py1, px2, py2; //杆两端坐标
		px1 = w/2;
		py1 = h-w/4-h/10;
		double l = h/3;
		px2 = px1 - (int)(l * Math.sin(theta));
		py2 = py1 - (int)(l * Math.cos(theta));
		g.drawLine(px1, py1, px2, py2);
	}
	public void setEmulator(AngleEmulator emulator) {
		this.emulator = emulator;
	}
}