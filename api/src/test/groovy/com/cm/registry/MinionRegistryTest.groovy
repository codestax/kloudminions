package com.cm.registry

import static org.junit.Assert.*

import org.junit.Test

import com.cm.context.Context
import com.cm.minions.TestCloudMinion
import com.cm.test.CloudMinionsTestCase

class MinionRegistryTest extends CloudMinionsTestCase{

	@Test
	void test_GetMinion_1() {
		
		/*
		 * Method under test
		 */
		String minionClassName = MinionRegistry.getMinionClassName("test_minion")
		
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_1() {
		
		/*
		 * Method under test
		 */
		MinionRegistry.registerMinionType("a_new_minion", "com.cm.minions.ANewMinion")
		
		String minionClassName = MinionRegistry.getMinionClassName("a_new_minion")
		assertEquals("com.cm.minions.ANewMinion", minionClassName)
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_1() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType(null, "com.cm.minions.ANewMinion")
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_2() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("", "com.cm.minions.ANewMinion")
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_3() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("    ", "com.cm.minions.ANewMinion")
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("a_new_minion", null)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_5() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("a_new_minion", "")
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_String_shouldFail_6() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("a_new_minion", "   ")
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Class_1() {
		
		
		/*
		 * Method under test
		 */
		MinionRegistry.registerMinionType("a_new_minion", com.cm.minions.TestCloudMinion)
		
		String minionClassName = MinionRegistry.getMinionClassName("a_new_minion")
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
	}
	
	
	@Test
	void test_RegisterMinionType_String_Class_shouldFail_1() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType(null, com.cm.minions.TestCloudMinion)
		}
	}
	
	@Test
	void test_RegisterMinionType_String_Class_shouldFail_2() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("", com.cm.minions.TestCloudMinion)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Class_shouldFail_3() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("    ", com.cm.minions.TestCloudMinion)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Class_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("a_new_minion", null)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Minion_1() {
		
		/*
		 * Fixture
		 */
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		MinionRegistry.registerMinionType("a_new_minion", testCloudMinion)
		
		String minionClassName = MinionRegistry.getMinionClassName("a_new_minion")
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
	}
	
	
	@Test
	void test_RegisterMinionType_String_Minion_shouldFail_1() {
		
		/*
		 * Fixture
		 */
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType(null, testCloudMinion)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Minion_shouldFail_2() {
		
		/*
		 * Fixture
		 */
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("", testCloudMinion)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Minion_shouldFail_3() {
		
		/*
		 * Fixture
		 */
		Context context = new Context([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(context)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("    ", testCloudMinion)
		}
	}
	
	
	@Test
	void test_RegisterMinionType_String_Minion_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinionRegistry.registerMinionType("a_new_minion", null)
		}
	}
	
} // END MinionRegistryTest Class
