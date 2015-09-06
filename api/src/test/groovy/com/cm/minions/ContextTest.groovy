package com.cm.minions

import static org.junit.Assert.*

import org.junit.Test

import com.cm.context.Context;
import com.cm.test.CloudMinionsTestCase

class ContextTest extends CloudMinionsTestCase {

	@Test
	void test_Context_empty_constructor_empty_context() {
		
		/*
		 * Method under test
		 */
		Context context = new Context()
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(0, contextMap.size())
	}


	@Test
	void test_Context_constructor_with_context_1() {
		
		HashMap testMap = [
			a: 45,
			b: "hello"
		]
		
		/*
		 * Method under test
		 */
		Context context = new Context(testMap)
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(2, contextMap.size())
		assertEquals(45, contextMap.get("a"))
		assertEquals("hello", contextMap.get("b"))
	}
	
	
	@Test
	void test_Context_add_existing_context_1() {
		
		HashMap testMap = [
			a: 45,
			b: "hello"
		]
		Context context = new Context(testMap)
		
		/*
		 * Method under test
		 */
		context.add("c", 23)
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(3, contextMap.size())
		assertEquals(45, contextMap.get("a"))
		assertEquals("hello", contextMap.get("b"))
		assertEquals(23, contextMap.get("c"))
	}
	
	
	@Test
	void test_Context_add_existing_context_2() {
		
		HashMap testMap = [
			a: 45,
			b: "hello"
		]
		Context context = new Context(testMap)
		
		/*
		 * Method under test
		 */
		context.add("b", 23)
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(2, contextMap.size())
		assertEquals(45, contextMap.get("a"))
		assertEquals(23, contextMap.get("b"))
	}
	
	
	@Test
	void test_Context_add_empty_context_1() {
		
		
		Context context = new Context()
		
		/*
		 * Method under test
		 */
		context.add("c", 23)
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(1, contextMap.size())
		assertEquals(23, contextMap.get("c"))
	}
	
	
	@Test
	void test_Context_get_existing_context_1() {
		
		HashMap testMap = [
			a: 45,
			b: "hello"
		]
		Context context = new Context(testMap)
		
		/*
		 * Method under test
		 */
		def result = context.get("a")
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(2, contextMap.size())
		assertEquals(45, result)
	}
	
	
	@Test
	void test_Context_get_existing_context_2() {
		
		HashMap testMap = [
			a: 45,
			b: "hello"
		]
		Context context = new Context(testMap)
		
		/*
		 * Method under test
		 */
		def result = context.get("k")
		
		
		Map contextMap = context.getContextMap()
		assertNotNull(contextMap)
		assertEquals(2, contextMap.size())
		assertNull(result)
	}
	
	
	
} /// END ContextTest Class
