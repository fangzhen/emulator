package org.fzhen.emulator.component;

import java.util.Random;

public class RandomSignal implements BasicComponent{
	private double[] output = new double[]{0};
	private double threshold = 0.1;
	private double min, max;
	private double period; //随机脉冲持续时间
	private int nperiod;//几个cycle
	private double cycle;
	private int curnp;
	public RandomSignal(){
		min = 0;
		max = 10;
	}
	public RandomSignal(double ts, double min, double max, double period){
		setPara(ts, min, max, period);
	}
	public void setPara(double ts, double min, double max, double period){
		this.max = max;
		this.min = min;
		this.threshold = ts;
		this.period = period;
	}
	@Override
	public void input(double[] input) {
		//nothing to do, no input;
	}

	@Override
	public double[] output() {
		return output;
	}

	@Override
	public void emulate() {
		if (curnp > 0){
			curnp --;
			return;
		}
		Random r = new Random();
		double r1 = r.nextDouble();
		if (r1 < threshold){
			double r2 = r.nextDouble()*(max-min)+min;
			output = new double[]{r2};
			curnp = nperiod;
		}else{
			output = new double[]{0};
			curnp = nperiod;
		}
	}

	@Override
	public void setCycle(double cycle) {
		this.cycle = cycle;
		nperiod = (int)Math.round(period/cycle);
	}
	

}
