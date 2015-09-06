package com.cm.context

public class Context {

	private HashMap contextMap

	public Context() {
		this.contextMap = new HashMap()
	}


	public Context(Map contextMap) {

		this();
		this.contextMap << contextMap
	}


	public void add(key, value) {
		this.contextMap.put(key, value)
	}

	public void addAttributes(Map attributes) {
		this.contextMap.putAll(attributes)
	}

	public def get(key) {
		return this.contextMap.get(key)
	}


	public Map getContextMap() {
		return this.contextMap
	}
}
