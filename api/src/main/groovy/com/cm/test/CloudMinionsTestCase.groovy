package com.cm.test

import static org.junit.Assert.*
import groovy.transform.InheritConstructors

import org.junit.Test

@InheritConstructors
class CloudMinionsTestCase extends GroovyTestCase {

	@Test
	void testDoNothing() {
		assertTrue true
	}

} // END CloudMinionsTestCase Class