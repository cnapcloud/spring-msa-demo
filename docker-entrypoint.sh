#!/bin/sh
HOSTNAME=`hostname`
if [ -z "$API_NAME" ];
  then
    API_NAME=project-service
fi
exec java -jar ${JAVA_OPTS} -Dspring.profiles.active=${PROFILE} `ls app/${API_NAME}*-SNAPSHOT.jar |tail -n1`