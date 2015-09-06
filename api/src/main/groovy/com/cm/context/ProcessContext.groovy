package com.cm.context

import groovy.transform.InheritConstructors

import com.cm.process.ProcessNode
import com.cm.process.Process

@InheritConstructors
class ProcessContext extends Context implements GroovyInterceptable {

	Process process
	
	public def get(key) {
		def value = this.contextMap.get(key)
		
		if ((value instanceof Map) && (value.ref != null)) {
			
			def referencedKey = value.ref
			def referencedValue = this.get(referencedKey)
			return referencedValue
			
		} else {
		
			return value
		}
	}
	
} // END ProcessContext Class