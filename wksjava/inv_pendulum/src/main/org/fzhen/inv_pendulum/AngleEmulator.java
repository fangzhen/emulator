package org.fzhen.inv_pendulum;
import java.util.ArrayList;
import java.util.List;

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

public class AngleEmulator{
		
	private Emulator emulator;
	private StateSpace ss;
	private	MamdaniFLC flc;
	private Gain gain1, gain2, gain3;
	private Saturation sat1, sat2;
	private Derivative dv;
	private Mux mux;
	private Adder adder;
	private RandomSignal rs;
	public MamdaniFLC generateFlc(){
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
	public void init(){
		emulator = new Emulator();

		ss = new StateSpace();
		double[][] initCondi = new double[][]{{-0.3},{-0.1}};
		double[][] A = {{0,1},{100.0/12,0}};
		double[][] B= {{0},{1.0/12}};
		double[][] C = {{1,0}};
		double[][] D = {{0}};
		ss.setStateSpacePara(A, B, C, D);
		ss.discretize();
		ss.init(initCondi);
		emulator.add(ss);

		flc = generateFlc();
		emulator.add(flc);

		gain1= new Gain(2);
		gain2 = new Gain(2);
		gain3 = new Gain(120);
		emulator.add(gain1);
		emulator.add(gain2);
		emulator.add(gain3);

		sat1 = new Saturation(-2, 2);
		sat2 = new Saturation(-2, 2);
		emulator.add(sat1);
		emulator.add(sat2);

		dv = new Derivative();
		emulator.add(dv);

		mux = new Mux();
		emulator.add(mux);
		
		adder = new Adder();
		emulator.add(adder);
		
		rs = new RandomSignal(0.2, -80, 80, 0.1);
		emulator.add(rs);
		

		//jiaodu.mdl
		emulator.connect(ss, dv);
		emulator.connect(dv, gain1);
		emulator.connect(gain1, sat1);
		emulator.connect(ss, gain2);
		emulator.connect(gain2, sat2);
		emulator.connect(sat1, mux);
		emulator.connect(sat2, mux);
		emulator.connect(mux, flc);
		emulator.connect(flc, gain3);
		emulator.connect(gain3, adder);
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
	public MamdaniFLC getFlc() {
		return flc;
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
	public Saturation getSat1() {
		return sat1;
	}
	public Saturation getSat2() {
		return sat2;
	}
	public Derivative getDv() {
		return dv;
	}
	public Mux getMux() {
		return mux;
	}
	public Adder getAdder() {
		return adder;
	}
	public RandomSignal getRs() {
		return rs;
	}

}
