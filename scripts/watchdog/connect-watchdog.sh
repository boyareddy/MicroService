#!/bin/sh
JSON_FILE_PATH="/opt/roche/connect/scripts/watchdog/watchdogurls.json"
service_wait_time=$(jq -c '.service_wait_time' $JSON_FILE_PATH)
shutdown_wait_time=$(jq -c '.shutdown_wait_time' $JSON_FILE_PATH)
LOG_FILE_PATH=$(jq -c -r '.log_file_path' $JSON_FILE_PATH)
LOGIN_URL=$(jq -c -r '.login_url' $JSON_FILE_PATH)
WATCHDOG_RESTART_TIME=$(jq -c '.watchdog_restart_time' $JSON_FILE_PATH)
connect_watchdog(){

SERVICEURL=$(_jq '.service_url')
SERVICENAME=$(_jq '.servicename')
JAR_FILE_PATH=$(_jq '.jar_file_path')
SERVICE_START_TIME=$(_jq '.service_start_time')
LOG_FILE_NAME=$(_jq '.log_file_name')
JAR_MEMORY=$(_jq '.jar_memory')
YAML_LOCATION=$(_jq '.yaml_location')
JAR_MEMORY=$(echo $JAR_MEMORY | tr "/" " ")
PID_STATUS=$(ps -ef | grep -v grep | grep java | grep $SERVICENAME | wc -l)
logger -s $date "Watchdog going to test with" $SERVICENAME 2>> $LOG_FILE_PATH
logger -s $date "Watchdog trying to connect" $SERVICEURL 2>> $LOG_FILE_PATH
BROWNSTONEAUTHCOOKIE=$( curl --insecure -s -X POST --header "Content-Type: application/x-www-form-urlencoded" --header "Accept: text/plain" -d "j_username=admin&j_password=pas123&org=hcl.com" $LOGIN_URL )
SECURITYSERVICE=$(curl --insecure --cookie "brownstoneauthcookie=$BROWNSTONEAUTHCOOKIE" -X GET --header "Accept: application/json" -s -o /dev/null -w '%{http_code}\n' $SERVICEURL)
SERVICE_POLL_RESULT=$( curl --silent -k $SERVICEURL | tr -d '\r')
echo $SECURITYSERVICE
    if [ "$SECURITYSERVICE" -eq 200 ] && [ "$SERVICE_POLL_RESULT" != "no" ];
    then
	logger -s  $SERVICENAME "is running successfully..." 2>> $LOG_FILE_PATH
	echo "$SERVICENAME service running, everything is fine"
    else
	logger -s $SERVICENAME "service is not running successfully need to restart..." 2>> $LOG_FILE_PATH
    	if [ "$PID_STATUS" -gt 0 ]
    	then
            echo "$SERVICENAME PID is running...."
	    echo  "going to kill ----" $SERVICENAME
	    logger -s "PID is running going to kill" $SERVICENAME 2>> $LOG_FILE_PATH
	    pkill $SERVICENAME
	    sleep $shutdown_wait_time
	    PID_STATUS=$(ps -ef | grep -v grep | grep java | grep $SERVICENAME | wc -l)
	    if [ "$PID_STATUS" -gt 0 ]
	    then
	    	pkill -f $SERVICENAME
		    PID_STATUS=$(ps -ef | grep -v grep | grep java | grep $SERVICENAME | wc -l)
			PROCESS_PID=$(ps -ef | grep java | grep -v sudo | grep $SERVICENAME | awk '{print $2}')
			if [ "$PID_STATUS" -gt 0 ]
			then
				kill -9 $PROCESS_PID
	    	else
				echo "pkill force not successfull"
			fi
	    	else
	    	echo "pkill force not successfull"
	    fi
		
	    logger -s $SERVICENAME "PID killed successfully..." 2>> $LOG_FILE_PATH
	    echo "$SERVICENAME PID killed successfully..."
        sleep 5
	    logger -s "Going to start" $SERVICENAME 2>> $LOG_FILE_PATH
	    echo "$SERVICENAME start inprogress-----------"
	    nohup java -jar $JAR_MEMORY -Dspring.config.location=$YAML_LOCATION $JAR_FILE_PATH/$SERVICENAME > /dev/null 2>&1 &
	    sleep $SERVICE_START_TIME
        else
	    logger -s "Going to start" $SERVICENAME 2>> $LOG_FILE_PATH
	    nohup java -jar $JAR_MEMORY -Dspring.config.location=$YAML_LOCATION $JAR_FILE_PATH/$SERVICENAME > /dev/null 2>&1 &
        echo "$SERVICENAME start inprogress.."
	    sleep $SERVICE_START_TIME
        fi
    fi

}

while true
do
for i in $(jq -c '.urls[]' $JSON_FILE_PATH); do
    _jq() {
     echo ${i}| jq -r ${1}
    }
connect_watchdog
done
sleep $WATCHDOG_RESTART_TIME
done
