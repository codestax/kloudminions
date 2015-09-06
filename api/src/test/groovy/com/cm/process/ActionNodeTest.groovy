package com.cm.process

import org.junit.Test

import com.cm.context.Context
import com.cm.context.ProcessContext
import com.cm.test.CloudMinionsTestCase

class ActionNodeTest extends CloudMinionsTestCase {


	@Test
	void testCreateInstance_1() {

		def actionNodeSpecObj = [
			type: "action",
			action: "com.cm.process.TestCloudActionNodeMinion:doSomething",
			something: "extra"
		] 

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])

		Process process = new Process(actionNodeSpecObj, context)

		/*
		 * Method Under Test
		 */

		def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)

		assertNotNull(actionNode)
		assertEquals("action", actionNode.getTypeDefinition())
		assertEquals("com.cm.process.TestCloudActionNodeMinion", actionNode.getMinionClassName())
		assertEquals("doSomething", actionNode.getMinionActionName())
		assertEquals("com.cm.process.TestCloudActionNodeMinion", actionNode.getMinionObject().class.name)
		assertEquals("AWS", actionNode.getMinionObject().getContext().get("cloud_provider"))
		assertEquals("extra", actionNode.getNodeAttributes().get("something"))
		

	} // END testCreate_1 Method

	
	@Test
	void testCreateInstance_2() {

		def actionNodeSpecObj = [
			type: "action",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])

		Process process = new Process(actionNodeSpecObj, context)
		
		
		/*
		 * Method Under Test
		 */

		def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)

		assertNotNull(actionNode)
		assertEquals("action", actionNode.getTypeDefinition())
		assertEquals("com.cm.process.TestCloudActionNodeMinion", actionNode.getMinionClassName())
		assertEquals("doSomething", actionNode.getMinionActionName())
		assertEquals("com.cm.process.TestCloudActionNodeMinion", actionNode.getMinionObject().class.name)
		assertEquals("AWS", actionNode.getMinionObject().getContext().get("cloud_provider"))


	} // END testCreate_2 Method

	
	@Test
	void testCreateInstance_shouldFail_1_type_empty_string() {

		def actionNodeSpecObj = [
			type: "",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END testCreateInstance_shouldFail_1_type_empty_string Method
	
	
	@Test
	void testCreateInstance_shouldFail_1_type_null() {

		def actionNodeSpecObj = [
			type: null,
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END testCreateInstance_shouldFail_1_type_null Method
	
	
	@Test
	void testCreateInstance_shouldFail_1_type_all_spaces() {

		def actionNodeSpecObj = [
			type: "     ",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END testCreateInstance_shouldFail_1_type_all_spaces Method

	
	@Test
	void testCreateInstance_shouldFail_type_notequal_action_1() {

		def actionNodeSpecObj = [
			type: "ACTion",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	@Test
	void testCreateInstance_shouldFail_type_notequal_action_2() {

		def actionNodeSpecObj = [
			type: "sequence",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	@Test
	void testCreateInstance_shouldFail_action_is_null() {

		def actionNodeSpecObj = [
			type: "action",
			action: null
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_action_is_empty_string() {

		def actionNodeSpecObj = [
			type: "action",
			action: ""
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_action_is_all_spaces() {

		def actionNodeSpecObj = [
			type: "action",
			action: "    "
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])


		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testCreateInstance_shouldFail_context_is_null() {

		def actionNodeSpecObj = [
			type: "action",
			action: "test_action_cloud_minion:doSomething"
		]

		ProcessContext context = null

		/*
		 * Method Under Test
		 */
		shouldFail (IllegalArgumentException) {
			def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		}

	} // END Method
	
	
	@Test
	void testExecute_1() {

		def actionNodeSpecObj = [
			type: "action",
			action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])
		
		Process process = new Process(actionNodeSpecObj, context)
		
		def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		assertNotNull(actionNode)

		/*
		 * Method Under Test
		 */
		actionNode.execute()
		
		Context newContext = actionNode.getContext()
		
		assertEquals("Cool!", newContext.get("didSomething"))
		
	} // END testExecute_1 Method
	
	
	@Test
	void testExecute_2() {

		def actionNodeSpecObj = [
			type: "action",
			action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
		]

		ProcessContext context = new ProcessContext([
			"cloud_provider" : "AWS"
		])
		
		Process process = new Process(actionNodeSpecObj, context)
		
		def actionNode = ActionNode.createInstance(actionNodeSpecObj, context)
		assertNotNull(actionNode)

		/*
		 * Method Under Test
		 */
		actionNode.execute()
		
		Context newContext = actionNode.getContext()
		
		assertEquals("Hello!", newContext.get("saidSomething"))
		
	} // END testExecute_2 Method
	
} // END ActionNodeTest Class
