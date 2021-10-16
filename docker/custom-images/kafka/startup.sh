#!/bin/bash

broker_id=$1
server_name=$2

CONFIG_FILE=/opt/kafka/config/server.properties

{
    echo "broker.id=$broker_id";
    echo "advertised.listeners=INTERBROKER://$server_name:29092,CLIENTS://localhost:909${broker_id}";
    echo "listeners=INTERBROKER://:29092,CLIENTS://:909${broker_id}";
    echo "";
} >> $CONFIG_FILE

#Start Kafka
/opt/kafka/bin/kafka-server-start.sh $CONFIG_FILE