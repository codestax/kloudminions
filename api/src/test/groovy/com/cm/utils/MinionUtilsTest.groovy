package com.cm.utils

import org.junit.Test
import com.cm.test.CloudMinionsTestCase

class MinionUtilsTest extends CloudMinionsTestCase {


	@Test
	void testGetCurrentDirectory() {
	
		File currDir = MinionUtils.getCurrentDirectory()
		assertTrue true
	}

} // END MinionUtilsTest Class
