FROM debian:latest

RUN DEBIAN_FRONTEND=noninteractive apt-get update \ 
    && DEBIAN_FRONTEND=noninteractive apt-get -y install curl openjdk-17-jdk

RUN curl -o /opt/kafka.tgz "https://dlcdn.apache.org/kafka/3.0.0/kafka_2.12-3.0.0.tgz" \ 
    && cd /opt && tar xzf kafka.tgz && mv kafka_* kafka && rm kafka.tgz \ 
    && mkdir -p /var/kafka/data

COPY server.properties /opt/kafka/config/
COPY startup.sh /opt/kafka-startup.sh

RUN chmod +x /opt/kafka-startup.sh

CMD /opt/kafka-startup.sh $BROKER_ID $BROKER_NAME