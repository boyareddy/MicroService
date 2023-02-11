#!/bin/bash
systemctl stop connectsecurity.service
echo "connectsecurity have been stopped."
systemctl stop connectmetadata.service
echo "connectmetadata have been stopped."
systemctl stop connectadminapi.service
echo "connectadminapi have been stopped."
systemctl stop connectemail.service
echo "connectemail have been stopped."
systemctl stop connectmailsender.service
echo "connectmailsender have been stopped."
systemctl stop connectaudittrail.service
echo "connectaudittrail have been stopped."
systemctl stop connectmanagement.service
echo "connectmanagement have been stopped."
systemctl stop connectamm.service
echo "connectamm have been stopped."
systemctl stop connectomm.service
echo "connectomm have been stopped."
systemctl stop connectimm.service
echo "connectimm have been stopped."
systemctl stop connectrmm.service
echo "connectrmm have been stopped."
systemctl stop connectwfm.service
echo "connectwfm have been stopped."
systemctl stop connectadm.service
echo "connectadm have been stopped."
systemctl stop connectmp24adapter.service
echo "connectmp24adapter have been stopped."
systemctl stop connectmp96adapter.service
echo "connectmp96adapter have been stopped."
systemctl stop connectlp24adapter.service
echo "connectlp24adapter have been stopped."
systemctl stop connectdpcradapter.service
echo "connectdpcradapter have been stopped."
systemctl stop connecthtpadapter.service
echo "connecthtpadapter have been stopped."
echo "Connect microservices have been stopped."
