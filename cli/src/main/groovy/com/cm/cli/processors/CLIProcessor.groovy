/**
 * 
 */
package com.cm.cli.processors

import com.cm.cli.actions.Actions


/**
 * @author Marco L. Jacobs
 *
 */
interface CLIProcessor {

	public void process(Actions action, OptionAccessor options)

} // END CLIProcessor Interface
