#!/bin/bash
CWD=`echo $(dirname $(readlink -f $0))`
cd `dirname $(readlink -f $0)`

source edisko-cred.sh

SCALA_VERSION=2.12

# ATTENTION: must be created by sbt
echo "INF: resolving libraries..."
CP=`find lib_managed/ -name "*.jar" | paste -sd ":" -`

EDO_GCS=`find -L edo-gcs/target/scala-$SCALA_VERSION -name "edo*.jar" | paste -sd ":" -`
EDO_SERVER=`find -L edo-server/target/scala-$SCALA_VERSION -name "edo*.jar" | paste -sd ":" -`
EDO_PLUGINS=`find -L ./plugins/ -name "*.jar" | paste -sd ":" -`

CP=`pwd`/conf/:$EDO_GCS:$EDO_SERVER:$EDO_PLUGINS:$CP

echo "CP=$CP"| sed "s/\:/\n/g"
echo "OPT=$OPT"

