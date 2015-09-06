package com.cm.process


import groovy.transform.InheritConstructors

import com.cm.context.Context
import com.cm.minions.AbstractCloudMinion
import com.cm.registry.MinReg

/*
 *  FOR TESTING PURPOSES ONLY!!!!!!
 *  Class used to implement and instantiate the abstract class
 */
@InheritConstructors
public class TestCloudActionNodeMinion extends AbstractCloudMinion {

	static {
		MinReg.add("test_action_cloud_minion", "com.cm.process.TestCloudActionNodeMinion")
	}
	
	public TestCloudActionNodeMinion(Context context) {
		super(context)
	}

	@Override
	void execute() {
		// TODO Auto-generated method stub

	}
	
	void doSomething() {
		
		this.context.add("didSomething", "Cool!")
//		println "doSomething"
		
	}
	
	void saySomething() {
		
		this.context.add("saidSomething", "Hello!")
//		println "saySomething"
		
	}
} // END TestCloudActionNodeMinion Inner Class