package com.cm.process

import org.junit.Test

import com.cm.context.Context
import com.cm.context.ProcessContext
import com.cm.test.CloudMinionsTestCase

class SequenceNodeTest extends CloudMinionsTestCase {
	
	
	@Test
	void testCreateInstance_1() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			something: "extra",
			nodes: []
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])

		Process process = new Process(sequenceNodeSpecObj, context)
		
		/*
		 * Method Under Test
		 */
		SequenceNode sequenceNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)

		assertNotNull(sequenceNode)
		assertEquals("sequence", sequenceNode.getTypeDefinition())
		assertEquals(0, sequenceNode.getNodeList().size())
		assertEquals("AWS", sequenceNode.getContext().get("cloud_provider"))
		assertEquals("extra", sequenceNode.getNodeAttributes().get("something"))

	} // END testCreate_1 Method


	@Test
	void testCreateInstance_2() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			something: "extra",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])

		Process process = new Process(sequenceNodeSpecObj, context)

		/*
		 * Method Under Test
		 */
		SequenceNode sequenceNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)

		assertNotNull(sequenceNode)
		assertEquals("sequence", sequenceNode.getTypeDefinition())
		assertEquals(2, sequenceNode.getNodeList().size())
		
		ProcessNode pn1 = sequenceNode.getNodeList().get(0)
		assertEquals("action", pn1.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", pn1.class.name)
		
		ProcessNode pn2 = sequenceNode.getNodeList().get(1)
		assertEquals("action", pn2.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", pn2.class.name)
		
		assertEquals("AWS", sequenceNode.getContext().get("cloud_provider"))
		assertEquals("extra", sequenceNode.getNodeAttributes().get("something"))

	} // END testCreate_2 Method

	
	@Test
	void testCreateInstance_shouldFail_1_type_empty_string() {

		def sequenceNodeSpecObj = [
			type: "",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END testCreateInstance_shouldFail_1_type_empty_string Method
	
	
	@Test
	void testCreateInstance_shouldFail_1_type_null() {
		
		def sequenceNodeSpecObj = [
			type: null,
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}
		
	} // END testCreateInstance_shouldFail_1_type_null Method
	
	
	@Test
	void testCreateInstance_shouldFail_1_type_all_spaces() {

		def sequenceNodeSpecObj = [
			type: "     ",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END testCreateInstance_shouldFail_1_type_all_spaces Method

	
	@Test
	void testCreateInstance_shouldFail_type_notequal_sequence_1() {

		def sequenceNodeSpecObj = [
			type: "SEQuence",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_type_notequal_action_2() {

		def sequenceNodeSpecObj = [
			type: "action",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_nodes_is_null() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			nodes: null
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_nodes_is_not_a_list_or_array() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			nodes: ""
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_context_is_null() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			nodes: []
		]

		ProcessContext context = null


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testExecute_1() {

		def sequenceNodeSpecObj = [
			type: "sequence",
			something: "extra",
			nodes: [[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
			],[
				type: "action",
				action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
			]]
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])
		
		Process process = new Process(sequenceNodeSpecObj, context)
		
		SequenceNode sequenceNode = SequenceNode.createInstance(sequenceNodeSpecObj, context)
		assertNotNull(sequenceNode)
		
		/*
		 * Method Under Test
		 */
		sequenceNode.execute()
		
		
		assertEquals("sequence", sequenceNode.getTypeDefinition())
		assertEquals(2, sequenceNode.getNodeList().size())
		
		ProcessNode pn1 = sequenceNode.getNodeList().get(0)
		assertEquals("action", pn1.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", pn1.class.name)
		assertEquals("Cool!", pn1.getContext().get("didSomething"))
		
		ProcessNode pn2 = sequenceNode.getNodeList().get(1)
		assertEquals("action", pn2.getTypeDefinition())
		assertEquals("com.cm.process.ActionNode", pn2.class.name)
		assertEquals("Hello!", pn2.getContext().get("saidSomething"))
		
	} // END testExecute_1 Method
	
	
} // END SequenceNodeTest Class
