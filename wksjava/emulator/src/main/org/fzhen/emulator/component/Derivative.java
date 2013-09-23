package org.fzhen.emulator.component;

public class Derivative implements BasicComponent{
	private double[] last;
	private double[] input;
	private double[] output;
	private boolean firstFlag = true;
	private double dt;

	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		this.input = input;
		if (firstFlag){
			output = new double[input.length];
		}
		else{
			output = new double[input.length];
			for (int i = 0; i < input.length; i++){
				output[i] = (this.input[i] - last[i])/dt;
			}
		}
		firstFlag = false;
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate() {
		last = input;
	}

	@Override
	public void setCycle(double cycle) {
		dt = cycle;
	}
}
