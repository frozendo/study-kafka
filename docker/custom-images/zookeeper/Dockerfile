FROM debian:latest

RUN DEBIAN_FRONTEND=noninteractive apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install curl
RUN curl -Lo /tmp/java_17.deb \ 
    https://cdn.azul.com/zulu/bin/zulu17.28.13-ca-jdk17.0.0-linux_amd64.deb
RUN dpkg -i /tmp/java_17.deb; apt-get -yf install \
    && rm /tmp/java_17.deb

RUN DEBIAN_FRONTEND=noninteractive apt-get -y install jq
RUN curl -o /opt/zookeeper.tgz "https://dlcdn.apache.org/zookeeper/zookeeper-3.7.0/apache-zookeeper-3.7.0-bin.tar.gz"

RUN cd /opt && tar xzf zookeeper.tgz && mv apache-zookeeper* zookeeper && rm zookeeper.tgz

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y procps
RUN mkdir -p /var/zookeeper
COPY zoo.cfg /opt/zookeeper/conf
COPY startup.sh /opt/zookeeper-startup.sh

RUN chmod +x /opt/zookeeper-startup.sh

CMD /opt/zookeeper-startup.sh $SERVER_ID