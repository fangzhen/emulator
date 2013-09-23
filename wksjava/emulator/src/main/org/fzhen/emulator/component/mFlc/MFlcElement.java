package org.fzhen.emulator.component.mFlc;

import java.util.ArrayList;
import java.util.List;


public class MFlcElement {
	private int id;
	private double max;
	private double min;
	private String text;
	private List<MFlcItem> items;
	
	public MFlcElement(){}
	MFlcElement(int id, String text, double min, double max){
		setPara(id, text, min, max);
	}
	public void setPara(int id, String text, double min, double max){
		this.id = id;
		this.text = text;
		this.min = min;
		this.max = max;
		items = new ArrayList<MFlcItem>();
	}
	
	public void addItem(MFlcItem item){
		item.getMf().setMax(max);
		item.getMf().setMin(min);
		items.add(item);
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<MFlcItem> getItems() {
		return items;
	}

	public void setItems(List<MFlcItem> items) {
		this.items = items;
	}
}
