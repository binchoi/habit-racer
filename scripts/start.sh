#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=habit-racer

echo "> Copy Build file"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Deploying new application"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n -1)

echo "> JAR Name: $JAR_NAME"

echo "> add execution permission to $JAR_NAME"
chmod +x $JAR_NAME

echo "> Run $JAR_NAME"

IDLE_PROFILE=$(find_idle_profile)

echo "> Run $JAR_NAME with profile=$IDLE_PROFILE"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &