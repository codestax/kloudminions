package com.cm.process

import static org.junit.Assert.*

import org.junit.Test

import com.cm.context.ProcessContext
import com.cm.test.CloudMinionsTestCase

class ProcessNodeTest extends CloudMinionsTestCase {

	
	@Test
	void testCreateInstance_1() {

		def processNodeSpecObj = [
			type: "sequence",
			something: "extra",
			nodes: [[
					type: "action",
					action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
				],[
					type: "action",
					action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
				]]
		] // END processNodeSpecObj
		
		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])
		
		Process process = new Process(processNodeSpecObj, context)
		
		/*
		 * Method Under Test
		 */
		
		def pn = ProcessNode.createInstance(processNodeSpecObj, context)		
		assertNotNull(pn)

		
		assertNotNull(pn)
		assertEquals("sequence", pn.getTypeDefinition())
		assertEquals(2, pn.getNodeList().size())
		
		ProcessNode child_pn1 = pn.getNodeList().get(0)
		assertEquals("action", child_pn1.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", child_pn1.class.name)
		
		ProcessNode child_pn2 = pn.getNodeList().get(1)
		assertEquals("action", child_pn2.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", child_pn2.class.name)
		
		assertEquals("AWS", pn.getContext().get("cloud_provider"))
		assertEquals("extra", pn.getNodeAttributes().get("something"))

		
	} // END testCreate_1 Method
	
	
	
	
} // END ProcessNodeTest Class
