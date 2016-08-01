FROM debian:jessie

MAINTAINER Ronny Friedland &lt;mail@ronnyfriedland.de&gt;

RUN apt-get update \
        && apt-get install -y openjdk-7-jre unzip \
        && apt-get clean

# Unzip application
ADD opt/knowledgebase.zip /opt
RUN unzip /opt/knowledgebase.zip -d /opt/knowledgebase

# Install application as a service
RUN ln -s /opt/knowledgebase/knowledgebase.sh /etc/init.d/knowledgebase
RUN update-rc.d knowledgebase defaults

# Run service
RUN service knowledgebase start

EXPOSE 15566
