#!/bin/bash

#Montamos DNS Server
sudo apt-get update
sudo apt-get install -y bind9
sudo cp ../ServerDNS-92/files/* /etc/bind/
sudo service bind9 start
