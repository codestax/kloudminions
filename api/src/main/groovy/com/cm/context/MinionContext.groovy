package com.cm.context

import java.util.Map;

import groovy.transform.InheritConstructors;


@InheritConstructors
public class MinionContext extends Context {

    private Map userConfig
    private String environment
    private String currentDir
    private String infraDir
    private String templateFile
    private String infrastructureName
    private String customUserDataPath
	
	
    Map getUserConfig(){
        return userConfig
    }

    String getEnvironment(){
        return environment
    }

    String getCurrentDir(){
        return currentDir
    }

    String getTemplateFile(){
        return templateFile
    }

    String getInfraDir(){
        return infraDir
    }

    String getInfrastructureName(){
        return infrastructureName
    }

    String getCustomUserDataPath(){
        return infrastructureName
    }


    MinionContext withEnvironment(String environment){
        this.environment = environment
        return this
    }

    MinionContext withCurrentDir(String currentDir){
        this.currentDir = currentDir
        return this
    }

    MinionContext withInfraDir(String infraDir){
        this.infraDir = infraDir
        return this
    }

    MinionContext withUserConfig(Map userConfig){
        this.userConfig = userConfig
        return this
    }

    MinionContext withTemplateFile(String templateFile){
        this.templateFile = templateFile
        return this
    }

    MinionContext withInfrastructureName(String infrastructureName){
        this.infrastructureName = infrastructureName
        return this
    }

    MinionContext withCustomUserDataPath(String customUserDataPath){
        this.customUserDataPath = customUserDataPath
        return this
    }

}
