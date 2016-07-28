FROM debian:jessie

MAINTAINER Ronny Friedland &lt;mail@ronnyfriedland.de&gt;

RUN apt-get update \
        && apt-get install -y openjdk-7-jre unzip \
        && apt-get clean

ADD opt/knowledgebase.zip /opt

RUN unzip /opt/knowledgebase.zip -d /opt/knowledgebase

EXPOSE 15566
