package org.fzhen.emulator.component.mFlc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrimWrapper implements MsFunction {

	private double x1, x2, x3;
	private double ms;
	private double max, min;

	/**
	 * 要求x1<x2<x3
	 * @param x1
	 * @param x2
	 * @param x3
	 */
	public TrimWrapper(double x1, double x2, double x3) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		ms = 1.0;
	}

	@Override
	public double getMembership(double x) {
		if (x <= x1)
			return 0;
		else if (x >= x3)
			return 0;
		else if (x <= x2) {
			return ms * (x - x1) / (x2 - x1);
		} else {
			return ms * (x3 - x) / (x3 - x2);
		}
	}

	//返回4个顶点
	@Override
	public double[][] getRegion(double ms) {
		if (ms <= 0 || ms > this.ms) {
			return null;
		}
		double[][] region = new double[4][2];
		region[0][0] = x1;
		region[0][1] = 0;
		region[3][0] = x3;
		region[3][1] = 0;
		region[1][0] = ms * (x2 - x1) / this.ms + x1;
		region[1][1] = ms;
		region[2][0] = x3 - (ms * (x3 - x2) / this.ms);
		region[2][1] = ms;
		List<double[]> lregion = new ArrayList(Arrays.asList(region));
		if (max >region[0][0] && max < region[1][0]){
			lregion.remove(3);
			lregion.remove(2);
			lregion.get(1)[1] = region[1][1]*(region[1][0]-region[0][0])/(max-region[0][0]);
			lregion.get(1)[0] = max;
		}
		else if (max >=region[1][0] && max < region[2][0]){
			lregion.remove(3);
			lregion.get(2)[1] = ms;
			lregion.get(2)[0] = max;
		}
		else if (max >=region[2][0] && max < region[3][0]){
			lregion.get(3)[1] = region[2][1]*(region[2][0]-region[3][0])/(max-region[3][0]);
			lregion.get(3)[0] = max;
		}else if (max < region[0][0]){
			lregion.clear();
		}
		
		if (min >region[0][0] && min < region[1][0]){
			lregion.get(0)[1] = region[1][1]*(region[1][0]-region[0][0])/(min-region[0][0]);
			lregion.get(0)[0] = min;
		}
		else if (min >=region[1][0] && min < region[2][0]){
			lregion.remove(0);
			lregion.get(0)[1] = ms;
			lregion.get(0)[0] = min;
		}
		else if (min >=region[2][0] && min < region[3][0]){
			lregion.remove(0);
			lregion.remove(0);
			lregion.get(0)[1] = region[2][1]*(region[2][0]-region[3][0])/(min-region[3][0]);
			lregion.get(0)[0] = min;
		}else if (min >= region[3][0]) {
			lregion.clear();
		}
		double [][]temp=new double[][]{};
		return lregion.toArray(temp);
	}

	@Override
	public void setMax(double max) {
		this.max = max;
	}

	@Override
	public void setMin(double min) {
		this.min = min;
	}

}
