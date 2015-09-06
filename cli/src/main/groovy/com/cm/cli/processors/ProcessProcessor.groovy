package com.cm.cli.processors

import groovy.json.JsonSlurper;

import com.cm.cli.actions.Actions
import com.cm.context.ProcessContext
import com.cm.exceptions.CommandLineException
import com.cm.process.Process


/**
 * @author Marco L. Jacobs
 *
 */
class ProcessProcessor implements CLIProcessor {

	private OptionAccessor options

	@Override
	public void process(Actions action, OptionAccessor options) {
		
		this.options = options

		
		String processContextFilePath = this.getProcessContextFilePath()
		File processContextFile = new File(processContextFilePath)
		String processContextFileContents = processContextFile.getText()
		
		JsonSlurper jsonSlurper = new JsonSlurper();
		Map processContextMap = jsonSlurper.parseText(processContextFileContents)
		
		
		String credentialsFilePath = this.getCredentialsFilePath()
		File credentialsFile = new File(credentialsFilePath)
		String credentialsFileContent = credentialsFile.getText()
		
		Map credentialsMap = jsonSlurper.parseText(credentialsFileContent)
		
		processContextMap << credentialsMap
		ProcessContext processContext = new ProcessContext(processContextMap)
		
		
		String processSpecFilePath = this.getProcessSpecFilePath()
		File processSpecFile = new File(processSpecFilePath)
		String processSpecFileContent = processSpecFile.getText()
		
		Map processSpecMap = jsonSlurper.parseText(processSpecFileContent)
		
		
		Process process = new Process(processSpecMap, processContext)
		
		process.execute()
		
	}


	private String getProcessSpecFilePath(){
		if(options.process){
			return options.process
		}else{
			throw new CommandLineException("No Process Specification file path was specified")
		}
	}


    private String getCredentialsFilePath(){
        if(options.credentials){
            return options.credentials
        }
        else{
            throw new CommandLineException("No Credentials file path was specified")
        }
    }

    public String getProcessContextFilePath(){
        if(options.context){
            return options.context
        }
        else{
            return null
        }
    }

} // END ProcessProcessor Class
