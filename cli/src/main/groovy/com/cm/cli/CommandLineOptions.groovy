package com.cm.cli

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options;

interface CommandLineOptions {
	
	static final def REQUIRED_OPTION = true
	
	def Options getOptions()

}
