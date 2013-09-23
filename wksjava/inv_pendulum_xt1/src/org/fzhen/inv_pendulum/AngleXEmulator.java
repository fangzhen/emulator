package org.fzhen.inv_pendulum;

import java.util.ArrayList;
import java.util.List;

import org.fzhen.emulator.Emulator;
import org.fzhen.emulator.component.Adder;
import org.fzhen.emulator.component.DeMux;
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

public class AngleXEmulator {

	private Emulator emulator;
	private StateSpace ss;
	private	MamdaniFLC flc0, flc1;
	private Gain gain0, gain1, gain2, gain3, gain4, gain5,gain6;
	private Saturation sat0, sat1, sat2, sat3;
	private Derivative dv1, dv0;
	private Mux mux0, mux1;
	private DeMux demux;
	private Adder adder;
	private RandomSignal rs;//扰动
	private Adder adder1;//位移输出转为角度后相加
	
	private double M, m;
	
	private MamdaniFLC generateFlcx(){
		return generateFlct();
	}
	private MamdaniFLC generateFlct(){
		MamdaniFLC flc;
		flc = new MamdaniFLC();


		MFlcItem mi;
		//input
		MFlcElement ie1 = new MFlcElement();
		ie1.setPara(0, "x1", -2, 2);
		mi = new MFlcItem();
		mi.setPara("N", 0, new TrimWrapper(-3,-2,0));
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-2,0,2));
		ie1.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("P", 0, new TrimWrapper(0, 2, 3));
		ie1.addItem(mi);
		flc.addMFlcInputElement(ie1);

		MFlcElement ie2 = new MFlcElement();
		ie2.setPara(0, "x2", -2, 2);
		mi = new MFlcItem();
		mi.setPara("N", 0, new TrimWrapper(-3,-2,0));
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-2,0,2));;
		ie2.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("P", 0, new TrimWrapper(0, 2, 3));
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
		rules.add("1 1;3:1;0");//i i;o:w o:w;c //ZZ Z
		rules.add("0 0;5:1;0");//N N PM
		rules.add("2 2;1:1;0");//PP NM
		rules.add("2 0;3:1;0");//PN Z
		rules.add("0 2;3:1;0");//NP Z
		rules.add("2 1;2:1;0");//PZ NS
		rules.add("1 2;2:1;0");//ZP NS
		rules.add("0 1;4:1;0");//NZ PS
		rules.add("1 0;4:1;0");//ZN PS
		flc.setRules(rules);

		return flc;
	}
	public void init(double M, double m, double l, double x, double v, double t, double w){
		this.m = m;
		this.M = M;
		emulator = new Emulator();

		ss = new StateSpace();
		double g = 9.8;
		double I = 1.0/3 * l * l;
		double deno = I*(M+m) + M*m*l*l;
		double [][]A={
				{0,1,0,0},
				{0,0, m*m*g*l*l/deno, 0},
				{0,0,0,1},
				{0,0,m*g*l*(M+m)/deno, 0},
		};
		double [][]B={{0},{(I+m*l*l)/deno}, {0}, {m*l/deno}};
		double[][] C = {{1,0,0,0}, {0,0,1,0}};
		double[][] D = {{0},{0}};
		double[][] initCondi = new double[][]{{x}, {v}, {t}, {w}};
//		double[][] initCondi = new double[][]{{50},{0},{0.20},{0}};
//		double[][] A = {{0,1,0,0},{0,0,10.0/12,0},
//				{0,0,0,1}, {0,0,100.0/12,0}};
//		double[][] B= {{0},{1.0/9},{0},{1.0/12}};
//		double[][] C = {{1,0,0,0}, {0,0,1,0}};
//		double[][] D = {{0},{0}};
		ss.setStateSpacePara(A, B, C, D);
		ss.discretize();
		ss.init(initCondi);
		emulator.add(ss);

		demux = new DeMux();
		emulator.add(demux);
		
		flc0 = generateFlct();//angle controller
		emulator.add(flc0);
		flc1 = generateFlcx(); //position controller ;flc0 and flc1 turns to be the same;
		emulator.add(flc1);

		gain0= new Gain(2);
		gain1= new Gain(2);
		gain2 = new Gain(10*(M+m));
		gain3 = new Gain(0.2);
		gain4 = new Gain(0.01);
		gain5 = new Gain(0.1);
		gain6 = new Gain(1);
		emulator.add(gain0);
		emulator.add(gain1);
		emulator.add(gain2);
		emulator.add(gain3);
		emulator.add(gain4);
		emulator.add(gain5);
		emulator.add(gain6);

		sat0 = new Saturation(-2, 2);
		sat1 = new Saturation(-2, 2);
		sat2 = new Saturation(-2, 2);
		sat3 = new Saturation(-2, 2);
		emulator.add(sat0);
		emulator.add(sat1);
		emulator.add(sat2);
		emulator.add(sat3);

		dv0 = new Derivative();
		dv1 = new Derivative();
		emulator.add(dv0);
		emulator.add(dv1);

		mux0 = new Mux();
		mux1 = new Mux();
		emulator.add(mux0);
		emulator.add(mux1);

		adder = new Adder();
		emulator.add(adder);
		rs = new RandomSignal(0, 0, 0, 0.1);
		emulator.add(rs);
		
		adder1 = new Adder();
		emulator.add(adder1);


		//jiaodu.mdl
		emulator.connect(ss, demux);
		emulator.connect(demux,gain6);//gain6 隔离demux输出 demux设计
		emulator.connect(gain6, gain4);
		emulator.connect(gain4, sat2);
		emulator.connect(sat2, mux1);
		emulator.connect(gain6, dv1);
		emulator.connect(dv1, gain3);
		emulator.connect(gain3, sat3);
		emulator.connect(sat3, mux1);
		emulator.connect(mux1, flc1);
		emulator.connect(flc1, gain5);
		emulator.connect(gain5, adder1);
		emulator.connect(demux, adder1);
		emulator.connect(adder1, gain0);
		emulator.connect(gain0, sat1);
		emulator.connect(sat1, mux0);
		emulator.connect(adder1, dv0);
		emulator.connect(dv0, gain1);
		emulator.connect(gain1, sat0);
		emulator.connect(sat0, mux0);
		emulator.connect(mux0, flc0);
		emulator.connect(flc0, gain2);
		emulator.connect(gain2, adder);
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
	public Gain getGain0() {
		return gain0;
	}
	public Gain getGain1() {
		return gain1;
	}
	public Gain getGain2() {
		return gain2;
	}
	public Gain getGain3() {
		return gain3;
	}
	public Gain getGain4() {
		return gain4;
	}
	public Gain getGain5() {
		return gain5;
	}
	public Gain getGain6() {
		return gain6;
	}
	public Saturation getSat0() {
		return sat0;
	}
	public Saturation getSat1() {
		return sat1;
	}
	public Saturation getSat2() {
		return sat2;
	}
	public Saturation getSat3() {
		return sat3;
	}
	public Derivative getDv1() {
		return dv1;
	}
	public Derivative getDv0() {
		return dv0;
	}
	public Mux getMux0() {
		return mux0;
	}
	public Mux getMux1() {
		return mux1;
	}
	public DeMux getDemux() {
		return demux;
	}
	public Adder getAdder() {
		return adder;
	}
	public RandomSignal getRs() {
		return rs;
	}
	public Adder getAdder1() {
		return adder1;
	}
	public void disturb() {
		double rupper = 5*(m+M);
		double pos;
		pos = 0.2;
		rs.setPara(pos, -rupper, rupper, 0.1);
	}
	public void undisturb() {
		rs.setPara(0, 0, 0, 0.1);
	}
}
