package com.cm.providers

import com.cm.context.Context
import com.cm.context.MinionContext;
import com.cm.exceptions.CloudProviderNotFoundException
import com.cm.providers.aws.AWSCloudProvider

/**
 * @author Marco L. Jacobs
 *
 */
class CloudProviderFactory {
	
    static CloudProvider getInstance(CloudProviders providers, Context context) {
        CloudProvider provider
        switch (providers) {
            case CloudProviders.AWS:
                provider = new AWSCloudProvider(context)
                break
            default:
            // throw some exception
                break
        }
        return provider
    }
	
	
	static CloudProvider getInstance(Context context) {
		
		if (context == null) {
			throw new IllegalArgumentException("context is null")
		}
		
		String cloudProviderName = context.get("cloud_provider")
		
		if (!cloudProviderName?.trim()) {
			throw new IllegalArgumentException("'cloud_provider' attribute not found in the Context")
		}
		
		CloudProvider provider = null
		
		switch (cloudProviderName) {
			case "AWS":
				provider = new AWSCloudProvider(context)
				break
			default:
				throw new CloudProviderNotFoundException("Cloud Provider: " + cloudProviderName + " not found!")
				break
		}
		return provider
	}
	
} // END CloudProviderFactory Class
