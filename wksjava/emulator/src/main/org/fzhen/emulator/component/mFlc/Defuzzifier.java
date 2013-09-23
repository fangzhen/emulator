package org.fzhen.emulator.component.mFlc;

import java.util.List;

public interface Defuzzifier {
	double defuzzify();
	void setRegions(List regions);
}
