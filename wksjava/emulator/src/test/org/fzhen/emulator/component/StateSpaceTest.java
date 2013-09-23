package org.fzhen.emulator.component;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class StateSpaceTest {
	private StateSpace ss;
	
	@Before
	public void init(){
		ss = new StateSpace();
		double[][] initCondi = new double[][]{{0},{0}};
		ss.init(initCondi);
		double[][] A = {{0,1},{100.0/12,0}};
		double[][] B= {{0},{1/12}};
		double[][] C = {{1,0}};
		double[][] D = {{0}};
		ss.setStateSpacePara(A, B, C, D);
		ss.discretize();
	}

	@Test
	public void test() {
		ss.input(new double[]{1});
		ss.setCycle(1e-5);
		ss.emulate();
		ss.output();
	}

}
