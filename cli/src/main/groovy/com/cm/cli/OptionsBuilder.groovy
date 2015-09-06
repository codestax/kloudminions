/**
 * 
 */
package com.cm.cli

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author marco
 *
 */
class OptionsBuilder {

	private def Set<Option> storedOptions = new HashSet<>();
	private def static final String DUPLICATE_SHORT_CODE = "The short code %s has already been added!"
	private def static final String DUPLICATE_LONG_CODE = "The long code %s has already been added!"

	def OptionsBuilder addOption(Option option){

		verifyOptionHasNotAlreadyBeenAdded(option)
		storedOptions.add(option)

		return this
	}

	def Options build() {

		Options options = new Options()
		for (Option option : storedOptions) {
			options.addOption(option)
		}

		return options
	}

	private def verifyOptionHasNotAlreadyBeenAdded(Option option) {

		for (Option anOption: storedOptions){
			verifyOptionHasNotAlreadyBeenAdded(anOption.getOpt(), option.getOpt(), DUPLICATE_SHORT_CODE)
			verifyOptionHasNotAlreadyBeenAdded(anOption.getLongOpt(), option.getLongOpt(), DUPLICATE_LONG_CODE)
		}
	}

	private def verifyOptionHasNotAlreadyBeenAdded(String storedOptionField, String newOptionField, String errorMessage){

		if(storedOptionField.equals(newOptionField)){
			throw new IllegalArgumentException(String.format(errorMessage, newOptionField))
		}
	}
	
}
