package org.fzhen.emulator.component.mFlc;

import java.util.ArrayList;
import java.util.List;

public class MFlcOutElement extends MFlcElement{
	private List regions;
	private Defuzzifier defuzzifier;
	public MFlcOutElement(){
	 regions = new ArrayList();
	}
	public void addRegion(double[] region){
		regions.add(region);
	}
	public void setDefuzzifier(Defuzzifier defuzzifier) {
		defuzzifier.setRegions(regions);
		this.defuzzifier = defuzzifier;
	}
	public List getRegions() {
		return regions;
	}
	public void setRegions(List regions) {
		this.regions = regions;
	}
	public Defuzzifier getDefuzzifier() {
		return defuzzifier;
	}

}