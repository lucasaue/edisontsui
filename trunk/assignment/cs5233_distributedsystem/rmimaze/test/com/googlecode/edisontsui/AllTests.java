package com.googlecode.edisontsui;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.googlecode.edisontsui");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestMazeElement.class);
		suite.addTestSuite(TestPlayer.class);
		suite.addTestSuite(TestMaze.class);
		//$JUnit-END$
		return suite;
	}

}
