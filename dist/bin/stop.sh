#!/bin/bash

THIS="$0"

while [ -h "$THIS" ]; do
ls=`ls -ld "$THIS"`
link=`expr "$ls" : '.*-> \(.*\)$'`
if expr "$link" : '.*/.*' > /dev/null; then
THIS="$link"
else
THIS=`dirname "$THIS"`/"$link"
fi
done

THIS_DIR=`dirname "$THIS"`
BIGDATA_HOME=`cd "$THIS_DIR/.." ; pwd`
cd $BIGDATA_HOME
source $BIGDATA_HOME/bin/env.sh

if [ ! -d "$BIGDATA_HOME/logs" ];then
   mkdir -p $BIGDATA_HOME/logs 
fi

BIGDATA_CLASSPATH=""
if [ -d "${BIGDATA_HOME}/conf/web" ] || [ -d "${BIGDATA_HOME}/conf/application" ];then
   if [ -d "${BIGDATA_HOME}/conf/application" ];then
   	 BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/conf/application
   fi
   if [ -d "${BIGDATA_HOME}/conf/web" ];then
     BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/conf/web
   fi
elif [ -d "${BIGDATA_HOME}/conf" ];then
  BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/conf
fi

if [ -d "${BIGDATA_HOME}/lib" ];then
   jarLibs=$(ls ${BIGDATA_HOME}/lib)
   for jarLib in $jarLibs;do
      BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/lib/${jarLib}
   done
fi

if [ -d "${BIGDATA_HOME}/command" ];then
   jarLibs=$(ls ${BIGDATA_HOME}/command)
   for jarLib in $jarLibs;do
      BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/command/${jarLib}
   done
fi

if [ -d "${BIGDATA_HOME}/lib/native" ];then
   LIBRARY_PATH="-Djava.library.path=${BIGDATA_HOME}/lib/native"
fi

COMMAND="com.sky.http.StopServer"
$JAVA_HOME/bin/java -DBIGDATA.HOME=${BIGDATA_HOME} -DSTOP.PORT=$STOP_PORT -DSTOP.KEY=$STOP_KEY -Djava.home=$JAVA_HOME $LIBRARY_PATH -cp $BIGDATA_CLASSPATH com.sky.http.StopServer
