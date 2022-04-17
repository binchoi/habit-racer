#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=habit-racer

echo "> Copy Build File"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Check the pid of the current running application"

CURRENT_PID=$(pgrep -fl habit-racer | grep java | awk '{print $1}')

echo "Current application pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> There is no current running application. No termination is required."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> New application deployment"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> add execution permission to $JAR_NAME"

chmod +x $JAR_NAME

echo "> Run $JAR_NAME"

nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &