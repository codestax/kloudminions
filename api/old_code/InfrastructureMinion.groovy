package com.cm.minions

import com.cm.context.Context;
import com.cm.context.MinionContext;
import com.cm.exceptions.CommandLineException
import com.cm.providers.CloudProvider
import com.cm.providers.CloudProviderFactory
import com.cm.providers.CloudProviders
import com.cm.utils.MinionUtils

class InfrastructureMinion implements Minion {

    private static final String CLOUD_PROVIDER_NOT_FOUND_ERR_MSG = "Cloud provider is not specified in environment configuration file!"
	protected static final CURRENT_DIRECTORY = System.getProperty("user.dir")
	protected static final DEFAULT_INFRASTRUCTURE_DIRECTORY = "src"+ File.separator + "main" + File.separator + "infrastructure"

	private MinionContext context
	
    public String buildInfrastructure(String configFilePath, String templateFilePath) {
        return buildInfrastructure(configFilePath, templateFilePath, null)
    }

    public String buildInfrastructure(String configFilePath, String templateFilePath, String customUserDataPath) {
        this.context = new MinionContext()
                .withCustomUserDataPath(customUserDataPath)
                .withCurrentDir(MinionUtils.CURRENT_DIRECTORY)
                .withUserConfig(MinionUtils.readJsonFromFile(configFilePath))
                .withTemplateFile(templateFilePath)

        CloudProvider provider = this.getCloudProvider()

        return provider.buildInfrastructure()
    }


    public void destroyInfrastructure(String configFilePath, String infrastructureName) {
        this.context = new MinionContext()
                .withCurrentDir(MinionUtils.CURRENT_DIRECTORY)
                .withInfrastructureName(infrastructureName)
                .withUserConfig(MinionUtils.readJsonFromFile(configFilePath))

        CloudProvider provider = this.getCloudProvider()
        provider.deleteInfrastructure()
    }


    private CloudProvider getCloudProvider() {
        Map config = this.context.userConfig

        CloudProviders cloudProviders = CloudProviders.valueOf(config.provider.toUpperCase())

        try {
            return CloudProviderFactory.getInstance(cloudProviders, this.context)
        } catch(NullPointerException e) {
            throw new CommandLineException(CLOUD_PROVIDER_NOT_FOUND_ERR_MSG)
        }
    }


	/**
	 *  Sets the infrastructure directory. If the user does no provide a path to the infrastructure directory,
	 *  it is assumed that the default path (/src/main/infrastructure/) will be used.
	 *
	 * @param options commandline options provided by the user
	 * @return the path to the infrastructure directory.
	 */
	def getInfraDir(){
		def infraDir = DEFAULT_INFRASTRUCTURE_DIRECTORY
		if(!options.d){
			def	file = new File(CURRENT_DIRECTORY + File.separator + DEFAULT_INFRASTRUCTURE_DIRECTORY)
			if(!file.exists()){
				throw new CommandLineException("Infrastructure dir: " + CURRENT_DIRECTORY + File.separator + DEFAULT_INFRASTRUCTURE_DIRECTORY + " does not exist!")
			}
		}
		else if (options.d){
			def	file = new File(CURRENT_DIRECTORY + File.separator + options.d)
			if(!file.exists()){
				throw new CommandLineException("Infrastructure dir: " + CURRENT_DIRECTORY + File.separator + options.d + " does not exist!")
			}
			infraDir = file.canonicalPath
		}

		return infraDir
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	public void buildContext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Context context) {
		// TODO Auto-generated method stub
		
	}
	
}
