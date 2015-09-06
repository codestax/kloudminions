
Command Line Quick Start Up Guide

Build The project with dependencies

gradle jarWithDependencies

You'll need to create API credentials for your user account as well.
NOTE: aws_credentials MUST not be placed anywhere near the project directory.  Ideally store it in a protected folder in your home directory

Change the name of the "aws_stack_name" parameter in the "process_context.json" file to whatever you like to avoid stepping on each other's toes.
Also, change the names of the files testProcSpec.json and process_context.json to your own version

Run this inside the "cli" directory

java -jar build/libs/cloudminions.jar -a RUN_PROCESS -p practice/rashad_ProcSpec.json -c ~/.minions/aws_credentials.json -t practice/rashad_process_context.json


Let me know if you have any trouble.

