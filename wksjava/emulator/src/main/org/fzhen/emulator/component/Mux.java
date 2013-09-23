package org.fzhen.emulator.component;

import java.util.Arrays;

public class Mux implements BasicComponent{
	private double[]input;
	private double[]output;
	
	public Mux(){
		output = new double[0];
	}
	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		int leno = output.length;
		int leni = input.length;
		output = Arrays.copyOf(output, leni+leno);
		for (int i = leno; i < leni + leno; i++){
			output[i] = input[i-leno];
		}
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate() {
		output = new double[0];//一个仿真周期之后重置
	}

	@Override
	public void setCycle(double cycle) {
	}

}
