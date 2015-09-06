package com.cm.utils

import groovy.json.JsonSlurper

import org.apache.commons.lang3.StringUtils

import com.cm.exceptions.MinionUtilityException


class MinionUtils {

	public static final CURRENT_DIRECTORY = System.getProperty("user.dir")

	public static final DEFAULT_INFRASTRUCTURE_DIRECTORY = "src"+ File.separator + "main" + File.separator + "infrastructure"
	
//	public static final TEST_AWS_CREDENTIALS_POINTER_FILE = "src/test/resources/test_files/test_aws_credentials_ponter_file.json"
	public static final TEST_AWS_CREDENTIALS_POINTER_FILE = "./test_aws_credentials_ponter_file.json"
	
	
	public static Map readJsonFromFile(String filePath) {
		JsonSlurper slurper = new JsonSlurper()
		File file = new File(filePath)
		file.setReadOnly()
		String fileContents = file.text
		if(StringUtils.isBlank(fileContents)){
			throw new MinionUtilityException(filePath + " can not be empty!")
		}
		
		return slurper.parseText(fileContents)
		
	} // END readJsonFromFile Method


	public static File getCurrentDirectory() {
		return new File(".")
	}


}
