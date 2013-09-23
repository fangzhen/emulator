package org.fzhen.emulator.component;

public interface BasicComponent {
	void input(double[] input);
	double[] output();
	void emulate();
	void setCycle(double cycle);
}
