/**
 * 
 */
package com.cm.cli.actions

import com.cm.cli.processors.CLIProcessor
import com.cm.cli.processors.ProcessProcessor

/**
 * @author Marco L. Jacobs
 *
 */
class CLIProcessorFactory {

	static CLIProcessor getInstance(Actions command) {
		CLIProcessor processor
		switch (command) {

			case Actions.RUN_PROCESS:
				processor = new ProcessProcessor()
				break
			default:
				// throw some exception
				break
		}
		return processor
	}

} // END CLIProcessorFactory Class
