package com.cm.registry

import org.apache.commons.lang3.StringUtils

import com.cm.minions.Minion

public class MinionRegistry {

	static Map minionRegister = initializeRegister()
	
	private static Map initializeRegister() {
		Map defaultRegister = new HashMap()
		defaultRegister.put("test_minion", "com.cm.minions.TestCloudMinion")
		defaultRegister.put("cloud_formation_minion", "com.cm.minions.aws.CloudFormationMinion")
		defaultRegister.put("iac_minion", "com.cm.minions.iac.InfrastructureAsCodeMinion")
		defaultRegister.put("s3_minion", "com.cm.minions.aws.S3Minion")
		return defaultRegister
	}
	
	static String getMinionClassName(String minionType) {
		
		return MinionRegistry.minionRegister.get(minionType)
	}
	
	
	static void registerMinionType(String minionType, Minion minionObject) {
		
		if (minionObject == null) {
			throw new IllegalArgumentException("ERROR: In MinionRegistry.registerMinionType minionObject argument is null")
		}
		
		Class minionClass = minionObject.class
		MinionRegistry.registerMinionType(minionType, minionClass)
	}


	static void registerMinionType(String minionType, Class minionClass) {
		
		if (minionClass == null) {
			throw new IllegalArgumentException("ERROR: In MinionRegistry.registerMinionType minionClass argument is null")
		}
		
		String minionClassName = minionClass.name
		MinionRegistry.registerMinionType(minionType, minionClassName)
	}
	
	
	static void registerMinionType(String minionType, String minionClassName) {
		
		if (StringUtils.isBlank(minionType)) {
			throw new IllegalArgumentException("ERROR: In MinionRegistry.registerMinionType minionType argument is null, empty string or blank")
		}
		
		if (StringUtils.isBlank(minionClassName)) {
			throw new IllegalArgumentException("ERROR: In MinionRegistry.registerMinionType minionClassName argument is null, empty string or blank")
		}
		
		MinionRegistry.minionRegister.put(minionType, minionClassName)
	}
	
} // END MinionRegistry Class
