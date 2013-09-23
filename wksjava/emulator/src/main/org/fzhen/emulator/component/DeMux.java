package org.fzhen.emulator.component;

public class DeMux implements BasicComponent{
	private int curInd;
	private double[] input;
	
	public DeMux(){
		curInd = 0;
	}
	@Override
	public void input(double[] input) {
		this.input = input;
	}

	/**
	 * bug：因为output指针每次都后移，用gain(1)隔离开~~
	 */
	@Override
	public double[] output() {
		return new double[]{input[curInd++]};
	}

	@Override
	public void emulate() {
		curInd = 0;
	}

	@Override
	public void setCycle(double cycle) {
	}

}
