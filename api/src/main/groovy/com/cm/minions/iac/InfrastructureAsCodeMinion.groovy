package com.cm.minions.iac

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils

import com.cm.context.Context
import com.cm.exceptions.ContextException
import com.cm.exceptions.MinionContextException
import com.cm.minions.AbstractCloudMinion
import com.cm.utils.MinionUtils


class InfrastructureAsCodeMinion extends AbstractCloudMinion {
	
	public static final String DEFAULT_CONFIG_JSON_FILE_NAME = "config.json"
	public static final String DEFAULT_CLOUDFORMATION_TEMPLATE_FILE_NAME = "cloudformation.tmpl"
	
	public InfrastructureAsCodeMinion(Context context) {
		super(context)
	}

	
	@Override
	void execute() {
		// TODO Auto-generated method stub
	}

	
	public void processArchiveFiles () {
		
		/*
		 * Attributes needed from the Minion's context
		 *
		 * == Main pointer ==
		 * iac.archive_path  --> The path to the Infrastructure as Code Archive directory
		 *
		 *
		 * Attributes calculated and added to the Minion's context
		 */
		
		
		/*
		 * Find the Infrastructure Archive Directory
		 */
		String archivePath = this.context.get("iac.archive_path")
		
		if (StringUtils.isBlank(archivePath)) {
			throw new ContextException("ERROR: In InfrastructureAsCodeMinion.execute(): iac.archive_path attribute not found in Context")
		}
		
		/* Stash the Archive Path file pointer in the context for safe keeping */
		File archivePathObj = new File(archivePath)
		archivePathObj.setReadOnly()
		this.context.add("iac.archive_path_obj", archivePathObj)
		
		String archiveTargetEnv = this.context.get("iac.archive_target_env")
		
		if (StringUtils.isBlank(archiveTargetEnv)) {
			throw new ContextException("ERROR: In InfrastructureAsCodeMinion.execute(): iac.archive_target_env attribute not found in Context")
		}
		
		Map archiveEnvironmentConfigMap = this.createArchiveEnvironmentConfigMap(this.context.get("iac.archive_path_obj"), archiveTargetEnv)
		this.context.add("iac.archive_target_env_conf", archiveEnvironmentConfigMap)
		
		
		/*
		 * Get the Cloudformation template from the archive directory
		 * Use the default Cloudformation filename "cloudformation.tmpl"
		 *
		 */
		String cloudformationTemplateText = this.getCloudformationText(this.context.get("iac.archive_path_obj"))
		this.context.add("iac.archive_cloudformation_tmpl_text", cloudformationTemplateText)
		println "Cloudformation template text: " + cloudformationTemplateText

		
		/*
		 * Set the current root directory in the Context
		 */
		if (StringUtils.isBlank(this.context.get("iac.root_path"))) {
			this.context.add("iac.root_path", MinionUtils.getCurrentDirectory())
		}
		
		
		/*
		 * Set Puppet Modules Directory
		 */
		if (archiveEnvironmentConfigMap.containsKey("puppet_package_file_names")) {
			
			File puppetModulesDir = new File(archivePathObj.getAbsolutePath() + File.separator + "puppet" + File.separator + "modules")
			
			if (puppetModulesDir?.exists() && (puppetModulesDir.directory == true)) {
				
				String puppetPackageFileNamesString = archiveEnvironmentConfigMap.get("puppet_package_file_names")
				String[] puppetPackageFileNames = puppetPackageFileNamesString.split(",")
				
				String puppetModulesBasePath = puppetModulesDir.absolutePath
				String[] packageFilePaths = new String[puppetPackageFileNames.length]
				
				for (int i = 0; i < puppetPackageFileNames.length; ++i) {
					
					String[] puppetPackageFileNameKeyAndPath = puppetPackageFileNames[i].split(":")
					
					String filePath = ""
					
					if (StringUtils.isNotBlank(puppetPackageFileNameKeyAndPath[0])) {
						filePath += puppetPackageFileNameKeyAndPath[0] + ":"
					}
					filePath += puppetModulesBasePath + "/" + puppetPackageFileNameKeyAndPath[1]
					packageFilePaths[i] = filePath
					
				}
				
				this.context.add("iac.puppet_package_files", packageFilePaths.join(","))
				
				
			}
		
			
			
		} // END if archiveEnvironmentConfigMap.containsKey("puppet_package_file_names") statement
		
	}
	
	
	private Map createArchiveEnvironmentConfigMap(File archivePathObj, String archiveTargetEnv) {
		
		if (archivePathObj == null) {
			throw new IllegalArgumentException("ERROR: In createArchiveConfigMap(File archivePathObj): archivePathObj is null")
		}
		
		if (StringUtils.isBlank(archiveTargetEnv)) {
			throw new IllegalArgumentException("ERROR: In createArchiveConfigMap(String archiveTargetEnv): archiveTargetEnv is Blank [null, empty string, or all spaces]")
		}
		
		String archiveConfigPathAndFile = archivePathObj.canonicalPath + File.separator + archiveTargetEnv + File.separator + InfrastructureAsCodeMinion.DEFAULT_CONFIG_JSON_FILE_NAME
		
		Map archiveEnvironmentConfigMap = MinionUtils.readJsonFromFile(archiveConfigPathAndFile)
		return archiveEnvironmentConfigMap
		
	}
	
	
	private String getCloudformationText(File archivePathObj) {
		
		if (archivePathObj == null) {
			throw new IllegalArgumentException("ERROR: In getCloudformationText(File archivePathObj): archivePathObj is null")
		}
		
		String archiveCloudformationTemplatePathAndFile = archivePathObj.canonicalPath + File.separator + InfrastructureAsCodeMinion.DEFAULT_CLOUDFORMATION_TEMPLATE_FILE_NAME
		this.context.add("iac.cloudformation_template_path_and_file", archiveCloudformationTemplatePathAndFile)
		
		File cloudformationTemplateFileObj = new File(archiveCloudformationTemplatePathAndFile)
		cloudformationTemplateFileObj.setReadOnly()
		String cloudformationTemplateText = cloudformationTemplateFileObj.text
		
		return cloudformationTemplateText
		
	}
	
	
} // END InfrastructureAsCodeMinion class
