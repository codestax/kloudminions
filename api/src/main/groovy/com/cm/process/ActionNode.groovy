package com.cm.process

import org.apache.commons.lang3.StringUtils

import com.cm.context.ProcessContext
import com.cm.exceptions.ActionException
import com.cm.registry.MinReg

class ActionNode extends ProcessNode {

	String minionClassName
	String minionActionName
	def  minionObject

		
	public ActionNode(Map processNodeSpecObj, ProcessContext context) {
	
		super(processNodeSpecObj, context)
		
		this.resolveActionReference(this.nodeAttributes.action)
		
	}
	
	
	void execute() {

		/*
		 * TODO: Look up the default method to execute if minionActionName is Blank.  If no
		 * default action is specified in the MinionClass then throw some sort of exception
		 */
		
		this.minionObject."$minionActionName"()
		
	}
	
	
	static ActionNode createInstance (Map processNodeSpecObj, ProcessContext processContext) {
		
				
		if (StringUtils.isBlank(processNodeSpecObj.type)) {
			throw new IllegalArgumentException("ERROR: In ActionNode.createInstance processNodeSpecObj.type is either null, empty string or all spaces")
		}
		
		if (processNodeSpecObj.type != "action") {
			throw new IllegalArgumentException("ERROR: In ActionNode.createInstance processNodeSpecObj.type is not equal to 'action'")
		}
		
		if (StringUtils.isBlank(processNodeSpecObj.action)) {
			throw new IllegalArgumentException("ERROR: In ActionNode.createInstance processNodeSpecObj.action is either null, empty string or all spaces")
		}
		
		if (processContext == null) {
			throw new IllegalArgumentException("ERROR: In ActionNode.createInstance processContext is null")
		}
		
		ActionNode actionNode = new ActionNode(processNodeSpecObj, processContext)
		return actionNode
	}
	
	
	def resolveActionReference (String actionReference) {
		
		if (StringUtils.isBlank(actionReference)) {
			throw new ActionException("ERROR: In SimpleAction.minion minionReference is null, empty string or all whitespace")
		}
		
		ArrayList<String> minionRefList = actionReference.split(":")
		this.minionClassName = minionRefList[0]
		this.minionActionName = minionRefList[1]
		
		if (StringUtils.isNotBlank(MinReg.get(this.minionClassName))) {
			this.minionClassName = MinReg.get(this.minionClassName)
		}
		
		// Add the attributes from the process specification to the associated minion's context
		this.context.addAttributes(this.nodeAttributes)
		
		this.minionObject = Class.forName(this.minionClassName)?.newInstance(this.context)
		
	}

	

} // END ActionNode Class
