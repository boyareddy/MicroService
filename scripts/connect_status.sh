host="www.qa-rocheconnect.com"

echo "Security:"  `curl --silent -k https://$host:90/security-web/poll`

echo "Metadata:"  `curl --silent -k https://$host:91/metadata-web/poll`

echo "Email:"  `curl --silent -k https://$host:94/email-web/poll`

echo "Admin (CARE):"  `curl --silent -k https://$host:92/admin-api/poll`

echo "Device Mgmt:"  `curl --silent -k https://$host:85/device-management/poll`

echo "Audit Log:"  `curl --silent -k https://$host:73/audit-trail-boot/poll`

echo "Workflow:"  `curl --silent -k https://$host:99/wfm/poll`

echo "Integration:"  `curl --silent -k https://$host:9090/imm/poll`

echo "Assay Config:"  `curl --silent -k https://$host:88/amm/poll`

echo "Admin (Connect):"  `curl --silent -k https://$host:97/adm/poll`

echo "Results:"  `curl --silent -k https://$host:86/rmm/poll`

echo "Order:"  `curl --silent -k https://$host:96/omm/poll`

echo "MP24:"  `curl --silent -k https://$host:87/adapter-mp24/poll`

echo "LP:"  `curl --silent -k https://$host:89/adapter-lp24/poll`

echo "HTP:"  `curl --silent -k https://$host:98/poll`

echo "dPCR:"  `curl --silent -k https://$host:76/adapter-dpcr/poll`

echo "MP96:"  `curl --silent -k https://$host:79/adapter-mp96/poll`

echo "Tomcat:" `systemctl is-active --quiet tomcat8 && echo yes || echo no`

echo "RabbitMQ:" `systemctl is-active --quiet rabbitmq-server && echo yes || echo no`

