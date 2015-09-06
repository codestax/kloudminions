package com.cm.context

import static org.junit.Assert.*;

import org.junit.Test;

import com.cm.test.CloudMinionsTestCase

class ProcessContextTest extends CloudMinionsTestCase {


	@Test
	void testGet_1() {
		
		Map map = new HashMap([a:1, b:2, c:3])
		
		ProcessContext pc = new ProcessContext(map)
		
		
		/*
		 * Method under test
		 */
		def value = pc.get("a")
		
		assertEquals(1, value)
		
	}
	
	@Test
	void testGet_With_Ref_in_Map_1() {
		
		Map map = new HashMap([a:1, b:2, c:["ref": "b"]])
		
		ProcessContext pc = new ProcessContext(map)
		
		
		/*
		 * Method under test
		 */
		def value = pc.get("c")
		
		assertEquals(2, value)
		
	}
	
	@Test
	void testGet_With_Ref_in_Map_2() {
		
		Map map = new HashMap([a:1, b:2, c:["ref": "b"], d: [g : 3, h: 4]])
		
		ProcessContext pc = new ProcessContext(map)
		
		
		/*
		 * Method under test
		 */
		def value = pc.get("c")
		
		assertEquals(2, value)
		
	}
	
	
	@Test
	void testGet_With_Ref_in_Map_3() {
		
		Map map = new HashMap([a:1, b:2, c:["ref": "b"], d: [g : 3, h: 4]])
		
		ProcessContext pc = new ProcessContext(map)
		
		
		/*
		 * Method under test
		 */
		def value = pc.get("d")
		
		assertEquals(3, value.g)
		assertEquals(4, value.h)
		
	}

}
