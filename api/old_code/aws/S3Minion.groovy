package com.cm.providers.aws

import groovy.io.FileType

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.BucketVersioningConfiguration
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest
import com.cm.exceptions.CloudProviderException

class S3Minion {
	
    private AmazonS3 s3client = new AmazonS3Client()

    private static final def UPLOADING_MSG = "Uploading %s to S3!"
    private static final def DEPLOYMENTS_NOT_FOUND_MSG = "The following file(s) could not be uploaded to S3 because they were not found: %s"
    private def context

    S3Minion(def context) {
        this.context = context
    }

    /**
     * Uploads the deployment files to S3
     * 
     */
    void uploadDeployments(){

        def deployments = this.retrieveDeployments()
        try {
            s3client.setBucketVersioningConfiguration(buildS3BucketVersioningRequest())

            deployments.each {
                printf(UPLOADING_MSG,it.name)
                s3client.putObject(new PutObjectRequest(
                        getS3BucketName(),
                        buildS3KeyFrom(it.name),
                        it))
            }
        } catch (AmazonServiceException ase) {
            throw new CloudProviderException(ase.message, ase)
        } catch (AmazonClientException ace) {
            throw new CloudProviderException(ace.message, ace)
        }
    }

    /**
     * Deletes the specified file from the configured S3 bucket.
     * @param fileName file to delete
     * 
     */
    void deleteFile(def fileName){
        s3client.deleteObject(new DeleteObjectRequest(getS3BucketName(), fileName))
    }

    private def retrieveDeployments(){

        def deploymentFileNamesToUpload = getDeploymentFileNamesToUpload()
        def filesFound = searchCurrentDirectoryStructureForFiles(deploymentFileNamesToUpload)

        def namesOfFilesFound = filesFound*.name

        if(namesOfFilesFound.size() != deploymentFileNamesToUpload.size()){
            determineDeploymentFilesNotFound(namesOfFilesFound, deploymentFileNamesToUpload)
        }

        return filesFound
    }

    private searchCurrentDirectoryStructureForFiles(def deploymentFileNamesToUpload ){
        def filesFound = []

        deploymentFileNamesToUpload.each {war ->
            new File(getCurrentDirectory()).eachFileRecurse(FileType.FILES) {
                if(it.name == war){
                    filesFound.add(it)
                }
            }
        }

        return removeDuplicates(filesFound)
    }

    private def removeDuplicates(def fileList){
        def results = []
        def newFileList = []

        fileList.each {
            if(!results.contains(it.name)){
                results.add(it.name)
                newFileList.add(it)
            }
        }

        return newFileList
    }

    private def buildS3BucketVersioningRequest(){
        return new SetBucketVersioningConfigurationRequest(getS3BucketName(),
        new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED))
    }

    private def getS3BucketName(){
        return context.userConfig.s3_bucket
    }

    private def getCurrentDirectory(){
        return context.currentDir
    }

    private def getEnvironmentName(){
        return context.userConfig.environment
    }

    private def buildS3KeyFrom(def fileName){
        return getEnvironmentName()+"/"+fileName
    }

    private def getDeploymentFileNamesToUpload(){
        return context.userConfig.wars
    }

    private def determineDeploymentFilesNotFound(def namesOfFilesFound, def deploymentFileNamesToUpload){

        namesOfFilesFound.intersect(deploymentFileNamesToUpload).each {
            deploymentFileNamesToUpload.remove(it)
            namesOfFilesFound.remove(it)
        }

        throw new CloudProviderException(String.format(DEPLOYMENTS_NOT_FOUND_MSG, (deploymentFileNamesToUpload + namesOfFilesFound)))
    }
}
