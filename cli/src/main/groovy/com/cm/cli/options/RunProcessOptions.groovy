package com.cm.cli.options

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

import com.cm.cli.CommandLineOptions
import com.cm.cli.OptionsBuilder

class RunProcessOptions implements CommandLineOptions {

    static final String HELP_SHORT_VALUE = "h"
    static final String HELP_LONG_VALUE = "help"
    static final String HELP_DESCRIPTION = "usage information"
    static final Option HELP_OPTION = new Option(HELP_SHORT_VALUE, HELP_LONG_VALUE, false, HELP_DESCRIPTION)

    static final String ACTION_SHORT_VALUE = "a"
    static final String ACTION_LONG_VALUE = "action"
    static final String ACTION_DESCRIPTION = "action for minion to perform"
    static final Option ACTION_OPTION = new Option(ACTION_SHORT_VALUE, ACTION_LONG_VALUE, true, ACTION_DESCRIPTION)

    static final String PROCESS_SHORT_VALUE = "p"
    static final String PROCESS_LONG_VALUE = "process"
    static final String PROCESS_DESCRIPTION = "Path to process definition file"
    static final Option PROCESS_OPTION = new Option(PROCESS_SHORT_VALUE, PROCESS_LONG_VALUE, true, PROCESS_DESCRIPTION)

    static final String CREDENTIALS_SHORT_VALUE = "c"
    static final String CREDENTIALS_LONG_VALUE = "credentials"
    static final String CREDENTIALS_DESCRIPTION = "Path to credentials file"
    static final Option CREDENTIALS_OPTION = new Option(CREDENTIALS_SHORT_VALUE, CREDENTIALS_LONG_VALUE, true, CREDENTIALS_DESCRIPTION)

    static final String CONTEXT_SHORT_VALUE = "t"
    static final String CONTEXT_LONG_VALUE = "context"
    static final String CONTEXT_DESCRIPTION = "Path to process context file"
    static final Option CONTEXT_OPTION = new Option(CONTEXT_SHORT_VALUE, CONTEXT_LONG_VALUE, true, CONTEXT_DESCRIPTION)

    private static final Options OPTIONS

    static{
        OPTIONS = new OptionsBuilder().addOption(ACTION_OPTION)
                .addOption(PROCESS_OPTION)
                .addOption(CREDENTIALS_OPTION)
                .addOption(CONTEXT_OPTION)
                .addOption(HELP_OPTION)
                .build()
    }

    @Override
    public Options getOptions() {
        return OPTIONS
    }
}
