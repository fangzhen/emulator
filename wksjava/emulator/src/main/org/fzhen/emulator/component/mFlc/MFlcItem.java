package org.fzhen.emulator.component.mFlc;

public class MFlcItem {
	private String text;
	private int id;
	private MsFunction mf;

	public void setPara(String text, int id,  MsFunction mf){
		this.text = text;
		this.id = id;
		this.mf = mf;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MsFunction getMf() {
		return mf;
	}

	public void setMf(MsFunction mf) {
		this.mf = mf;
	}
}
