package org.fzhen.inv_pendulum2l;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.fzhen.emulator.Emulator;
import org.fzhen.emulator.component.Adder;
import org.fzhen.emulator.component.DeMux;
import org.fzhen.emulator.component.Derivative;
import org.fzhen.emulator.component.Gain;
import org.fzhen.emulator.component.MatrixGain;
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

public class AngleXEmulator {

	private Emulator emulator;
	private StateSpace ss;
	private	MamdaniFLC flc0, flc1;
	private Gain  gain1, gain3, gain4;;
	private MatrixGain gain0, gain2;
	private Saturation sat0, sat1;
	private Derivative dv0;
	private Mux mux0;
	private Adder adder;
	private RandomSignal rs;//扰动
	
	private double M, m1, m2;
	
	private MamdaniFLC generateFlc(){
		MamdaniFLC flc;
		flc = new MamdaniFLC();


		MFlcItem mi;
		//input
		MFlcElement ie1 = new MFlcElement();
		ie1.setPara(0, "x1", -3, 3);
		mi = new MFlcItem();
		mi.setPara("NB", 0, new TrimWrapper(-4,-3, -2));//0
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NM", 0, new TrimWrapper(-3,-2,-1));//1
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NS", 0, new TrimWrapper(-2,-1,0));//2
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-1,0,1));//3
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PS", 0, new TrimWrapper(0,1,2));//4
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PM", 0, new TrimWrapper(1,2,3));//5
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PB", 0, new TrimWrapper(2,3,4));//6
		ie1.addItem(mi);
		flc.addMFlcInputElement(ie1);

		MFlcElement ie2 = new MFlcElement();
		ie2.setPara(0, "x1", -3, 3);
		mi = new MFlcItem();
		mi.setPara("NB", 0, new TrimWrapper(-4,-3, -2));//0
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NM", 0, new TrimWrapper(-3,-2,-1));//1
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NS", 0, new TrimWrapper(-2,-1,0));//2
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-1,0,1));//3
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PS", 0, new TrimWrapper(0,1,2));//4
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PM", 0, new TrimWrapper(1,2,3));//5
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PB", 0, new TrimWrapper(2,3,4));//6
		ie2.addItem(mi);
		flc.addMFlcInputElement(ie2);

		MFlcOutElement io = new MFlcOutElement();
		io.setPara(0, "y1", -3, 3);
		TrapeGravDefuzzifier defuzzifier = new TrapeGravDefuzzifier();
		io.setDefuzzifier(defuzzifier);

		//输出
		mi = new MFlcItem();
		mi.setPara("NB", 0, new TrimWrapper(-4,-3, -2));//0
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NM", 0, new TrimWrapper(-3,-2,-1));//1
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("NS", 0, new TrimWrapper(-2,-1,0));//2
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-1,0,1));//3
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PS", 0, new TrimWrapper(0,1,2));//4
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PM", 0, new TrimWrapper(1,2,3));//5
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("PB", 0, new TrimWrapper(2,3,4));//6
		io.addItem(mi);
		flc.addMFlcOutputElement(io);

		List<String> rules = new ArrayList<String>();
		//File ruleFile = new File("rule");
		try {
			String rule;
//			BufferedReader reader = new BufferedReader(new FileReader(ruleFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resource/rule")));
			while ((rule = reader.readLine()) != null){
				rules.add(rule);
			}
		} catch (FileNotFoundException e) {
			System.err.println("error in reading rule file. filename 'rule'");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		flc.setRules(rules);

		return flc;
	}
	public void init(double M, double m1, double l1, double m2, double l2, double x, double v, double t1, double w1, double t2, double w2){
		this.m1 = m1;
		this.m2 = m2;
		this.M = M;
		emulator = new Emulator();

		ss = new StateSpace();
//		double g = 9.8;
//		double I1 = 1.0/3 * l1 * l1;
//		double I2 = 1.0/3 * l2 * l2;
//		double deno = I*(M+m) + M*m*l*l;
//		double [][]A={
//				{0,0,0,1,0,0},
//				{0,0,0,0,1,0},
//				{0,0,0,0,0,1}, 
//				{0,0, m*m*g*l*l/deno, 0},
//				{0,0,0,1},
//				{0,0,m*g*l*(M+m)/deno, 0},
//		};
//		double [][]B={{0},{(I+m*l*l)/deno}, {0}, {m*l/deno}};
//		double[][] C = {{1,0,0,0}, {0,0,1,0}};
//		double[][] D = {{0},{0}};
		double[][] initCondi = new double[][] { { x }, { t1 }, { t2 }, {v},  {w1},{w2}};
//		double[][] initCondi = new double[][]{{0},{0.1},{0},{0},{0},{0}};
		double[][] A = {{0,0,0,1,0,0},{0,0,0,0,1,0},
				{0,0,0,0,0,1}, {0,-2.569,0.16408,-16.667,0.01718,-0.0011},{0,29.919,-15.181,40.317,-0.28268,0.0959},{0,-36.656,65.378,-49.395,0.64301,-0.41489}};
		double[][] B= {{0},{0},{0},{8.6462},{-20.914},{25.642}};
		double[][] C = {{1,0,0,0,0,0}, {0,1,0,0,0,0},{0,0,1,0,0,0}};
		double[][] D = {{0},{0},{0}};
		ss.setStateSpacePara(A, B, C, D);
		ss.discretize();
		ss.init(initCondi);
		emulator.add(ss);

		
		flc0 = generateFlc();//angle controller
		emulator.add(flc0);

		dv0 = new Derivative();
		emulator.add(dv0);
		
		gain0= new MatrixGain(new double[][]{{4.0620, 29.4664, 60.9328}});
		gain1= new Gain(4);
		gain2 = new MatrixGain(new double[][]{{3.5382, 11.4092, 9.2027}});
		gain3 = new Gain(10);
		gain4 = new Gain(20);
		emulator.add(gain0);
		emulator.add(gain1);
		emulator.add(gain2);
		emulator.add(gain3);
		emulator.add(gain4);

		sat0 = new Saturation(-3, 3);
		sat1 = new Saturation(-3, 3);
		emulator.add(sat0);
		emulator.add(sat1);

		mux0 = new Mux();
		emulator.add(mux0);

		adder = new Adder();
		emulator.add(adder);
		rs = new RandomSignal(0, 0, 0, 0.1);
		emulator.add(rs);


		//daolibaixt2l.mdl
		emulator.connect(ss, gain0);
		emulator.connect(gain0,gain1);
		emulator.connect(gain1,sat0);
		emulator.connect(sat0, mux0);
		emulator.connect(ss, dv0);
		emulator.connect(dv0, gain2);
		emulator.connect(gain2, gain3);
		emulator.connect(gain3, sat1);
		emulator.connect(sat1, mux0);
		emulator.connect(mux0, flc0);
		emulator.connect(flc0, gain4);
		emulator.connect(gain4, adder);
		emulator.connect(rs, adder);
		emulator.connect(adder, ss);
		

		emulator.setCycle(1e-4);
		emulator.init();

	}
	public Emulator getEmulator() {
		return emulator;
	}
	public StateSpace getSs() {
		return ss;
	}
	public MamdaniFLC getFlc0() {
		return flc0;
	}
	public MamdaniFLC getFlc1() {
		return flc1;
	}
	public MatrixGain getGain0() {
		return gain0;
	}
	public Gain getGain1() {
		return gain1;
	}
	public MatrixGain getGain2() {
		return gain2;
	}
	public Gain getGain3() {
		return gain3;
	}
	public Gain getGain4() {
		return gain4;
	}
	public Saturation getSat0() {
		return sat0;
	}
	public Saturation getSat1() {
		return sat1;
	}
	public Derivative getDv0() {
		return dv0;
	}
	public Mux getMux0() {
		return mux0;
	}
	public Adder getAdder() {
		return adder;
	}
	public RandomSignal getRs() {
		return rs;
	}
	public void disturb() {
		double rupper = 5*(m1+m2+M);
		double pos;
		pos = 0.2;
		rs.setPara(pos, -rupper, rupper, 0.1);
	}
	public void undisturb() {
		rs.setPara(0, 0, 0, 0.1);
	}
}
