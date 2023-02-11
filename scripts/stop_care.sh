#!/bin/bash

connect_stop(){
service_name=$1
PID_STATUS_S=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
	if [ "$PID_STATUS_S" -gt 0 ]
		then
		echo  "Terminating " $service_name
	    	pkill $service_name
		sleep 5
		PID_STATUS_F=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
		if [ "$PID_STATUS_F" -gt 0 ]
		then
		echo  "Force terminating " $service_name
		pkill -f $service_name
		PID_STATUS_PID=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
		PROCESS_PID=$(ps -ef | grep java | grep -v sudo | grep $service_name | awk '{print $2}')
			if [ "$PID_STATUS_PID" -gt 0 ]
			then
				kill -9 $PROCESS_PID
			fi
		fi
	fi
}

connect_stop 'security-boot'
connect_stop 'metadata-boot'
connect_stop 'administration-api-boot'
connect_stop 'administration-ui-boot'
connect_stop 'email-api-boot'
connect_stop 'management-boot'
connect_stop 'audit-trail-boot'
connect_stop 'mailsender-boot'
echo "CARE microservices have been stopped."