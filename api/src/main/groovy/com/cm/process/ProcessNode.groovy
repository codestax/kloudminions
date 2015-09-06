package com.cm.process

import org.apache.commons.lang3.StringUtils

import com.cm.context.Context
import com.cm.context.ContextAware
import com.cm.context.ProcessContext
import com.cm.executable.Executable



abstract class ProcessNode implements Executable, ContextAware {

	UUID id
	Map nodeAttributes
	String typeDefinition
	
	ProcessContext processContext
	
	Process parentProcess
	
	public ProcessNode() {
		// TODO: Prevent creation of a ProcessNode with an empty constructor
	}
	
	public ProcessNode(Map processNodeSpecObj, ProcessContext processContext) {
		
		this.setId(UUID.randomUUID())
		
		
		this.nodeAttributes = processNodeSpecObj
		this.typeDefinition = processNodeSpecObj.type
		
		this.processContext = processContext
//		this.context = processContext.createContext()
		this.context = processContext
		
//		this.processContext.addInstanceContext(this, this.context)
		
		
		Process parentProcess = this.processContext.getProcess()
		this.setParentProcess(parentProcess)		
	}


	static ProcessNode createInstance (Map processNodeSpecObj, ProcessContext context) {
		
		if (StringUtils.isBlank(processNodeSpecObj.type)) {
			throw new IllegalArgumentException("ERROR: In ProcessNode.createInstance processNodeSpecObj.type is either null, empty string or all spaces")
		}
		
		if (context == null) {
			throw new IllegalArgumentException("ERROR: In ProcessNode.createInstance context is null")
		}
		
		ProcessNode processNode
		
		if (processNodeSpecObj.type == "sequence") {
			
			processNode = SequenceNode.createInstance(processNodeSpecObj, context)
			
		} else if (processNodeSpecObj.type == "action") {
		
			processNode = ActionNode.createInstance(processNodeSpecObj, context)
		
		}
		
//		processNode.setId(UUID.randomUUID())
//		processNode.setParentProcess(parentProcess)
		return processNode
		
	}
	
	
	void setParentProcess(Process parentProcess) {
		this.parentProcess = parentProcess
		this.parentProcess.addProcessNode(this)
	}
	
	
} // END ProcessNode Class
