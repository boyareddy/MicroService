#!/bin/bash
systemctl start connectsecurity.service
echo "connectsecurity have been started."
systemctl start connectmetadata.service
echo "connectmetadata have been started."
systemctl start connectadminapi.service
echo "connectadminapi have been started."
systemctl start connectemail.service
echo "connectemail have been started."
systemctl start connectmailsender.service
echo "connectmailsender have been started."
systemctl start connectaudittrail.service
echo "connectaudittrail have been started."
systemctl start connectmanagement.service
echo "connectmanagement have been started."
systemctl start connectamm.service
echo "connectamm have been started."
systemctl start connectomm.service
echo "connectomm have been started."
systemctl start connectimm.service
echo "connectimm have been started."
systemctl start connectrmm.service
echo "connectrmm have been started."
systemctl start connectwfm.service
echo "connectwfm have been started."
systemctl start connectadm.service
echo "connectadm have been started."
systemctl start connectmp24adapter.service
echo "connectmp24adapter have been started."
systemctl start connectmp96adapter.service
echo "connectmp96adapter have been started."
systemctl start connectlp24adapter.service
echo "connectlp24adapter have been started."
systemctl start connectdpcradapter.service
echo "connectdpcradapter have been started."
systemctl start connecthtpadapter.service
echo "connecthtpadapter have been started."
echo "Connect microservices have been started."
