#!/bin/bash
systemctl stop postgresql
systemctl stop tomcat8
systemctl stop rabbitmq-server
echo "Connect related 3PP services have been stopped."