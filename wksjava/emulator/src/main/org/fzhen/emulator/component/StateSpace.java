package org.fzhen.emulator.component;

import org.fzhen.emulator.component.util.SimpleMatrix;

public class StateSpace implements BasicComponent{
	private double u;// 输入
	private double[] output;
	private double[][] state;//当前状态
	private double[][]A;
	private double[][]B;
	private double[][]C;
	private double[][]D;
	private double[][]G;
	private double[][]H;
	private double T = 1e-5;//离散化 采样周期
	private double cycle;
	
	
	/* 初始状态 */
	public void init(double[][] initCondi) {
		state = initCondi;
		u = 0;
		calcOutput();
	}

	/*
	 * 状态方程(连续） X'=AX+Bu  Y=CX+Du 
	 * 输入状态方程参数
	 */
	public void setStateSpacePara(double[][] A, double[][] B, double[][] C, double[][] D) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
	}

	// 设置仿真周期,即离散化周期
	public void setSimuCycle(double T) {
		this.T = T;
	}

	/*
	 * 状态方程离散化（近似算法--离散化差商代替微商） T 采样周期
	 * 连续方程 X'=AX+Bu y=CX+Du
	 * 离散化 x((k+1)T)=G(T)x(kT)+H(T)u(kT)
	 * 		y(kT)=CX(kT)+Du(kT)
	 * G=I+AT H=BT
	 */
	public void discretize() {
		double[][]I = SimpleMatrix.identityMat(A.length);
		this.G = SimpleMatrix.plus(I, SimpleMatrix.multiplyScalar(this.A, this.T));
		this.H = SimpleMatrix.multiplyScalar(this.B, this.T);
	}

	/*
	 * 直接输入离散状态方程 X(k+1)=AX(k)+Bu Y=CX+Du
	 */
	public void setDiscretePara(double[][] G, double[][] H, double[][] C, double[][] D) {
		this.G = G;
		this.H = H;
		this.C = C;
		this.D = D;
	}

	/*
	 * 设置输入u
	public void setInput(double u) {
	}
	 */
	/*
	 * x((k+1)T)=G(T)x(kT)+H(T)u(kT)
	 */
	private void nextState() {
		state = SimpleMatrix.plus(SimpleMatrix.multiply(G, state),
				SimpleMatrix.multiplyScalar(H, u));
	}

	private void nextState(double t) {
		int n = (int)Math.round (t/T);
		for (int i = 0; i < n; i++){
			nextState();
		}
	}

	@Override
	public void input(double[] input) {
		if(input ==null){
			return;
		}
		u = input[0];
		calcOutput();
	}

	/*
	 * y=Cx+Du
	 */
	private void calcOutput() {
		//输出方程计算
		double[][]output;
		output = SimpleMatrix.plus (
				SimpleMatrix.multiply(this.C, state),
				SimpleMatrix.multiplyScalar(this.D, u));
		this.output = new double[output.length];
		for (int i = 0; i < output.length; i++){
			this.output[i] = output[i][0];
		}
	}

	@Override
	public double[] output() {
		return output;
	}


	@Override
	public void emulate() {
		nextState(cycle);
		
	}

	@Override
	public void setCycle(double cycle) {
		this.cycle = cycle;
	}

}