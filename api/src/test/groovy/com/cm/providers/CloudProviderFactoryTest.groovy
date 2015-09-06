package com.cm.providers

import org.junit.Test

import com.cm.context.Context;
import com.cm.exceptions.CloudProviderNotFoundException
import com.cm.providers.aws.AWSCloudProvider
import com.cm.test.CloudMinionsTestCase

class CloudProviderFactoryTest extends CloudMinionsTestCase {

	
	@Test
	void testGetInstance_Context_1() {
		
		
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		/*
		 * Method under test
		 */
		CloudProvider cp = CloudProviderFactory.getInstance(context)
		
		assertNotNull cp
		assertEquals (AWSCloudProvider.class, cp.class)
		
	}

	@Test
	void testGetInstance_Context_IllegalArgument_1() {
		
		
		Context context = new Context()
		
		/*
		 * Method under test
		 */
		shouldFail(IllegalArgumentException) {
			CloudProvider cp = CloudProviderFactory.getInstance(context)
		}
		
	}
	
	
	@Test
	void testGetInstance_Context_IllegalArgument_2() {
		
		/*
		 * Method under test
		 */
		shouldFail(IllegalArgumentException) {
			CloudProvider cp = CloudProviderFactory.getInstance(null)
		}
		
	}

	@Test
	void testGetInstance_Context_CloudProviderNotFound() {
		
		
		Context context = new Context([
			"cloud_provider" : "DOES NOT EXIST"
		])
		
		
		/*
		 * Method under test
		 */
		shouldFail(CloudProviderNotFoundException) {
			CloudProvider cp = CloudProviderFactory.getInstance(context)
		}
		
	}
	
	
} // END CloudProviderFactoryTest Class
