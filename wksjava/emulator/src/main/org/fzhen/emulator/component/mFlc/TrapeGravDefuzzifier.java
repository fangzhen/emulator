package org.fzhen.emulator.component.mFlc;

import java.util.ArrayList;

import static org.fzhen.emulator.component.util.LineCrossing.crossProduct;
import static org.fzhen.emulator.component.util.LineCrossing.getCrossing;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fzhen.emulator.component.util.LineCrossing;

public class TrapeGravDefuzzifier implements Defuzzifier {

	List<double[][]> regions = null;
	List<double[][]> lines;
	private final double exp=1e-9;

	public void setRegions(List regions) {
		this.regions = regions;
	}

	/**
	 * 重心法去模糊
	 */
	@Override
	public double defuzzify() {
		if (regions == null)
			return Double.NaN;
		generateAndSortLines();
		int i, j;
		int len = lines.size();
		List<double[]> crossing = new ArrayList<double[]>();
		double[] tCrossing = new double[4];
		int flag; // 返回标志
		double[][] linei, linej;

		List<double[]>[] crossings;

		//计算所有线段之间的交点,把所有线段划分成没有中间交点的线段。
		crossings = (List<double[]>[]) new ArrayList[lines.size()];
		for (i = 0; i < len; i++){
			crossings[i] = new ArrayList<double[]>();
		}
		for (i = 0; i < len; i++) {
			linei = lines.get(i);
			crossings[i].add(linei[0]);
			crossings[i].add(linei[1]);
			for (j = i + 1; j < len; j++) {
				linej = lines.get(j);
				if (lines.get(j)[0][0] >= lines.get(i)[1][0])// x坐标没有重叠 不会相交 或不会产生新线段
					break;
				crossing = new ArrayList<double[]>();
				flag = getCrossing(linei, linej, crossing);

				if (flag == 0x12) {// 普通交点
					crossings[i].add(crossing.get(0));
					crossings[j].add(crossing.get(0));
				} else if (flag == 0x13) {//linei 包含linej
					if (linei[0][0] < crossing.get(0)[0]) {
						crossings[i].add(crossing.get(0));
					}
					if (linei[1][0] > tCrossing[2]) {
						crossings[i].add(crossing.get(1));
					}
				} else if (flag == 0x23) {//部分重合
					if (linei[0][0] < tCrossing[0]) {
						crossings[i].add(crossing.get(0));
					}
					if (linej[1][0] > tCrossing[2]) {
						crossings[i].add(crossing.get(1));
					}

				}else if (flag ==0x32){//linei 左
					crossings[j].add(crossing.get(0));
				}else if (flag == 0x22){//交于linei右端点
					crossings[j].add(crossing.get(0));
				}else if (flag == 0x42){//交于linej 左端点
					crossings[i].add(crossing.get(0));
				}else if (flag ==0x72){//linej 右
					crossings[i].add(crossing.get(0));
				}
			}
		}
		Comparator<double[]> pointComparator = new Comparator<double[]>() {
			public int compare(double[] a, double[] b) {
				if (a[0] > b[0])
					return 1;
				else if (a[0] == b[0])
					return 0;
				else
					return -1;
			}
		};
		//原线段每个对应一个新region--并不闭合
		List<double[][]> newRegion = new ArrayList<double[][]>();
		for (i = 0; i < crossings.length; i++) {
			Collections.sort(crossings[i], pointComparator);
			/*
			 * TODO
			 * 下面调用不成功 类型强制转换有问题
			 * newRegion.add((double[][])crossings[i].toArray());
			 */
			newRegion.add(crossings[i].toArray(new double[0][0]));
		}
		generateAndSortLines(newRegion);
		// 删除内部边
		for (i = 0; i < lines.size(); i++) {
			if (insideTest(i) == 1) {
				lines.remove(i);
				i--;
			}
		}
		// 再次按x坐标排序
		sortLines(lines);

		// 划分成图形 计算重心
		double sumAU = 0, sumA = 0;
		len = lines.size();
		for (i = 0; i < len; i++) {
			double graY = gravityCenter(lines.get(i))[0];
			double a = area(lines.get(i));
			sumA += a;
			sumAU += a * graY;
		}
		return sumAU / sumA;
	}
	/**
	 * 计算line 和它到x轴投影围成的梯形的面积
	 * @param line
	 * @return
	 */
	private double area(double[][] line) {
		double x1, x2, y1, y2;
		x1 = line[0][0];
		y1 = line[0][1];
		x2 = line[1][0];
		y2 = line[1][1];
		return (y1 + y2) * (x2 - x1) / 2;
	}

	/**
	 * 计算line 和它到x轴投影围成的梯形的重心
	 * @param line
	 * @return
	 */
	private double[] gravityCenter(double[][] line) {
		double x1, x2, y1, y2;
		x1 = line[0][0];
		y1 = line[0][1];
		x2 = line[1][0];
		y2 = line[1][1];
		if (y1== y2 && y1 == 0){//it should not be true
			return new double[]{(x1+x2)/2, 0};
		}
		double yp = y1 / 2;
		double yq = y2 / 2;
		double ye = y1 + y2;
		double yr = -y1;
		return LineCrossing.getCrossing(
				new double[][] { { x1, yp }, { x2, yq } }, new double[][] {
						{ x1, ye }, { x2, yr } });
	}

	/**
	 * 从每条规则产生的区域生成整个区域的外围线段
	 */
	private void generateAndSortLines(List<double[][]> regions) {
		lines = new ArrayList<double[][]>();
		double[][] line;
		for (double[][] points : regions) {
			int len = points.length;
			for (int i = 0; i < len -1; i++) {
				line = new double[2][];
				line[0] = points[i];
				line[1] = points[i + 1];
//				if (!Arrays.equals(line[0], line[1]))
//					lines.add(line);
				if (!(line[1][0] - line[0][0] < exp && line[1][1] -line[0][1] < exp)){
					lines.add(line);
				}
			}
		}
		sortLines(lines);
	}

	/**
	 * 按线段左点的x坐标排序 假定line[0] is the left point
	 */
	private void sortLines(List<double[][]> lines) {
		Comparator<double[][]> pointComparator = new Comparator<double[][]>() {
			public int compare(double[][] a, double[][] b) {
				if (a[0][0] > b[0][0])
					return 1;
				else if (a[0][0] == b[0][0])
					return 0;
				else
					return -1;
			}
		};
		Collections.sort(lines, pointComparator);
	}

	/**
	 * 测试某线段是否在围成的区域内部
	 * @param ind 线段下标
	 * @return
	 */
	private int insideTest(int ind) {
		double[][] line = lines.get(ind);
		double x1, x2, y1, y2, xm, ym;
		x1 = line[0][0];
		y1 = line[0][1];
		x2 = line[1][0];
		y2 = line[1][1];
		xm = (x1 + x2) / 2;
		ym = (y1 + y2) / 2;
		double[] pointm = new double[] { xm, ym };
		int r0, r1;
		r0 = pointInsideTest(line[0], ind);
		r1 = pointInsideTest(line[1], ind);
		if (r0 < 0 || r1 < 0) {
			return -1;
		} else if (r0 > 0 || r1 > 0) {
			return 1; //内部
		} else if (pointInsideTest(pointm, ind) >= 0) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * 测试某点是不是在整个围成的区域内部，
	 * @param point 要测试的点
	 * @param ind 点所在线段的下标 若不是线上的点，可传-1
	 * @return
	 */
	private int pointInsideTest(double point[], int ind) {
		int i;
		int len = lines.size();
		int r1;
		r1 = -1;
		for (i = 0; i < len; i++) {
			if (i == ind)
				continue;
			double[][] linei = lines.get(i);
			// int r1 = below(line[0], lines.get(i));
			// int r2 = below(line[1], lines.get(i));
			int tr = below(point, lines.get(i));
			if (tr > 0) {
				r1 = 1;// the point is below the line
				break;
			} else if (tr == 0) {
				r1 = 0;// point on the line
			}
		}
		return r1;
	}

	/**
	 * line 都是line[0]is left point, line[1] right, means line[0][0] <=
	 * line[1][0]
	 * 
	 * @param p
	 * @param ln
	 * @return
	 */
	private int below(double[] p, double[][] ln) {
		int x0, y0, x1, y1, x2, y2;
		if (p[0] < ln[0][0]+exp || p[0] > ln[1][0]-exp) {
			return -2;
		}
		if (ln[0][0] == ln[1][0]) {
			return -3;
		}
		double[][] v2 = new double[2][2];
		v2[0] = ln[0];
		v2[1] = p;
		double c = crossProduct(v2, ln);
		if (c > exp)
			return 1; // point is below the line
		else if (c < -exp)
			return -1;// over
		else 
			return 0;// on
	}

	private void generateAndSortLines() {
		generateAndSortLines(regions);
	}

}
