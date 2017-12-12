#!/bin/bash

# Instalar soporte para VLAN
sudo apt-get update
sudo apt-get install -y vlan
sudo modprobe 8021q
sudo su -c 'echo "8021q" >> /etc/modules'

# Desactivar gestor red
sudo service network-manager stop

# Editar /etc/network/interfaces
echo "# interfaces(5) file used by ifup(8) and ifdown(8)
auto lo
iface lo inet loopback

auto eth0
iface eth0 inet dhcp

auto eth1.91 
iface eth1.91 inet static 
address 192.168.91.1 
netmask 255.255.255.0
dns-nameservers 192.168.91.2
vlan-raw-device eth1

auto eth1.92
iface eth1.92 inet static
address 192.168.92.1
netmask 255.255.255.0
dns-nameservers 192.168.92.2
vlan-raw-device eth1" > /etc/network/interfaces

# Reiniciar interfaces
sudo ifdown eth0
sudo ifup eth0
sudo ifup eth1.91
sudo ifup eth1.92

# Activar routing/forwarding
sudo sysctl -w net.ipv4.ip_forward=1

# Activar NAT en router trafico externo
sudo iptables -t nat -A POSTROUTING ! -d 192.168.0.0/16 -o eth0 -j MASQUERADE
sudo iptables-save
sudo apt-get install -y iptables-persistent
sudo dpkg-reconfigure iptables-persistent






