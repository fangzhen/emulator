package org.fzhen.emulator.component.mFlc;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TrapeGravDefuzzifierTest {
	private TrapeGravDefuzzifier defuzzifier;
	private Map<String, Method> privateMethods;
	Class c;
	@Before
	public void init(){
		 defuzzifier = new TrapeGravDefuzzifier();
		 c = defuzzifier.getClass();
		 Method[] methods = c.getDeclaredMethods();
		 privateMethods = new HashMap<String, Method>();
		 for (Method method : methods){
			 if (Modifier.isPrivate(method.getModifiers()) == true){
				 method.setAccessible(true);
				 privateMethods.put(method.getName(), method);
			 }
		 }
		 List<double[][]> regions = new ArrayList<double[][]>();
		 regions.add(new double[][]{{0,0}, {0.5,0.5}, {1,0.5}, {1.5,0}});
		 regions.add(new double[][]{{1,0}, {1.5,0.5}, {2,0.5}, {2.5,0}});
		 defuzzifier.setRegions(regions);
	}
	
	private Object invokeMethod(String methodName, Object... params){
		Method method = privateMethods.get(methodName);
		try {
			return method.invoke(defuzzifier, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test
	public void testDefuzzify() {
		double output = defuzzifier.defuzzify();
		double oe = 1.25;
		assertEquals(oe, output, 1e-9);
	}
	
	@Test
	public void testGenerateAndSortLines(){
		//tested via debug..
		invokeMethod("generateAndSortLines");
	}

	@Test
	public void testGravityCenter() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		double[][] line = new double[][] { { 0,4 }, { 4, 0 } };
		double[] center = (double[])invokeMethod("gravityCenter", line);
		double[] centerE = new double[]{4.0/3, 4.0/3};
		assertArrayEquals(centerE, center, 1e-9);
	}

	/*
	@Test
	public void testDefuzzify() {
		fail("Not yet implemented");
	}

	@Test
	public void testDefuzzify() {
		fail("Not yet implemented");
	}

	@Test
	public void testDefuzzify() {
		fail("Not yet implemented");
	}
*/
}
