#!/bin/bash

server_id=$1

#Configuration
echo "$server_id" > /var/zookeeper/myid

#Start Zookeeper
/opt/zookeeper/bin/zkServer.sh start-foreground