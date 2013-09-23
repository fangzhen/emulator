package org.fzhen.emulator.component;

import java.util.Arrays;

public class Adder implements BasicComponent{
	private double[]input;
	private double[]output;
	
	@Override
	public void input(double[] input) {
		this.input = input;
		if (output == null){
			output = Arrays.copyOf(input, input.length);
		}else if(output.length == input.length){
			for (int i = 0; i < input.length; i++){
				output[i] += input[i];
			}
		}else{
			return;
		}
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate() {
		output = null;
	}

	@Override
	public void setCycle(double cycle) {
		
	}

}
