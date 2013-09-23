package org.fzhen.emulator.component.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.fzhen.emulator.component.util.LineCrossing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value=Parameterized.class)
public class LineCrossingTest {
	private double[][] v1, v2;
	private double[] ce;
	private double ve;
	private int res;
/*	
	@Parameter(2)
	public double[] ce;
	@Parameter(3)
	public double ve;
	*/
	
	public LineCrossingTest(double[][] v1, double[][] v2, double[] ce, double ve, int res) {
		this.v1 = v1;
		this.v2 = v2;
		this.ce = ce;
		this.ve = ve;
		this.res = res;
	}
	@Parameters
	public static Collection<Object[]> getTestParameters(){
		return Arrays.asList(new Object[][]{
				//TODO 数组初始化 问题
				{
					new double[][]{{0.3, 0.5},{3,4}}, 
					new double[][]{{0.3, 0.5},{4,5}}, 
					new double[]{0.3,0.5},
					-0.8,
					0x52
					},
				{
					new double[][]{{0, 0},{3,3}}, 
					new double[][]{{0,3},{3,0}}, 
					new double[]{1.5,1.5},
					-18,
					0x12
					}
		});
	}
	@Test
	public void testGetCrossing(){
		List<double[]> c = new ArrayList<double[]>(); 
		int r = LineCrossing.getCrossing(v1, v2, c);
		assertEquals(res, r, 0);
		assertArrayEquals(ce, c.get(0), 1e-5);
	}
	@Test
	public void testCrossProduct() {
		assertEquals(ve, LineCrossing.crossProduct(v1, v2), 1e-5); 
	}

}
