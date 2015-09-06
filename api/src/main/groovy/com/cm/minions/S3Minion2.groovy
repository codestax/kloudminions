package com.cm.minions

import groovy.io.FileType
import groovy.transform.InheritConstructors

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.BucketVersioningConfiguration
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest
import com.cm.exceptions.CloudProviderException
import com.cm.exceptions.MinionException


@InheritConstructors
class S3Minion2 extends AbstractCloudMinion {

	private AmazonS3 s3client = new AmazonS3Client()

	private static final def UPLOADING_MSG = "Uploading %s to S3!"
	private static final def DEPLOYMENTS_NOT_FOUND_MSG = "The following file(s) could not be uploaded to S3 because they were not found: %s"


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

		def deploymentFileNamesToUpload = this.getDeploymentFileNamesToUpload()
		def filesFound = this.searchCurrentDirectoryStructureForFiles(
								deploymentFileNamesToUpload, 
								this.minionContext.get("root_path"))

		def namesOfFilesFound = filesFound*.name

		if(namesOfFilesFound.size() != deploymentFileNamesToUpload.size()){
			this.determineDeploymentFilesNotFound(namesOfFilesFound, deploymentFileNamesToUpload)
		}

		return filesFound
	}

	private searchCurrentDirectoryStructureForFiles(def deploymentFileNamesToUpload, File rootDir) {
		
		def filesFound = []

		deploymentFileNamesToUpload.each {fileName ->
			rootDir.eachFileRecurse(FileType.FILES) {
				if(it.name == fileName){
					filesFound.add(it)
				}
			}
		}

		def uniqueFilesFound = filesFound.unique { file -> file.name }
		return uniqueFilesFound
	}

	
	private def buildS3BucketVersioningRequest(){
		return new SetBucketVersioningConfigurationRequest(getS3BucketName(),
		new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED))
	}


	/*
	 * 
	 * @return
	 */
	private String getS3BucketName() {
		return this.minionContext.get("archive_target_env_conf").s3_bucket
	}

	
	/*
	 * 
	 * @return
	 */
	private String getEnvironmentName() {
		return this.minionContext.get("archive_target_env_conf").environment
	}

	
	/*
	 * 
	 * @param fileName
	 * @return
	 */
	private String buildS3KeyFrom(def fileName) {
		return this.getEnvironmentName() + File.separator + fileName
	}

	
	/*
	 * 
	 * @return
	 */
	private def getDeploymentFileNamesToUpload() {
		return this.minionContext.get("archive_target_env_conf").wars
	}

	/*
	 * 
	 */
	private def determineDeploymentFilesNotFound(def namesOfFilesFound, def deploymentFileNamesToUpload){

		deploymentFileNamesToUpload.removeAll(namesOfFilesFound)
		throw new MinionException(String.format(DEPLOYMENTS_NOT_FOUND_MSG, (deploymentFileNamesToUpload + namesOfFilesFound)))
	}


	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
} // END S3Minion Class
