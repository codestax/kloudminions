package com.cm.minions.aws;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket
import com.cm.context.Context;
import com.cm.minions.AbstractCloudMinion;

public class S3Minion extends AbstractCloudMinion {

	
	public S3Minion(Context context) {
		super(context)
	}
	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	
	void addFilesToBucket() {
		
		String accessKey = this.context.get("aws_access_key")
		String secretKey = this.context.get("aws_secret_key")
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
		AmazonS3Client s3client = new AmazonS3Client(awsCredentials)
		
		String filesToSend = this.context.get("file_names")
		
		println "Files to put in S3 Bucket: " + filesToSend
		
		boolean createBucketIfNotExist = true;
		String createBucketIfNotExistString = "true"
		if (StringUtils.isNotBlank(this.context.get("create_bucket_if_not_exist"))) {
			createBucketIfNotExistString = this.context.get("create_bucket_if_not_exist")
		}
		
		if ("true".equalsIgnoreCase(createBucketIfNotExistString)) {
			createBucketIfNotExist = true
		} else {
			createBucketIfNotExist = false
		}
		
		String bucketName = this.context.get("bucket_name")
		Bucket bucket = null;
		if (!s3client.doesBucketExist(bucketName)) {
			
			bucket = s3client.createBucket(bucketName)
			
		}
		
		String[] filesToSendList = filesToSend.split(",")
		for(int i = 0; i < filesToSendList.length; ++i) {
			String[] fileNameKeyAndPath = filesToSendList[i].split(":")
			s3client.putObject(bucketName, fileNameKeyAndPath[0], new File(fileNameKeyAndPath[1]))
		}
		def a =1
		
	}
	

}


