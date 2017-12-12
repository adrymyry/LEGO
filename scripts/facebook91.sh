#!/bin/bash

# Desactivar gestor red
sudo service network-manager stop

# Editar /etc/network/interfaces
echo "# interfaces(5) file used by ifup(8) and ifdown(8)
auto lo
iface lo inet loopback

auto eth0
iface eth0 inet static
address 192.168.91.2
netmask 255.255.255.0
gateway 192.168.91.1
dns-nameservers 192.168.91.2" > /etc/network/interfaces

# Reiniciar interfaces
sudo ifdown eth0
sudo ifup eth0

# Asignamos manualmente por si falla
sudo ifconfig eth0 192.168.91.2/24
sudo route add default gw 192.168.91.1

#Montamos DNS Server
sudo apt-get update
sudo apt-get install -y bind9
sudo cp ../ServerDNS-91/files/* /etc/bind/
sudo service bind9 start
