package org.fzhen.emulator.component;

public class Gain implements BasicComponent{
	private double[] input;
	private double[] output;
	private double gain;
	
	public Gain(double g){
		this.gain = g;
	}

	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		this.input = input;
		int i;
		output = new double[input.length];
		for (i = 0; i < input.length; i++){
			output[i] = input[i]*gain;
		}
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate(){
		
	}

	@Override
	public void setCycle(double cycle) {
	}

}
