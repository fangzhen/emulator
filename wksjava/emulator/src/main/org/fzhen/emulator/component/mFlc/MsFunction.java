package org.fzhen.emulator.component.mFlc;

/**
 * membership function interface
 * @author fangzhen
 *
 */
public interface MsFunction {
	double getMembership(double x);
	//泛型方法
	<T> T getRegion(double ms);
	void setMax(double max);
	void setMin(double min);
}
