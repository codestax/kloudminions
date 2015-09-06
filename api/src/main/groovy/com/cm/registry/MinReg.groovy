package com.cm.registry

import com.cm.minions.Minion;

/**
 * Shortcut methods for the MinionRegistry Class
 *
 */
class MinReg {

	static String get(String minionType) {
		String classNameString = MinionRegistry.getMinionClassName(minionType)
		return classNameString
	}


	static void add(String minionType, Minion minionObject) {
		
		MinionRegistry.registerMinionType(minionType, minionObject)
	}


	static void add(String minionType, Class minionClass) {

		MinionRegistry.registerMinionType(minionType, minionClass)
	}


	static void add(String minionType, String minionClassName) {

		MinionRegistry.registerMinionType(minionType, minionClassName)
	}
	
} // END MinReg Class
