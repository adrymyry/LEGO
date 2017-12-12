#!/bin/bash

echo "Ficheros SNMPManejador-91"
scp snmp.fb.com:/etc/network/interfaces ../ServerSNMPManejador-91/files/
scp snmp.fb.com:/etc/snmp/snmp.conf ../ServerSNMPManejador-91/files/
scp snmp.fb.com:/etc/snmp/snmptrapd.conf ../ServerSNMPManejador-91/files/

echo "Ficheros SNMPManejador-92"
scp snmp.ig.com:/etc/network/interfaces ../ServerSNMPManejador-92/files/
scp snmp.ig.com:/etc/snmp/snmp.conf ../ServerSNMPManejador-92/files/
scp snmp.ig.com:/etc/snmp/snmptrapd.conf ../ServerSNMPManejador-92/files/

echo "Ficheros LDAP-91"
scp ldap.fb.com:/etc/network/interfaces ../ServerLDAP-91/files/
scp ldap.fb.com:fb.users ../ServerLDAP-91/files/
scp ldap.fb.com:fbhackamonth.users ../ServerLDAP-91/files/

echo "Ficheros LDAP-92"
scp ldap.ig.com:/etc/network/interfaces ../ServerLDAP-92/files/
scp ldap.ig.com:sta.ldif ../ServerLDAP-92/files/

echo "Ficheros CentralitaVoIP-91"
scp asterisk.fb.com:/etc/network/interfaces ../ServerCentralitaVoIP-91/files/
scp asterisk.fb.com:/etc/asterisk/sip.conf ../ServerCentralitaVoIP-91/files/
scp asterisk.fb.com:/etc/asterisk/users.conf ../ServerCentralitaVoIP-91/files/
scp asterisk.fb.com:/etc/asterisk/extensions.conf ../ServerCentralitaVoIP-91/files/

echo "Ficheros CentralitaVoIP-92"
scp asterisk.ig.com:/etc/network/interfaces ../ServerCentralitaVoIP-92/files/
scp asterisk.ig.com:/etc/asterisk/sip.conf ../ServerCentralitaVoIP-92/files/
scp asterisk.ig.com:/etc/asterisk/users.conf ../ServerCentralitaVoIP-92/files/
scp asterisk.ig.com:/etc/asterisk/extensions.conf ../ServerCentralitaVoIP-92/files/

echo "Ficheros Owncloud-91"
scp owncloud.fb.com:/etc/network/interfaces ../ServerOwncloud-91/files/
scp owncloud.fb.com:/var/www/owncloud/config/config.php ../ServerOwncloud-91/files/

echo "Ficheros Radius-91"
scp radius.fb.com:/etc/network/interfaces ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/sites-available/default ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/sites-available/inner-tunnel ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/dictionary ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/ldap.attrmap ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/eap.conf ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/modules/ldap ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/clients.conf ../ServerRadius-91/files/
scp radius.fb.com:/etc/freeradius/users.conf ../ServerRadius-91/files/

echo "Ficheros Radius-92"
scp radius.ig.com:/etc/network/interfaces ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/sites-available/default ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/sites-available/inner-tunnel ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/dictionary ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/ldap.attrmap ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/eap.conf ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/modules/ldap ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/clients.conf ../ServerRadius-92/files/
scp radius.ig.com:/etc/freeradius/users.conf ../ServerRadius-92/files/


