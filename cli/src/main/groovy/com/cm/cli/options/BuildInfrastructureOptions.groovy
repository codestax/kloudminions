package com.cm.cli.options

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

import com.cm.cli.CommandLineOptions
import com.cm.cli.OptionsBuilder

class BuildInfrastructureOptions implements CommandLineOptions {

    static final String HELP_SHORT_VALUE = "h"
    static final String HELP_LONG_VALUE = "help"
    static final String HELP_DESCRIPTION = "usage information"
    static final Option HELP_OPTION = new Option(HELP_SHORT_VALUE, HELP_LONG_VALUE, false, HELP_DESCRIPTION)

    static final String ACTION_SHORT_VALUE = "a"
    static final String ACTION_LONG_VALUE = "action"
    static final String ACTION_DESCRIPTION = "action for minion to perform"
    static final Option ACTION_OPTION = new Option(ACTION_SHORT_VALUE, ACTION_LONG_VALUE, true, ACTION_DESCRIPTION)

    static final String TEMPLATE_SHORT_VALUE = "t"
    static final String TEMPLATE_LONG_VALUE = "template"
    static final String TEMPLATE_DESCRIPTION = "path to template file"
    static final Option TEMPLATE_OPTION = new Option(TEMPLATE_SHORT_VALUE,TEMPLATE_LONG_VALUE, true, TEMPLATE_DESCRIPTION)

    static final String CONFIG_SHORT_VALUE = "c"
    static final String CONFIG_LONG_VALUE = "config"
    static final String CONFIG_DESCRIPTION = "path to config file"
    static final Option CONFIG_OPTION = new Option(CONFIG_SHORT_VALUE,CONFIG_LONG_VALUE, true, CONFIG_DESCRIPTION)

    static final String CUSTOM_USER_DATA_SHORT_VALUE = "u"
    static final String CUSTOM_USER_DATA_LONG_VALUE = "user-data"
    static final String CUSTOM_USER_DATA_DESCRIPTION = "path to custom user data file"
    static final Option CUSTOM_USER_DATA_OPTION = new Option(CUSTOM_USER_DATA_SHORT_VALUE,CUSTOM_USER_DATA_LONG_VALUE, true, CUSTOM_USER_DATA_DESCRIPTION)

    static final String INFRA_NAME_SHORT_VALUE = "n"
    static final String INFRA_NAME_LONG_VALUE = "infra-name"
    static final String INFRA_NAME_DESCRIPTION = "name of infrastructure/stack"
    static final Option INFRA_NAME_OPTION = new Option(INFRA_NAME_SHORT_VALUE,INFRA_NAME_LONG_VALUE, true, INFRA_NAME_DESCRIPTION)

    private static final Options OPTIONS

    static{
        OPTIONS = new OptionsBuilder().addOption(ACTION_OPTION)
                .addOption(TEMPLATE_OPTION)
                .addOption(CUSTOM_USER_DATA_OPTION)
                .addOption(CONFIG_OPTION)
                .addOption(INFRA_NAME_OPTION)
                .addOption(HELP_OPTION)
                .build()
    }

    @Override
    public Options getOptions() {
        return OPTIONS
    }
}
