#!/bin/bash

# Instalar snmpd, snmp-mibs-downloader y snmp
sudo apt-get -y install snmpd snmp-mibs-downloader snmp

# Detenemos el servicio para que no falle al configurarlo
sudo service snmpd stop

# Cargamos el fichero de configuracion
sudo cp ./snmpdAgente$1.conf /etc/snmp/snmpd.conf

# Arrancamos el servicio
sudo service snmpd start

