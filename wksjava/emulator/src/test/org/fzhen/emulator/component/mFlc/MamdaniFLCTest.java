package org.fzhen.emulator.component.mFlc;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MamdaniFLCTest {
	private MamdaniFLC flc;
	@Before
	public void init(){
		flc = new MamdaniFLC();
		List<String> rules = new ArrayList<String>();
		rules.add("0;0:1;0");//i;o:w o:w;c
		rules.add("1;1:1;0");//i;o:w o:w;c
		rules.add("2;2:1;0");//i;o:w o:w;c
		flc.setRules(rules);
		
		MFlcItem mi;
		MFlcElement ie = new MFlcElement();
		ie.setPara(0, "x1", -2, 2);
		
		//输入
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-1,0,1));;
		ie.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("N", 0, new TrimWrapper(-2,-2,0));;
		ie.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("P", 0, new TrimWrapper(0, 2, 2));//TODO 0 2 3
		ie.addItem(mi);
		
		flc.addMFlcInputElement(ie);
		
		MFlcOutElement io = new MFlcOutElement();
		io.setPara(0, "y1", -2, 2);
		TrapeGravDefuzzifier defuzzifier = new TrapeGravDefuzzifier();
		io.setDefuzzifier(defuzzifier);
		
		//输出
		mi = new MFlcItem();
		mi.setPara("Z", 0, new TrimWrapper(-1,0,1));;
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("N", 0, new TrimWrapper(-2,-2,0));;
		io.addItem(mi);
		mi = new MFlcItem();
		mi.setPara("P", 0, new TrimWrapper(0, 2, 3));;
		io.addItem(mi);
		flc.addMFlcOutputElement(io);
	}

	@Test
	public void testInput() {
		double[]x;
		x = new double[]{-0.5};
		flc.input(x);
		x = new double[]{0.5};
		flc.input(x);
	}

	@Test
	public void testOutput() {
		fail("Not yet implemented");
	}

}
