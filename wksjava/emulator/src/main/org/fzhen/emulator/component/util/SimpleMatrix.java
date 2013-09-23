package org.fzhen.emulator.component.util;

public class SimpleMatrix {
	public static double[][]tranpose(double[][]m){
		//TODO:impl
		return null;
	}
	public static double[][] multiplyScalar(double [][]A, double lm){
		int i, j;
		int m = A.length;
		int n;
		if (m != 0)
			n = A[0].length;
		else 
			n = 0;
		double[][] C = new double[m][n];
		for (i = 0; i < m; i++)
			for (j = 0; j < n; j++){
				C[i][j] = A[i][j]*lm;
			}
		return C;
	}
	public static double[][] multiply(double[][]A, double[][]B){
		if (A.length == 0 || B.length == 0)
			return null;
		if (A[0].length != B.length)//not match
			return null;
		int m = A.length;
		int r = B.length;
		int n = B[0].length;
		double [][]C = new double[m][n];
		int i, j, k;
		for (i = 0; i < m; i ++){
			for (j = 0; j < n; j ++){
				C[i][j] = 0;
				for (k = 0; k < r; k++){
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}
	public static double[][] plus(double[][]A, double[][]B){
		if (A.length != B.length)
			return null;
		if (A.length != 0 && A[0].length != B[0].length)
			return null;
		int i, j;
		int m = A.length;
		int n;
		if (m != 0)
			n = A[0].length;
		else 
			n = 0;
		
		double [][]C = new double[m][n];
		for (i = 0; i < m; i++)
			for (j = 0; j <n; j++){
				C[i][j] = A[i][j] + B[i][j];
			}
		return C;
	}
	
	public static double[][] identityMat(int d){
		double[][] I = new double[d][d];
		for (int i = 0; i < d; i++){
			I[i][i] = 1;
		}
		return I;
	}

}
