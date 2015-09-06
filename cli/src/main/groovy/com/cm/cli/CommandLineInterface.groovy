/**
 * 
 */
package com.cm.cli

import com.cm.cli.actions.Actions
import com.cm.cli.actions.CLIProcessorFactory
import com.cm.cli.options.RunProcessOptions
import com.cm.cli.processors.CLIProcessor
import com.cm.exceptions.CloudProviderException
import com.cm.exceptions.CommandLineException

/**
 * @author marco
 *
 */
class CommandLineInterface {

	static final def ERROR_EXIT_CODE = 1
	static final def PRINT_HELP_WITH_ERROR = true
	static final String NO_ACTION_ERR_MSG = "An action must be provided.\n%s\n"

	static CliBuilder cliBuilder = new CliBuilder(
				usage: 'minion [command] [options]',
				header: 'Options:', 
				stopAtNonOption : false)


	private static def onError(Throwable cause) {
		onError(cause, null, false)
	}


	private static def onError(Throwable cause, boolean printHelpWithError){

		printf("\n%s\n",cause.message)

		if (printHelpWithError){
			printHelp()
		}
		System.exit(ERROR_EXIT_CODE)
	}

	public static main(args) {
		
		try{
			OptionAccessor optionAccessor = parseArguments(args)

            if(optionAccessor.h){
                printHelp()
                return
            }
            if(!optionAccessor.a){
                throw new CommandLineException(
					String.format(NO_ACTION_ERR_MSG,
								  Actions.values().toString())
					)
            }

            String actionString = minionActionFrom(optionAccessor.a)
            Actions action = Actions.valueOf(actionString)
            CLIProcessor cliProcessor = CLIProcessorFactory.getInstance(action)

            cliProcessor.process(action, optionAccessor)
        }
        catch(CommandLineException e){
            onError(e, PRINT_HELP_WITH_ERROR)
        }
        catch(CloudProviderException e){
            onError(e, !PRINT_HELP_WITH_ERROR)
        }
    }
	
    private static void printHelp(){
        cliBuilder.usage()
    }

	private static OptionAccessor parseArguments(String[] args){
        CommandLineOptions commandLineOptions = new RunProcessOptions()
        cliBuilder.setOptions(commandLineOptions.options)
        return cliBuilder.parse(args)
    }

    private static String minionActionFrom(def action){
        return action.toString().toUpperCase()
    }
	
} // END CommandLineInterface Class
