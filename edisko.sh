#!/bin/bash                                                                                                                                                                                            
CWD=`echo $(dirname $(readlink -f $0))`                                                                                                                                                                
cd `dirname $(readlink -f $0)`                                                                                                                                                                         
                                                                                                                                                                                                       
source $CWD/edisko-init.sh                                                                                                                                                                                 
                                                                                                                                                                                                       
# command:                                                                                                                                                                                             
EXEC="$JAVA_HOME/bin/java -Xss512M -cp $CP $AGENT $OPT io.syspulse.edisko.server.Boot $@"

# With fork
#sbt '; set javaOptions += "-Dlogback.configurationFile=./conf/logback.xml"; run-main com.syspulse.vault.client.EditorClientShell'
# Without fork
#sbt '; eval System.setProperty("logback.configurationFile","./conf/logback.xml"); run-main com.syspulse.vault.client.EditorClientShell' 

exec $EXEC 
