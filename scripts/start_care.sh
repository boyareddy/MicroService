#!/bin/bash
#### CARE microservices ####
sudo nohup java -jar -Xms512M -Xmx1024M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_security.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/security-boot-4.4.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms128M -Xmx256M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_metadata.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/metadata-boot-4.4.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms128M -Xmx256M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_adminapi.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/administration-api-boot-4.4.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms128M -Xmx256M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_email.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/email-api-boot-4.4.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms128M -Xmx256M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_Management.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/management-boot-4.4-SNAPSHOT.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms512M -Xmx1024M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_audit.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/audit-trail-boot-4.4.jar >/dev/null 2>&1 &
sudo nohup java -jar -Xms128M -Xmx256M -Dspring.config.location=/opt/roche/connect/yaml_runtime/application_mailsender.yaml,/opt/roche/connect/yaml_runtime/application-connectcommon.yaml /opt/roche/connect/bin/CARE/mailsender-boot-4.4.jar >/dev/null 2>&1 &
echo "CARE microservices have been started."
