/**
 * 
 */
package com.cm.providers.aws

import groovy.text.SimpleTemplateEngine

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.AlreadyExistsException
import com.amazonaws.services.cloudformation.model.CreateStackRequest
import com.amazonaws.services.cloudformation.model.DeleteStackRequest
import com.amazonaws.services.cloudformation.model.DescribeStackResourcesRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.amazonaws.services.cloudformation.model.Output
import com.amazonaws.services.cloudformation.model.Stack
import com.amazonaws.services.cloudformation.model.StackResource
import com.amazonaws.services.cloudformation.model.StackStatus
import com.cm.context.MinionContext;
import com.cm.exceptions.CloudProviderException
import com.google.common.collect.ImmutableSet

/**
 * @author marco
 *
 */
class CloudformationMinion {

    private Map config
    private MinionContext context

    def cloudFormationClient = new AmazonCloudFormationClient()
    private static final def CREATE_STACK_MSG = "Creating a stack named: %s\n"
    private static final def STACK_EXIST_MSG = "Stack %s already exists! Deleting\n"
    private static final def DELETE_STACK_MSG = "Deleting stack named %s\n"

    private static final def STACK_STATUS_IN_PROGRESS_SET = ImmutableSet.<String> builder()
    .add(StackStatus.CREATE_IN_PROGRESS.toString(), StackStatus.DELETE_IN_PROGRESS.toString(),
    StackStatus.UPDATE_ROLLBACK_COMPLETE_CLEANUP_IN_PROGRESS.toString(),
    StackStatus.ROLLBACK_IN_PROGRESS.toString(),
    StackStatus.UPDATE_COMPLETE_CLEANUP_IN_PROGRESS.toString(),
    StackStatus.UPDATE_IN_PROGRESS.toString(), StackStatus.UPDATE_ROLLBACK_IN_PROGRESS.toString()).build()


    /**
     * 
     */
    public CloudformationMinion(def context) {
        this.context = context
        this.config = context.userConfig
    }

    /**
     * 
     * @param ami
     */
    def createEnvironment(def ami){

        def stackName = config.environment
        def templateBodyJson = loadCloudFormationTemplate(ami)

        printf(CREATE_STACK_MSG,stackName)

        try{

            cloudFormationClient.createStack(buildStackRequest(stackName, templateBodyJson))
            waitForTransitionCompletion(cloudFormationClient, stackName)
            return stackName
        }catch (AlreadyExistsException aee) {
            printf(STACK_EXIST_MSG,stackName)
            deleteStack()
            createEnvironment(ami)
        }
        catch(AmazonServiceException ase){
            throw new CloudProviderException(ase.message, ase)
        }
    }

    def deleteStack(){
        DeleteStackRequest deleteRequest = new DeleteStackRequest()
        deleteRequest.setStackName(context.infrastructureName)

        printf(DELETE_STACK_MSG, deleteRequest.getStackName())
        cloudFormationClient.deleteStack(deleteRequest)

        this.waitForTransitionCompletion(cloudFormationClient, context.infrastructureName)
    }

    private def waitForTransitionCompletion(AmazonCloudFormation cloudFormation, String stackName){

        def transitionCompleted = false
        def stackStatus = "Unknown"
        def stackReason = "Unspecified"
        Stack transitionedStack = null

        while (!transitionCompleted) {
            try {
                def stacks = cloudFormation.describeStacks(
                        buildStackDescribeRequest(stackName)
                        ).getStacks()
                if (stacks.isEmpty()) {
                    transitionCompleted = true
                }
                else {
                    for (Stack stack : stacks) {
                        if (!STACK_STATUS_IN_PROGRESS_SET.contains(stack.getStackStatus())) {
                            transitionCompleted = true
                            transitionedStack = stack
                        }
                    }
                }
            }
            catch (AmazonServiceException ase) {
                // describeStacks() throws an exception once the stack is
                // deleted instead of returning an empty list.
                if (ase.getMessage().contains("Stack:" + stackName + " does not exist"))
                {
                    transitionCompleted = true
                }
                else{
                    throw new CloudProviderException(ase.getMessage(), ase)
                }
            }

            // Sleep until transition has completed.
            if (!transitionCompleted)
            {
                sleep(3000)
            }
        }
        printTransitionResults(transitionedStack, stackName)
    }

    private def loadCloudFormationTemplate(def ami){

        File customCludFrmTmpl = new File(context.templateFile)
        //TODO: check to see if file exisits

        def binding = [config:config,ami:ami]
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(customCludFrmTmpl).make(binding)

        return template.toString()


        //        if(config.custom_cloud_formation != null){
        //
        //            println("Using Custom Cloudformation script!")
        //            File customCludFrmTmpl = new File(context.infraDir + File.separator + config.custom_cloud_formation )
        //            //TODO: check to see if file exisits
        //
        //            def binding = [config:config,ami:ami]
        //            def engine = new SimpleTemplateEngine()
        //            def template = engine.createTemplate(customCludFrmTmpl).make(binding)
        //
        //            result = template.toString()
        //
        //        }
        //
        //        else{
        //
        //            def binding = [config:config,ami:ami]
        //            def inputStream = this.class.classLoader.getResourceAsStream('cloudformation.tmpl').newReader()
        //            def engine = new SimpleTemplateEngine()
        //            def template = engine.createTemplate(inputStream).make(binding)
        //
        //            result = template.toString()
        //        }

    }

    private void printTransitionResults(def transitionedStack, def stackName){

        def stackStatus
        def stackReason

        if (transitionedStack == null)
        {
            stackStatus = "NO_SUCH_STACK"
            stackReason = "Stack has been deleted"
            println "Transition of stack '" + stackName + "' completed with status " + stackStatus + " (" + stackReason + ")."
        }
        else
        {
            stackStatus = transitionedStack.getStackStatus()
            stackReason = transitionedStack.getStackStatusReason()
            println "Transition of stack '" + stackName + "' completed with status " + stackStatus + " (" + stackReason + ")."
            describeOutputs(transitionedStack)
            describeResources(stackName)
        }
    }

    private def describeOutputs(def stack)
    {
        if (StackStatus.CREATE_COMPLETE.toString().equals(stack.getStackStatus()))
        {
            def outputs = stack.getOutputs()
            def outputKeys = new ArrayList<String>(outputs.size())
            println "Stack '" + stack.getStackName() + "' generated "+ new Integer(outputs.size()).toString() + " outputs:"
            for (Output output : outputs)
            {
                println "\tKey: " + output.getOutputKey() + " | Value: " + output.getOutputValue()
            }
        }
    }

    private def describeResources(def stackName){

        def describeStackResourcesResult = cloudFormationClient.describeStackResources(
                buildStackResourceRequest(stackName))

        def stackResources = describeStackResourcesResult.getStackResources()
        println "The stack contains " + new Integer(stackResources.size()).toString() + " resources:"
        for (StackResource resource : stackResources)
        {
            println "\tId: " + resource.getPhysicalResourceId() + " | Status: "+ resource.getResourceStatus()
        }
    }

    private def buildStackRequest(def stackName, def templateBodyJson){
        def createRequest = new CreateStackRequest()
        createRequest.setStackName(stackName)
        createRequest.setTemplateBody(templateBodyJson)

        return createRequest
    }

    private def buildStackDescribeRequest(def stackName){

        def describeRequest = new DescribeStacksRequest()
        describeRequest.setStackName(stackName)

        return describeRequest
    }

    private def buildStackResourceRequest(def stackName){
        def describeStackResourcesRequest = new DescribeStackResourcesRequest()
        describeStackResourcesRequest.setStackName(stackName)
        return describeStackResourcesRequest
    }
}
