package com.cm.minions

import groovy.transform.InheritConstructors

import com.cm.context.Context
import com.cm.providers.CloudProvider
import com.cm.providers.CloudProviderFactory

@InheritConstructors
public abstract class AbstractCloudMinion extends AbstractMinion {
	
	CloudProvider cloudProvider

	/**
	 * 
	 * @param context
	 */
	public AbstractCloudMinion(Context context) {

		super(context)
		this.cloudProvider = CloudProviderFactory.getInstance(this.context)
	}
	
} // END AbstractCloudMinion Class
