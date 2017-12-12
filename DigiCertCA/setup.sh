#!/bin/bash

# Configuramos interfaces de red
cp ../DigiCertCA/files/interfaces /etc/network/interfaces

# Copiamos ficheros CA
cp -r ../DigiCertCA/files/demoCA/ /home/alumno/
cp ../DigiCertCA/files/openssl.cnf /usr/lib/ssl/

# Montamos DNS Server
apt-get update
apt-get install -y bind9
cp ../DigiCertCA/files/named* /etc/bind/
cp ../DigiCertCA/files/db.digicert.com.zone /etc/bind/
service bind9 start

# Modificamos crontab para que genere crl de la CA
crontab -l > mycron
echo "@daily openssl ca -keyfile /home/alumno/demoCA/private/cakey.pem -gencrl -out /home/alumno/demoCA/crl/ca-crl.pem" >> mycron
crontab mycron
rm mycron

# Instalamos Apache para servir la crl
apt-get install -y apache2

# Creamos un enlace símbolico para servir la crl con Apache
ln -s /home/alumno/demoCA/crl/ca-crl.pem /var/www/ca-crl.pem

# Montamos OCSP Server
openssl ocsp -port 127.0.0.1:3333 -text -sha256\
 -index /home/alumno/demoCA/index.txt\
 -CA /home/alumno/demoCA/cacert.pem\
 -rkey /home/alumno/demoCA/ocsp/ocspkey.pem\
 -rsigner /home/alumno/demoCA/ocsp/ocspcert.pem &
