package com.cm.providers.aws

import com.cm.context.Context
import com.cm.providers.CloudProvider

class AWSCloudProvider implements CloudProvider {


    Context context

    def AWSCloudProvider(Context context){
        this.context = context

//        cfMinion = new CloudformationMinion(this.context)
//        ec2 = new EC2Minion(this.context)
//        s3 = new S3Minion(this.context)
//        ami = new AMIMinion()
    }

    @Override
    String buildInfrastructure() {
//
//        s3.uploadDeployments()
//        def describedInstance = ec2.createInstance()
//        ec2.stopInstances( getInstanceIdFrom(describedInstance) )
//        def customAMI = getSingleCreatedImage(describedInstance)
//        ec2.deleteInstance( getInstanceIdFrom(describedInstance) )
//        return cfMinion.createEnvironment(customAMI)
    }

    @Override
    void deleteInfrastructure() {
//        CloudformationMinion cfMinion = new CloudformationMinion(this.minionContext)
//        cfMinion.deleteStack()
    }

    private def getInstanceIdFrom(def describedInstance){
//        return describedInstance.reservation.instances.get(0).instanceId
    }

    private def getSingleCreatedImage(def describedInstance){
//        return ami.createImage(describedInstance).get(0)
    }
}
