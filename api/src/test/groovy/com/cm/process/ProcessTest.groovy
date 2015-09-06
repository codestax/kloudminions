package com.cm.process

import static org.junit.Assert.*
import groovy.json.JsonSlurper;

import org.junit.Ignore;
import org.junit.Test

import com.cm.context.Context
import com.cm.context.ProcessContext
import com.cm.test.CloudMinionsTestCase
import com.cm.utils.MinionUtils;

class ProcessTest extends CloudMinionsTestCase {
	
	@Test
	void testExecute_UsingFiles() {
		
		JsonSlurper jsonSlurper = new JsonSlurper()
		
		String processSpecString = """
			{
				"type": "sequence",
				"nodes": [{
						"type": "action",
						"action": "iac_minion:processArchiveFiles",
						"iac.archive_path": "src/test/resources/test_files/archive1",
						"iac.archive_target_env": "ci"
					},{
						"type": "action",
						"action": "s3_minion:addFilesToBucket",
						"file_names": {"ref": "iac.puppet_package_files"},
						"bucket_name": "puppetmodules",
						"create_if_not_exist" : "true"
					},{
						"type": "action",
						"action": "cloud_formation_minion:deleteStack"
					},{
						"type": "action",
						"action": "cloud_formation_minion:createStack",
						"cf_script_filename": {"ref": "iac.cloudformation_template_path_and_file"}
					}]
			}
			"""
		Map processSpecMap = jsonSlurper.parseText(processSpecString)
		
		
//		{
//			"type": "action",
//			"action": "cloud_formation_minion:deleteStack"
//		},{
//			"type": "action",
//			"action": "cloud_formation_minion:createStack",
//			"cf_script_filename": {"ref": "iac.cloudformation_template_path_and_file"}
//		},
		
		/**
		 * Get the users Test AWS Credentials from their file
		 */
		String awsCredentialsPointerFile = MinionUtils.TEST_AWS_CREDENTIALS_POINTER_FILE
		Map awsCredentialsPointerMap = MinionUtils.readJsonFromFile(awsCredentialsPointerFile)
		
		String awsCredentialsFile = awsCredentialsPointerMap.get("test_aws_credentials_file_path")
		Map awsCredentialsMap = MinionUtils.readJsonFromFile(awsCredentialsFile)
		
		
		String processContextString = """
			{
				"cloud_provider" : "AWS",
				"aws_stack_name": "Archive1Stack"
			}
			"""
		Map processContextMap = jsonSlurper.parseText(processContextString)
		processContextMap << awsCredentialsMap
		
		ProcessContext processContext = new ProcessContext(processContextMap)
		
		Process process = new Process(processSpecMap, processContext)
		assertNotNull(process)
		
		
		/*
		 * Method Under Test
		 */
		process.execute()
		
		
		SequenceNode sn = process.rootProcessNode
		assertNotNull(sn)
		
	} // END testExecute_UsingFiles Method
	
	
	
//	@Test
//	void testConstructor_1() {
//
//		def processSpec = [
//			type: "sequence",
//			something: "extra",
//			nodes: [[
//					type: "action",
//					action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
//				],[
//					type: "action",
//					action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
//				]]
//		] // END processSpec
//		
//		ProcessContext processContext = new ProcessContext([
//			"cloud_provider" : "AWS"
//		])
//		
//		
//		/*
//		 * Method Under Test
//		 */
//		Process process = new Process(processSpec, processContext)
//		assertNotNull(process)
//		
//		assertNotNull(process)
//		ProcessNode rootProcessNode = process.getRootProcessNode()
//		assertEquals("sequence", rootProcessNode.getTypeDefinition())
//		assertEquals(2, rootProcessNode.getNodeList().size())
//		
//		ProcessNode child_processNode1 = rootProcessNode.getNodeList().get(0)
//		assertEquals("action", child_processNode1.getTypeDefinition())
//		assertEquals("com.cm.process.ActionNode", child_processNode1.class.name)
//		
//		ProcessNode child_processNode2 = rootProcessNode.getNodeList().get(1)
//		assertEquals("action", child_processNode2.getTypeDefinition())
//		assertEquals("com.cm.process.ActionNode", child_processNode2.class.name)
//		
//		assertEquals("extra", rootProcessNode.getNodeAttributes().get("something"))
//		
//	} // END  Method
//	
//	
//	
//	@Ignore
//	void testExecute_1() {
//
//		def processSpec = [
//			type: "sequence",
//			something: "extra",
//			nodes: [[
//					type: "action",
//					action: "com.cm.process.TestCloudActionNodeMinion:doSomething"
//				],[
//					type: "action",
//					action: "com.cm.process.TestCloudActionNodeMinion:saySomething"
//				]]
//		] // END processSpec
//		
//		ProcessContext processContext = new ProcessContext([
//			"cloud_provider" : "AWS"
//		])
//		
//		
//		Process process = new Process(processSpec, processContext)
//		assertNotNull(process)
//		
//		
//		/*
//		 * Method Under Test
//		 */
//		process.execute()
//		
//		
//		SequenceNode sn = process.rootProcessNode
//		assertNotNull(sn)
//		
//		ActionNode ac1 = sn.getNodeList()[0]
//		assertNotNull(ac1)
//		Context ac1_context = ac1.getContext()
//		assertEquals("Cool!", ac1_context.get("didSomething"))
//		assertNotEquals("Hello!", ac1_context.get("saidSomething"))
//		
//		ActionNode ac2 = sn.getNodeList()[1]
//		assertNotNull(ac2)
//		Context ac2_context = ac2.getContext()
//		assertEquals("Hello!", ac2_context.get("saidSomething"))
//		assertNotEquals("Cool!", ac2_context.get("didSomething"))
//		
//	} // END Method
	
	
} // END ProcessNodeTest Class
