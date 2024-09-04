#!/bin/bash
cd /home/ec2-user/app
echo "init" > start_server_log.txt

if [ -f .env ]; then
    export $(cat .env | xargs)
    echo "Environment variables loaded:" >> start_server_log.txt
    cat .env >> start_server_log.txt
fi

export SPRING_PROFILES_ACTIVE=prod
echo "SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE" >> start_server_log.txt

nohup java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE todotravel-0.0.1-SNAPSHOT.jar > applog.txt 2> errorlog.txt &
echo "Application started with SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE" >> start_server_log.txt
