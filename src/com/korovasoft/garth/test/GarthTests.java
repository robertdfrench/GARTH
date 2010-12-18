package com.korovasoft.garth.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GarthTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(GarthTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(CachingPerformanceTest.class);
		suite.addTestSuite(TestForgetfulHash.class);
		//$JUnit-END$
		return suite;
	}

}
