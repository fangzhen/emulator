package org.fzhen.emulator.component.mFlc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TrimWrapperTest {

	private TrimWrapper tw;
	
	@Before
	public void init() {
		tw = new TrimWrapper(0,1,2);
	}

	@Test
	public void testGetMembership() {
		double ms = tw.getMembership(1.5);
		assertEquals("diffent ", ms, 0.5, 0);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetRegion() {
		double[][] expect={{0,0}, {0.5, 0.5}, {1.5,0.5}, {2,0}};
		double[][] actual = tw.getRegion(0.5);
		assertEquals(expect, actual);
	}

}
