package com.cm.minions.aws

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.event.ProgressEvent
import com.amazonaws.event.ProgressListener
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.CreateStackRequest
import com.amazonaws.services.cloudformation.model.CreateStackResult
import com.amazonaws.services.cloudformation.model.DeleteStackRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksResult
import com.amazonaws.services.cloudformation.model.StackStatus
import com.cm.context.Context
import com.cm.minions.AbstractCloudMinion
import com.cm.minions.AbstractMinion


class CloudFormationMinion extends AbstractMinion {
	
	
	public CloudFormationMinion(Context context) {
		super(context)
	}

	
	@Override
	void execute() {
		// TODO Auto-generated method stub
	}

		
	void createStack() {
		
		String stackName = this.context.get("aws_stack_name")
		if (this.doesStackExist() == true) {
			println "Stack " + stackName + " already exists; we cannot create this new stack without deleting the previous stack"
			return
		}
		
		String accessKey = this.context.get("aws_access_key")
		String secretKey = this.context.get("aws_secret_key")
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
		AmazonCloudFormationClient cloudFormationClient = new AmazonCloudFormationClient(awsCredentials)
		
		def createRequest = new CreateStackRequest()
		createRequest.setStackName(stackName)
		
		String cf_script_filename = this.context.get("cf_script_filename")
		File cf_file = new File(cf_script_filename)
		String cf_template = cf_file.getText()
		createRequest.setTemplateBody(cf_template)
		createRequest.setCapabilities(Arrays.asList("CAPABILITY_IAM"))
		
		CreateStackResult createStackResult = cloudFormationClient.createStack(createRequest)
		
		this.waitUntil(this.&stackCompleted, true)
		println "Stack " + stackName + " Created!"
		
	}
	
	
	void deleteStack() {
		
		String stackName = this.context.get("aws_stack_name")
		if (this.doesStackExist() == false) {
			println "Stack " + stackName + " does not exist, so there is no need to delete the stack"
			return
		}
		
		String accessKey = this.context.get("aws_access_key")
		String secretKey = this.context.get("aws_secret_key")
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
		AmazonCloudFormationClient cloudFormationClient = new AmazonCloudFormationClient(awsCredentials)
		
		DeleteStackRequest deleteStackRequest = new DeleteStackRequest()
		deleteStackRequest.setStackName(stackName)
		
		cloudFormationClient.deleteStack(deleteStackRequest)
		
		this.waitUntil(this.&doesStackExist, false)
		println "Stack " + stackName + " Deleted!"
	}
	
	
	void deleteCreateStack() {
		
		this.deleteStack()
		this.createStack()
	}
	
	
	private void waitUntil(closure, targetValue, long pollPeriodLength = 5000, long timeOutCycles = 200, boolean verbose = true) {
		
		for(int i = 0; i < timeOutCycles; ++i) {
			
			if (closure() == targetValue) {
				return
			}
			
			if (verbose) {
				println "Waiting for completion of the task."
			}
			Thread.sleep(pollPeriodLength)
		}
	}
	
	
	private DescribeStacksResult describeStack() {
		
		String accessKey = this.context.get("aws_access_key")
		String secretKey = this.context.get("aws_secret_key")
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
		AmazonCloudFormationClient cloudFormationClient = new AmazonCloudFormationClient(awsCredentials)
		
		String stackName = this.context.get("aws_stack_name")
		DescribeStacksRequest describeStackRequest = new DescribeStacksRequest()
		describeStackRequest.setStackName(stackName)
		
		DescribeStacksResult describeStacksResult = cloudFormationClient.describeStacks(describeStackRequest)
		
		return describeStacksResult
	}
		

	private boolean doesStackExist() {
		
		DescribeStacksResult describeStacksResult = null
		
		try {
			describeStacksResult = this.describeStack() 
		} catch (AmazonServiceException ase) {
			return false
		}
		
		List<com.amazonaws.services.cloudformation.model.Stack> stacks = describeStacksResult.getStacks() 
		
		String stackName = this.context.get("aws_stack_name")
		
		println stacks[0].getStackName() + " - Current Status: " + stacks[0].getStackStatus() + " - Target Status: " + StackStatus.CREATE_COMPLETE.toString()
		
		if ((stacks.size() == 1)  && (stacks[0]).getStackName() == stackName) {
			return true
		} else {
			return true // TODO: Do some defensive programming here
		}
	} // END doesStackExist Method
	
	
	private boolean stackCompleted() {
		
		DescribeStacksResult describeStacksResult = null
		
		try {
			describeStacksResult = this.describeStack()
		} catch (AmazonServiceException ase) {
			return false
		}
		
		List<com.amazonaws.services.cloudformation.model.Stack> stacks = describeStacksResult.getStacks()
		
		String stackName = this.context.get("aws_stack_name")
		
		println "Creating Stack... " + stacks[0].getStackName() + " - Current Status: " + stacks[0].getStackStatus() + " - Target Status: " + StackStatus.CREATE_COMPLETE.toString()
		
		if ((stacks.size() == 1)  && (stacks[0].getStackName() == stackName) && (stacks[0].getStackStatus() == StackStatus.CREATE_COMPLETE.toString())) {
			return true
		} else {
			return false
		}
	} // END stackCompleted Method


} // END CloudFormationMinion class
