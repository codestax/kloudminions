package com.cm.process

import org.apache.commons.lang3.StringUtils

import com.cm.context.ProcessContext

class SequenceNode extends ProcessNode {

	List<ProcessNode> nodeList
	
	public SequenceNode(Map processNodeSpecObj, ProcessContext processContext) {

		super(processNodeSpecObj, processContext)

		this.nodeList = new ArrayList<ProcessNode>()

		processNodeSpecObj.nodes.each {childProcessNodeSpecObj ->

			ProcessNode processNode = ProcessNode.createInstance(childProcessNodeSpecObj, processContext)
			this.nodeList.add(processNode)
		}

	}

	
	void execute () {
		
		this.nodeList.each { processNode ->
			processNode.execute()
		}
	}

	
	static SequenceNode createInstance(Map processNodeSpecObj, ProcessContext processContext) {

		if (StringUtils.isBlank(processNodeSpecObj.type)) {
			throw new IllegalArgumentException("ERROR: In SequenceNode.createInstance processNodeSpecObj.type is either null, empty string or all spaces")
		}

		if (processNodeSpecObj.type != "sequence") {
			throw new IllegalArgumentException("ERROR: In SequenceNode.createInstance processNodeSpecObj.type is not equal to 'sequence'")
		}

		if (processNodeSpecObj.nodes == null) {
			throw new IllegalArgumentException("ERROR: In SequenceNode.createInstance processNodeSpecObj.nodes is null")
		}

		if ( (!processNodeSpecObj.nodes?.class?.isArray()) && 
			 (!List.isAssignableFrom(processNodeSpecObj.nodes.getClass())) ) {
			throw new IllegalArgumentException("ERROR: In SequenceNode.createInstance processNodeSpecObj.nodes is not an array nor is it a List")
		}

		if (processContext == null) {
			throw new IllegalArgumentException("ERROR: In SequenceNode.createInstance processContext is null")
		}
		

		SequenceNode sequenceNode = new SequenceNode(processNodeSpecObj, processContext)
		return sequenceNode
	}

} // END SequenceNode Class
