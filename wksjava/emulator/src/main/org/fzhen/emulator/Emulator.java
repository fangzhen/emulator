package org.fzhen.emulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fzhen.emulator.component.BasicComponent;

public class Emulator {
	private double cycle; 
	private List<BasicComponent[]> connections;
	private List<BasicComponent> components;

	public Emulator(){
		connections = new ArrayList<BasicComponent[]>();
		components = new ArrayList<BasicComponent>();
	}

	public void add(BasicComponent component) {
		components.add(component);
	}

	public void remove(int pos) {
	}

	public void remove(BasicComponent bc) {

	}

	public void connect(int outPos, int inPos) {

	}

	public void connect(BasicComponent out, BasicComponent in) {
		connections.add(new BasicComponent[]{out, in});
	}

	public void emulate(double period) {
		int n = (int) (period/cycle);
		emulate(n);
	}
	public void emulate (int n){
		for (int i = 0; i < n; i++){
			for (BasicComponent component : components){
				component.emulate();
			}
			for (BasicComponent[] connection : connections) {
				double[]out = connection[0].output();
//				if (i % 1000 == 0)
//					System.out.print(Arrays.toString(out) + "\t");
				connection[1].input(out);
			}
//			if (i % 1000 == 0)
//				System.out.println(i/1000);
//			System.out.println(i);
		}
	}

	public void setCycle(double cycle) {
		this.cycle = cycle;
	}
	public void init(){
		for (BasicComponent component : components){
			component.setCycle(cycle);
		}
		for (BasicComponent[] connection : connections) {
			double[]out = connection[0].output();
			connection[1].input(out);
		}
	}
}
