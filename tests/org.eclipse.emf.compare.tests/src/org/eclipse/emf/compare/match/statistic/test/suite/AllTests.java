package org.eclipse.emf.compare.match.statistic.test.suite;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;

/**
 * Launch all the JUnit tests for EMF compare
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class AllTests extends TestCase implements IPlatformRunnable {
	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * 
	 * @return the testsuite containing all the tests
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(ExtensionTestSuite.suite());
		return suite;
	}

	/**
	 * initializer
	 * 
	 */
	public AllTests() {
		super(""); //$NON-NLS-1$
	}

	public Object run(Object args) throws Exception {
		TestRunner.run(suite());
		return Arrays
				.asList(new String[] { "Please see raw test suite output for details." }); //$NON-NLS-1$
	}

}