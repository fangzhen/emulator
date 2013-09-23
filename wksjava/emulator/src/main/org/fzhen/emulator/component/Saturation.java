package org.fzhen.emulator.component;

public class Saturation implements BasicComponent{
	private double[] input;
	private double[] output;
	private double min;
	private double max;
	
	public Saturation(double min, double max){
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		this.input = input;
		int len = input.length;
		output = new double[len];
		for (int i= 0; i < len; i++){
			if(input[i] < min)
				output[i] = min;
			else if (input[i] > max)
				output[i] = max;
			else
				output[i] = input[i];
		}
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
