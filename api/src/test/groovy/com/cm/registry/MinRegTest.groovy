package com.cm.registry

import static org.junit.Assert.*

import org.junit.Test

import com.cm.context.MinionContext
import com.cm.minions.TestCloudMinion
import com.cm.test.CloudMinionsTestCase

class MinRegTest extends CloudMinionsTestCase{
	
	
	@Test
	void test_get_1() {
		
		/*
		 * Method under test
		 */
		String minionClassName = MinReg.get("test_minion")
		
		
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
		
	}
	
	
	@Test
	void test_add_String_String_1() {
		
		/*
		 * Method under test
		 */
		MinReg.add("a_new_minion", "com.cm.minions.ANewMinion")
		
		String minionClassName = MinReg.get("a_new_minion")
		assertEquals("com.cm.minions.ANewMinion", minionClassName)
	}
	
	
	@Test
	void test_add_String_String_shouldFail_1() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add(null, "com.cm.minions.ANewMinion")
		}
	}
	
	@Test
	void test_add_String_String_shouldFail_2() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("", "com.cm.minions.ANewMinion")
		}
	}
	
	
	@Test
	void test_add_String_String_shouldFail_3() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("    ", "com.cm.minions.ANewMinion")
		}
	}
	
	
	@Test
	void test_add_String_String_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("a_new_minion", null)
		}
	}
	
	@Test
	void test_add_String_String_shouldFail_5() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("a_new_minion", "")
		}
	}
	
	@Test
	void test_add_String_String_shouldFail_6() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("a_new_minion", "   ")
		}
	}
	
	
	@Test
	void test_add_String_Class_1() {
		
		
		/*
		 * Method under test
		 */
		MinReg.add("a_new_minion", com.cm.minions.TestCloudMinion)
		
		String minionClassName = MinReg.get("a_new_minion")
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
	}
	
	
	@Test
	void test_add_String_Class_shouldFail_1() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add(null, com.cm.minions.TestCloudMinion)
		}
	}
	
	@Test
	void test_add_String_Class_shouldFail_2() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("", com.cm.minions.TestCloudMinion)
		}
	}
	
	
	@Test
	void test_add_String_Class_shouldFail_3() {
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("    ", com.cm.minions.TestCloudMinion)
		}
	}
	
	
	@Test
	void test_add_String_Class_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("a_new_minion", null)
		}
	}
	
	
	@Test
	void test_add_String_Minion_1() {
		
		/*
		 * Fixture
		 */
		MinionContext minionContext = new MinionContext([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(minionContext)
		
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		MinReg.add("a_new_minion", testCloudMinion)
		
		String minionClassName = MinReg.get("a_new_minion")
		assertEquals("com.cm.minions.TestCloudMinion", minionClassName)
	}
	
	@Test
	void test_add_String_Minion_shouldFail_1() {
		
		/*
		 * Fixture
		 */
		MinionContext minionContext = new MinionContext([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(minionContext)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add(null, testCloudMinion)
		}
	}
	
	@Test
	void test_add_String_Minion_shouldFail_2() {
		
		/*
		 * Fixture
		 */
		MinionContext minionContext = new MinionContext([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(minionContext)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("", testCloudMinion)
		}
	}
	
	
	@Test
	void test_add_String_Minion_shouldFail_3() {
		
		/*
		 * Fixture
		 */
		MinionContext minionContext = new MinionContext([
			"cloud_provider" : "AWS"
		])
		
		TestCloudMinion testCloudMinion = new TestCloudMinion(minionContext)
		assertNotNull testCloudMinion
		
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("    ", testCloudMinion)
		}
	}
	
	
	@Test
	void test_add_String_Minion_shouldFail_4() {
		
		/*
		 * Method under test
		 */
		shouldFail (IllegalArgumentException) {
			MinReg.add("a_new_minion", null)
		}
	}
	
} // END MinRegTest Class
