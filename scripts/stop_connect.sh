#!/bin/bash

connect_stop(){
service_name=$1
PID_STATUS_S=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
	if [ "$PID_STATUS_S" -gt 0 ]
		then
		echo  "Terminating " $service_name
	    	pkill -f -15 $service_name
		sleep 5
		PID_STATUS_F=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
		if [ "$PID_STATUS_F" -gt 0 ]
		then
		echo  "Force terminating " $service_name
		pkill -f -9 $service_name
		sleep 2
		PID_STATUS_PID=$(ps -ef | grep -v grep | grep java | grep $service_name | wc -l)
		PROCESS_PID=$(ps -ef | grep java | grep -v sudo | grep $service_name | awk '{print $2}')
			if [ "$PID_STATUS_PID" -gt 0 ]
			then
				kill -9 $PROCESS_PID
			fi
		fi
	fi
}

connect_stop 'amm-'
connect_stop 'omm-'
connect_stop 'rmm-'
connect_stop 'imm-'
connect_stop 'wfm-'
connect_stop 'adm-'
connect_stop 'adapter-htp-forte-'
connect_stop 'adapter-mp24-'
connect_stop 'adapter-lp24-'
connect_stop 'adapter-mp96-.jar'
connect_stop 'adapter-dpcr-'

echo "Connect microservices have been stopped."

