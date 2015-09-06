package com.cm.process

import com.cm.context.Context
import com.cm.context.ContextAware
import com.cm.context.ProcessContext
import com.cm.executable.Executable

class Process implements Executable, ContextAware {

	Map processSpec
	String processSpecJSON
	ProcessContext processContext
	ProcessNode rootProcessNode
	Map processNodes
	
	public Process(Map processSpec, ProcessContext rootProcessContext) {
		
		this.processNodes = new LinkedHashMap()
		this.processSpec = processSpec

		this.processContext = rootProcessContext
		this.processContext.setProcess(this)
		
		this.rootProcessNode = ProcessNode.createInstance(this.processSpec, this.processContext)
		
	}

	@Override
	public void execute() {

		this.rootProcessNode.execute()
	
	} // END execute Method
	
	
	void addProcessNode(ProcessNode processNode) {
		this.processNodes[processNode.getId()] = processNode
		this.getProcessContext().add(processNode.getId(), this.context)
	}


} // END Process Class
