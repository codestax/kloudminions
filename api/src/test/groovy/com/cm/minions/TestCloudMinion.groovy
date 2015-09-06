package com.cm.minions

import groovy.transform.InheritConstructors;

import com.cm.context.Context;

/* ======================================================== */
/* ==== TEST SUPPORT CLASSES                           === */
/* ======================================================== */

/*
 * Test class used to implement and instantiate the abstract class
 */
@InheritConstructors
class TestCloudMinion extends AbstractCloudMinion {

	public TestCloudMinion(Context context) {
		super(context)
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
