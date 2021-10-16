FROM debian:latest

RUN DEBIAN_FRONTEND=noninteractive apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install curl
RUN curl -Lo /tmp/java_17.deb \ 
    https://cdn.azul.com/zulu/bin/zulu17.28.13-ca-jdk17.0.0-linux_amd64.deb
RUN dpkg -i /tmp/java_17.deb; apt-get -yf install \
    && rm /tmp/java_17.deb

RUN DEBIAN_FRONTEND=noninteractive apt-get -y install jq
RUN curl -o /opt/kafka.tgz "https://dlcdn.apache.org/kafka/3.0.0/kafka_2.12-3.0.0.tgz"

RUN cd /opt && tar xzf kafka.tgz && mv kafka_* kafka && rm kafka.tgz

COPY server.properties /opt/kafka/config/
COPY startup.sh /opt/kafka-startup.sh
RUN mkdir -p /var/kafka/data

RUN chmod +x /opt/kafka-startup.sh

CMD /opt/kafka-startup.sh $BROKER_ID  $BROKER_NAME