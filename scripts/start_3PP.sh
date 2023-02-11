#!/bin/bash
systemctl start postgresql
systemctl start tomcat8
systemctl start rabbitmq-server
echo "Connect related 3PP services have been started."