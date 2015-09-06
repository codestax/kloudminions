package com.cm.minions;

import org.junit.Test

import com.cm.context.Context
import com.cm.context.MinionContext
import com.cm.exceptions.CloudProviderNotFoundException
import com.cm.providers.CloudProvider
import com.cm.providers.aws.AWSCloudProvider
import com.cm.test.CloudMinionsTestCase

public class AbstractCloudMinionTest extends CloudMinionsTestCase {
	
	
	@Test
	void testConstructor_1() {
		
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		/*
		 * Method under test
		 */
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		
		assertNotNull testCloudMinion
		
		Context mc = testCloudMinion.getContext()
		
		assertNotNull mc
		assertEquals ("AWS", mc.get("cloud_provider"))
		
	}
	
	/**
	 * Test to see if Minion is prevented from being instantiated with a null context
	 */
	@Test
	void testConstructor_IllegalArgument_1() {
		
		/*
		 * Method under test
		 */
		shouldFail(IllegalArgumentException) {
			TestCloudMinion testCloudMinion = new TestCloudMinion(null)
		}
		
	}
	
	
	/**
	 * Test to see if Minion is prevented from being instantiated without a context
	 */
	@Test
	void testConstructor_IllegalArgument_2() {
		
		/*
		 * Method under test
		 */
		shouldFail(IllegalArgumentException) {
			TestCloudMinion testCloudMinion = new TestCloudMinion()
		}
		
	}
	
	
	/**
	 * Test to see if Minion is prevented from being instantiated without a proper cloud provider specified
	 */
	@Test
	void testConstructor_IllegalArgument_3() {
		
		Context context = new Context([
			"cloud_provider" : "DOES NOT EXIST"
		])
		
		
		/*
		 * Method under test
		 */
		shouldFail(CloudProviderNotFoundException) {
			TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		}
		
	}
	
	
	@Test
	void testGetCloudProvider() {
		
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		
		/*
		 * Method under test
		 */
		CloudProvider cp = testCloudMinion.getCloudProvider();
		
		assertNotNull cp
		assertEquals (AWSCloudProvider.class, cp.class)
		
	}
	
	
	@Test
	void testGetContext() {
		
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		
		/*
		 * Method under test
		 */
		Context c = testCloudMinion.getContext();
		
		assertNotNull c
		assertEquals ("AWS", c.get("cloud_provider"))
		
	}
	
	
	@Test
	void testSetContext() {
		
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		assertNotNull testCloudMinion 
		
		Context c = testCloudMinion.getContext();
		assertNotNull c
		assertEquals ("AWS", c.get("cloud_provider"))
		
		
		Context newContext = new Context([
			"cloud_provider" : "AWS",
			"new_attrib" : "HELLO"
		])
		
		
		/*
		 * Method under test
		 */
		testCloudMinion.setContext(newContext)
		
		
		Context new_context = testCloudMinion.getContext();
		
		assertNotNull new_context
		assertEquals ("AWS", new_context.get("cloud_provider"))
		assertEquals ("HELLO", new_context.get("new_attrib"))
		
	}
	
	

	
} // END AbstractCloudMinionTest Class