package org.fzhen.emulator.component.mFlc;

import java.util.ArrayList;
import java.util.List;

import org.fzhen.emulator.component.BasicComponent;

public class MamdaniFLC implements BasicComponent {
	private List<MFlcElement> inputElements;
	private List<MFlcOutElement> outputElements;
	double[] inputs;
	double[] outputs;
	List<String> rules;

	public MamdaniFLC() {
		rules = new ArrayList<String>();
		inputElements = new ArrayList<MFlcElement>();
		outputElements = new ArrayList<MFlcOutElement>();
	}

	class Rule {
		int[] input;
		int[] output;
		double[] weight;// 1
		int conn;//连接词 and=1
	}

	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		inputs = input;
		calcOutput();
	}

	private void calcOutput() {
		int i, j;
		int nitem;
		int nInput = inputElements.size();
		double[][] mfValue = new double[nInput][];
		for (i = 0; i < nInput; i++) {
			nitem = inputElements.get(i).getItems().size();
			mfValue[i] = new double[nitem];
			for (j = 0; j < nitem; j++) {
				mfValue[i][j] = inputElements.get(i).getItems().get(j).getMf()
						.getMembership(inputs[i]);
			}
		}
		
		//计算对应输入的输出   
		int rlen = rules.size();
		Rule rule;
		double min = 2; //隶属度 <=1
		for (i = 0; i < outputElements.size(); i++) {
			outputElements.get(i).getRegions().clear();
		}
		for (i = 0; i < rlen; i++) {//第i条规则
			min = 2;
			rule = decode(rules.get(i));
			int[] ins = rule.input; //第i条规则的输入
			for (j = 0; j < ins.length; j++) {//第i条规则输入j
				if (min > mfValue[j][ins[j]]) {
					min = mfValue[j][ins[j]];
				}
			}
			int[] outs = rule.output; //第i条规则的输出
			//计算根据规则得到输出区域
			for (j = 0; j < outs.length; j++) {
				if (min >0){
				outputElements .get(j) .getRegions().add(
					outputElements.get(j).getItems().get(outs[j]).getMf().getRegion(min));
				}
			}
		}
		
		/*
		 * 对每个输出根据输出的区域使用相应的解模糊器解模糊得到输出值。
		 */
		outputs = new double[outputElements.size()];
		for (i = 0; i < outputElements.size(); i++) {
			outputs[i] = outputElements.get(i).getDefuzzifier().defuzzify();
		}
	}

	/**
	 * 解码规则 规则格式为：
	 * 格式 i i;o:w o:w;c
	 * i 输入
	 * o 输出 w weight
	 * c 连接词 and
	 * @param ruleString
	 * @return
	 */
	private Rule decode(String ruleString) {
		Rule rule = new Rule();
		String[] r = ruleString.split(";");
		try {
			//得到连接词
			rule.conn = Integer.parseInt(r[2]);
			//得到输入
			String[] r0 = r[0].split(" ");
			rule.input = new int[r0.length];
			for (int i = 0; i < r0.length; i++) {
				rule.input[i] = Integer.parseInt(r0[i]);
			}
			//输出
			String[] r1 = r[1].split(" ");
			rule.output = new int[r1.length];
			rule.weight = new double[r1.length];
			for (int i = 0; i < r1.length; i++) {
				String[] r1i = r1[i].split(":");
				rule.output[i] = Integer.parseInt(r1i[0]);
				rule.weight[i] = Double.parseDouble(r1i[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return rule;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	public void addRule(String ruleStr) {
		if (rules == null)
			rules = new ArrayList<String>();
		Rule rule = decode(ruleStr);
		int i, j;
		/*
		 * 检查规则有效性
		 */
		int[] ins = rule.input; //第i条规则的输入
		if (ins.length > inputElements.size())
			throw new RuntimeException("规则输入部分有误");
		int[] outs = rule.output; //第i条规则输出
		if (outs.length > outputElements.size())
			throw new RuntimeException("规则输出部分有误");
		rules.add(ruleStr);
	}

	@Override
	public double[] output() {
		return outputs;
	}

	public void addMFlcInputElement(MFlcElement MFlcInputElement) {
		inputElements.add(MFlcInputElement);
	}

	public void addMFlcOutputElement(MFlcOutElement MFlcOutElement) {
		outputElements.add(MFlcOutElement);
	}


	@Override
	public void emulate() {
		
	}

	@Override
	public void setCycle(double cycle) {
		
	}

}
