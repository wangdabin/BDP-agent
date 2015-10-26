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

# if no args specified, show usage
if [ $# = 0 ]; then
  echo "Usage: spider COMMAND"
  echo "where COMMAND is one of:"
  echo "  init    initParam"
  echo " or"
  echo "  CLASSNAME         run the class named CLASSNAME"
  echo "Most commands print help when invoked w/o parameters."
  exit 1
fi

# get arguments
COMMAND=$1
shift

if [ -z "$JAVA_HOME" ];then
	export JAVA_HOME=$1
	echo "JAVA_HOME = $JAVA_HOME"
fi

if [ ! -d "$BIGDATA_HOME/logs" ];then
   mkdir -p $BIGDATA_HOME/logs 
fi

export HADOOP_USER_CLASSPATH_FIRST=true
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

export HADOOP_CLASSPATH="${BIGDATA_CLASSPATH}"

# figure out which class to run
if [ "$COMMAND" = "init" ] ; then
  CLASS="com.sky.agent.init.InitParam"
else
  CLASS=$COMMAND
fi

$JAVA_HOME/bin/java $LIBRARY_PATH -cp $BIGDATA_CLASSPATH $CLASS $@