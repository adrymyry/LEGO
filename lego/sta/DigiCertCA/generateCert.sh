#!/bin/bash

CA_PATH = /home/alumno/demoCA

# Crea directorio para ordenar certs
mkdir $CA_PATH$1

echo "Generando solicitud"
openssl req -new -nodes -newkey rsa:2048 -keyout $CA_PATH$1/$1key.pem -out $CA_PATH$1/$1csr.pem -days 365

echo "Firmando solicitud"
openssl ca -keyfile private/cakey.pem -in $CA_PATH$1/$1csr.pem -out $CA_PATH$1/$1cert.pem

echo "Generando fichero cliente"
openssl pkcs12 -export -in $CA_PATH$1/$1cert.pem -certfile cacert.pem -inkey $CA_PATH$1/$1key.pem -out $CA_PATH$1/$1cert.pfx
