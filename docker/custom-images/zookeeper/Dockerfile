FROM debian:latest

RUN DEBIAN_FRONTEND=noninteractive apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install curl procps openjdk-17-jdk

RUN curl -o /opt/zookeeper.tgz "https://dlcdn.apache.org/zookeeper/zookeeper-3.7.0/apache-zookeeper-3.7.0-bin.tar.gz" \
    && cd /opt && tar xzf zookeeper.tgz && mv apache-zookeeper* zookeeper && rm zookeeper.tgz \
    && mkdir -p /var/zookeeper

COPY zoo.cfg /opt/zookeeper/conf
COPY startup.sh /opt/zookeeper-startup.sh

RUN chmod +x /opt/zookeeper-startup.sh

CMD /opt/zookeeper-startup.sh $SERVER_ID