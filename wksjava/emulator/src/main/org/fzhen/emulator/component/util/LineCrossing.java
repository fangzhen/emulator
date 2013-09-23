package org.fzhen.emulator.component.util;

import java.util.Arrays;
import java.util.List;

public class LineCrossing {
	private static final double exp = 1e-9;
	// begin 计算交点
	/**
	 * linei linej [0] 为左点，[1]为右点 1且linei[0][0] <= linej[0][0] 
	 * 返回值所有情况： 
	 * 有重合：	 								0x				0011
	 * 		完全重合1	 							03 			0000 
	 * 		linei 完全包含linej 2 					13 			0001 
	 * 		部分重合但没有互相包含 3 					23 			0010 
	 * 无重合：
	 * 		有交点												0010 
	 * 		linei 右端点与linej左端点重合，且共线 10		02			0000 
	 * 		有交点 线段中间 11 						12			0001 
	 * 		linei 右端点 12 						22			0010 
	 * 		linei 左端点 13 						32			0011 
	 * 		linej left 14 						42 			0100 
	 * 		linei left point = linej left point 52 			0101 
	 * 		linej left = linei right 16 		62			0110
	 * 		linej right 17 						72 			0111 
	 * 		linej right = linei left 18 		82 			1000 
	 * 		lienj right = linei right 19 		92			1001 
	 * 无交点 														0000 
	 * 		共线但无交点 40 							00 			0000 
	 * 		平行但无交点 41 							10 			0001
	 * 		无交点快速排斥检出 42 						20			0010 
	 * 		无交点跨立实验检出 43 						30			0011
	 *` 
	 * @param linei
	 * @param linej
	 * @param crossing
	 * @return
	 */
	public static int getCrossing(final double[][] linei,
			final double[][] linej, List<double[]> crossing) {
		// 完全重合
		if (Arrays.equals(linei[0], linej[0])
				&& Arrays.equals(linei[1], linej[1])) {
			return 0x03;//
		}
		double[][] vector1 = new double[2][2], vector2 = new double[2][2];
//		vector1 = Arrays.copyOf(linei, 2);
//		vector2 = Arrays.copyOf(linej, 2);
		vector1[0][0] = linei[0][0];
		vector1[0][1] = linei[0][1];
		vector1[1][0] = linei[1][0];
		vector1[1][1] = linei[1][1];
		vector2[0][0] = linej[0][0];
		vector2[0][1] = linej[0][1];
		vector2[1][0] = linej[1][0];
		vector2[1][1] = linej[1][1];
		
		if (Math.abs(crossProduct(vector1, vector2)) < exp) {// 两线段平行
			vector2[0] = linei[0];
			vector2[1] = linej[0];
			if (Math.abs(crossProduct(vector1, vector2)) < exp) {// 两线段共线
				if (linei[1][1] == linej[0][1]) {// linei 右端点与linej左端点重合
					crossing.add(linei[1]);
					return 0x02;
				} else if (linei[1][1] > linej[0][1]) {
					crossing.add(linej[0]);
					if (linei[1][0] >= linej[1][0]) {
						crossing.add(linej[1]);
						return 0x13;
					} else {
						crossing.add(linei[1]);
						return 0x23;
					}
				} else {
					return 0x00;
				}
			} else {
				return 0x10; // 没有交点平行
			}
		} else {// 不平行
			// begin 快速排斥实验
			double ymaxi, ymini;
			double ymaxj, yminj;
			if (linei[0][1] < linei[1][1]) {
				ymaxi = linei[1][1];
				ymini = linei[0][1];
			} else {
				ymini = linei[1][1];
				ymaxi = linei[0][1];
			}
			if (linej[0][1] < linej[1][1]) {
				ymaxj = linej[1][1];
				yminj = linej[0][1];
			} else {
				yminj = linej[1][1];
				ymaxj = linej[0][1];
			}
			if (linej[0][0] > linei[1][0] || ymaxj < ymini || yminj > ymaxi) {
				return 0x20;
			}
			// end

			// begin 跨立实验
			int flag1 = straddlesTest(linei, linej);
			int flag2 = straddlesTest(linej, linei);
			if (flag1 == 0 || flag2 == 0) {
				return 0x30; // 没有交点
			} else if (flag1 == 1 && flag2 == 1) {// 有交点 线段中间
				crossing.add(getCrossing(linej, linei));
				return 0x12;
			} else if (flag1 == 1 && flag2 == 2) {// linei 右端点
			//					crossing = linei[0];
				crossing.add(linei[0]);
				return 0x22;
			} else if (flag1 == 1 && flag2 == 3) {// linei 左端点
				crossing.add(linei[1]);
				return 0x32;
			} else if (flag1 == 2 && flag2 == 1) {// linej left
				crossing.add(linej[0]);
				return 0x42;
			} else if (flag1 == 2 && flag2 == 2) {// linei left point = linej
				// left point
				crossing.add(linej[0]);
				return 0x52;
			} else if (flag1 == 2 && flag2 == 3) {// linej left = linei right
				crossing.add(linei[1]);
				return 0x62;
			} else if (flag1 == 3 && flag2 == 1) {// linej right
				crossing.add(linej[1]);
				return 0x72;
			} else if (flag1 == 3 && flag2 == 2) {// linej right = linei left
				crossing.add(linej[1]);
				return 0x82;
			} else {// lienj right = linei right
				crossing.add(linej[1]);
				return 0x92;
			}
			// end

		}
	}

	/**
	 * This function assums that linei and linej have one crossing, without check
	 * @param linei
	 * @param linej
	 * @return
	 */
	public static double[] getCrossing(final double[][] linei,
			final double[][] linej) {
		double x1, x2, x3, x4;
		double y1, y2, y3, y4;
		x1 = linei[0][0];
		x2 = linei[1][0];
		x3 = linej[0][0];
		x4 = linej[1][0];
		y1 = linei[0][1];
		y2 = linei[1][1];
		y3 = linej[0][1];
		y4 = linej[1][1];
		double b1 = (y2 - y1) * x1 + (x1 - x2) * y1;
		double b2 = (y4 - y3) * x3 + (x3 - x4) * y3;
		double D = (x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1);
		double D1 = b2 * (x2 - x1) - b1 * (x4 - x3);
		double D2 = b2 * (y2 - y1) - b1 * (y4 - y3);
		double x0 = D1 / D;
		double y0 = D2 / D;
		return new double[] { x0, y0 };
	}

	// linej 两端点在linei 两侧（1）同侧（0） 某点在linei上0 左点（2） 右点（3）
	private static int straddlesTest(final double[][] linei, final double[][] linej) {
		double[][] v1, v2;
		v1 = new double[2][2];
		v2 = new double[2][2];
		v1[0] = linei[0];
		v2[0] = linei[0];
		v1[1] = linej[0];
		v2[1] = linej[1];
		double c1 = crossProduct(v1, linei);
		double c2 = crossProduct(v2, linei);
		if (Math.abs(c1) < exp) {
			return 2;
		} else if (Math.abs(c2) < exp) {
			return 3;
		} else if (c1 * c2 > 0) {
			return 0;
		} else {
			return 1;
		}
	}

	public static double crossProduct(double[][] vv1, double[][] vv2) {
		double v1x = vv1[1][0] - vv1[0][0];
		double v1y = vv1[1][1] - vv1[0][1];
		double v2x = vv2[1][0] - vv2[0][0];
		double v2y = vv2[1][1] - vv2[0][1];
		return v1x * v2y - v1y * v2x;
	}

	// end 计算交点
}
