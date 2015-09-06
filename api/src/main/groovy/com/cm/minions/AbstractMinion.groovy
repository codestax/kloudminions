package com.cm.minions;

import groovy.transform.InheritConstructors

import com.cm.context.Context


@InheritConstructors
public abstract class AbstractMinion implements Minion {

	
	/**
	 * Prevents Minions from being created via the empty constructor
	 */
	public AbstractMinion() {
		throw new IllegalArgumentException("ERROR: Cannot instantiate a Minion with the empty constructor")
	}
	
	
	/**
	 * 
	 * 
	 * @param context
	 */
	public AbstractMinion(Context context) {
		
		if (context == null) {
			throw new IllegalArgumentException("context is null")
		}
		
		this.context = context
	}
	
	
	/**
	 * 
	 */
	public void execute(Context context) {
		this.context = context
		this.execute()
	}
	
	
} // END AbstractMinion Class
