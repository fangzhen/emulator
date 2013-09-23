package org.fzhen.emulator.component;

import org.fzhen.emulator.component.util.SimpleMatrix;

public class MatrixGain implements BasicComponent {
	double[][] gain;
	double[] input;
	double[] output;
	public MatrixGain(double[][] gain){
		this.gain = gain;
	}

	@Override
	public void input(double[] input) {
		double[][] tmpin = new double[input.length][1];
		for (int i = 0; i < input.length; i++){
			tmpin[i][0] = input[i];
		}
		double[][] res = SimpleMatrix.multiply(gain, tmpin);
		output = res[0];
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate() {
	}

	@Override
	public void setCycle(double cycle) {
	}

}
