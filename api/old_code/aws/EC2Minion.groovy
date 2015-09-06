package com.cm.providers.aws

import groovy.text.SimpleTemplateEngine

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.IamInstanceProfileSpecification
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.RunInstancesRequest
import com.amazonaws.services.ec2.model.StopInstancesRequest
import com.amazonaws.services.ec2.model.TerminateInstancesRequest
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.cm.context.MinionContext;
import com.cm.exceptions.CloudProviderException
import com.google.common.collect.ImmutableSet
import com.google.common.collect.TreeMultiset

class EC2Minion {

    private static final def INSTANCE_COMPLETE_SET = ImmutableSet.<String> builder()
    .add("running", "terminated", "stopped").build()

    private static final def DEFAULT_WAIT_INTERVAL = 3000
    private static final def START_INSTANCE_MSG = "Starting instance: %s\n"
    private static final def CONFIGURE_INSTANCE_MSG = "Configurings instance: %s\n"
    private static final def STOP_INSTANCE_MSG = "Stopping instance(s): %s\n"
    private static final def WAIT_ON_STOP_MSG = "waiting on instance to enter stopping state...\n"
    private static final def INSTANCE_TRANSITIONING_MSG = "instance(s) still transitioning (%s)...\n"
    private static final def INSTANCE_CONFIGURATION_MSG = "waiting on configuration of the instance...\n"
    private static final def INSTANCE_CONFIGURED_MSG = "Instance: %s configured!\n"
    private static final def EMPTY_ENTRY_STATUS_MSG = "none - not configured correctly or no longer available\n"

    //TODO: Instantiate with retry policy
    AmazonEC2Client amazonEC2Client = new AmazonEC2Client()
    AmazonS3 s3Client = new AmazonS3Client()

    private Map config
    private MinionContext context

    EC2Minion(def context) {
        this.context = context
        this.config = context.userConfig
    }

    /**
     * Creates and starts an EC2 instance
     * @return a {@link RunInstancesResult}
     */
    def createInstance(){

        def runInstanceResult = amazonEC2Client.runInstances(
                buildRunInstanceRequest(
                base64EncodeData(buildUserData())
                ))

        printf(START_INSTANCE_MSG, getSingleInstanceId(runInstanceResult))
        waitForTransitionCompletion(getSingleInstanceId(runInstanceResult))

        printf(CONFIGURE_INSTANCE_MSG, getSingleInstanceId(runInstanceResult))
        waitForUserDataCompletion(getSingleInstanceId(runInstanceResult))

        return runInstanceResult
    }

    /**
     * Stops the specified EC2 instance 
     * @param instanceIds instance id's of the EC2 instances to stop
     */
    void stopInstances(def instanceIds)
    {
        amazonEC2Client.stopInstances(buildStopInstanceReuest(instanceIds))
        printf(STOP_INSTANCE_MSG, instanceIds)

        //This is needed to prevent a race condition.
        //Instances don't always transition to the "stopping" state instantly. So, if we call our waitforTransitionComplete()
        //and the instance is still in the "running" state, it returns as transition complete. This causes other methods to fail that expect
        //the instance to be in a running or stopped state.
        def describeInstancesResult = amazonEC2Client.describeInstances(
                buildDescribeInstancesRequest(instanceIds))

        if(getStateFromDescribeInstanceResult(describeInstancesResult) != "stopping"){
            printf(WAIT_ON_STOP_MSG)
            sleep DEFAULT_WAIT_INTERVAL
        }

        waitForTransitionCompletion(instanceIds)
    }

    /**
     * Terminates the specified EC2 instance
     * @param instanceIds instance id's of the EC2 instances to terminate
     * @return
     */
    def deleteInstance(String...instanceIds){
        TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(instanceIds as List<String>)
        amazonEC2Client.terminateInstances(terminateRequest)
    }

    private void waitForTransitionCompletion(def instanceIds){

        def instanceStates = TreeMultiset.create()
        def resourceStates = new HashMap<String, String>()

        def transitionCompleted = false

        while (!transitionCompleted) {
            instanceStates.clear()
            try{
                def describeInstancesResult = amazonEC2Client.describeInstances(buildDescribeInstancesRequest(instanceIds))
                def instances = this.extractInstances(describeInstancesResult)
                for (Instance instance : instances){
                    instanceStates.add(instance.getState().getName())
                    resourceStates.put(instance.getInstanceId(), instance.getState().getName())
                }

                transitionCompleted = INSTANCE_COMPLETE_SET.containsAll(instanceStates)

            }catch(AmazonServiceException ase){
                throw new CloudProviderException(ase.message, ase)
            }

            // Sleep until transition has completed.
            if (!transitionCompleted)
            {
                String entryStatus = formatTransitionResult(instanceStates)

                printf(INSTANCE_TRANSITIONING_MSG, entryStatus)
                sleep DEFAULT_WAIT_INTERVAL
            }
        }
    }

    private def waitForUserDataCompletion(def instanceId){

        def s3FileFound = false

        while(!s3FileFound){

            def objects = s3Client.listObjects(new ListObjectsRequest().withBucketName(config.s3_bucket).withPrefix(instanceId))

            for (S3ObjectSummary objectSummary: objects.getObjectSummaries()) {
                if (objectSummary.getKey().equals(instanceId)) {
                    s3FileFound = true
                }
            }

            if(!s3FileFound){

                printf(INSTANCE_CONFIGURATION_MSG)
                sleep DEFAULT_WAIT_INTERVAL
            }
        }

        printf(INSTANCE_CONFIGURED_MSG, instanceId)
        new S3Minion(context).deleteFile(instanceId)

    }

    private def getStateFromDescribeInstanceResult(def describeInstancesResult){
        return describeInstancesResult.reservations.get(0).instances.get(0).state
    }

    private buildDescribeInstancesRequest(def instanceIds){
        return new DescribeInstancesRequest().withFilters(new Filter()
        .withName("root-device-type").withValues("ebs"),new Filter().withName("instance-id").withValues(instanceIds))
    }

    private def buildStopInstanceReuest(def instanceIds){
        return new StopInstancesRequest()
        .withInstanceIds(instanceIds)
        .withForce(false)
    }

    private def getSingleInstanceId(def runInstanceResult){
        return runInstanceResult.getReservation().getInstances().get(0).instanceId
    }

    private def base64EncodeData(def data){
        return data.getBytes().encodeBase64().toString()
    }

    private def buildRunInstanceRequest(def encodedUserData){
        def iam = new IamInstanceProfileSpecification().withName(config.aws_role_name)

        return new RunInstancesRequest()
        .withImageId(config.base_ami)
        .withSecurityGroups(config.security_group)
        .withKeyName(config.key_pair)
        .withIamInstanceProfile(iam)
        .withInstanceType(config.instance_type)
        .withUserData(encodedUserData)
        .withMinCount(1)
        .withMaxCount(1)
    }

    private def buildUserData(){

        def userData = new StringBuffer()

        userData << buildS3GetCommand()

        if(context.customUserDataPath != null){
            userData << loadCustomUserData()
        }

        def binding = [config:config,s3GetCmd:userData.toString()]
        def file = this.class.classLoader.getResourceAsStream("tomcat-bootstrap.tmpl").text
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(file).make(binding)

        return template.toString()
    }

    private loadCustomUserData(){
        def userDataFileContents = new File(context.customUserDataPath).text
        return userDataFileContents
    }

    private def buildS3GetCommand(){

        def s3GetCmd = new StringBuffer()
        def deploymentFileNames = config.wars

        deploymentFileNames.each {
            s3GetCmd << 's3get ' + config.s3_bucket + '/' + config.environment + '/' + it + ' /var/lib/tomcat7/webapps/' +it +'\n'
        }

        return s3GetCmd
    }

    private def extractInstances(def describeInstancesResult)
    {
        def instances = new ArrayList<Instance>()

        for (def reservation : describeInstancesResult.getReservations())
        {
            instances.addAll(reservation.getInstances())
        }
        return instances
    }


    private def formatTransitionResult(def transitionResult)
    {
        def entryStatus = ""
        if (0 < transitionResult.size())
        {
            for (def entry : transitionResult.entrySet())
            {
                entryStatus += entry.getElement() + ": " + entry.getCount() + ", "
            }
            entryStatus = entryStatus.replaceAll(", \$", "")
        }
        else
        {
            entryStatus += EMPTY_ENTRY_STATUS_MSG
        }

        return entryStatus
    }
}
