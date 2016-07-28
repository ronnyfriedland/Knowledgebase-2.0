FROM azul/zulu-openjdk:7

MAINTAINER Ronny Friedland &lt;mail@ronnyfriedland.de&gt;

RUN apt-get update \
        && apt-get install -y unzip \
        && apt-get clean

RUN mkdir -p /opt/knowledgebase

RUN cp -r zip/* /opt/knowledgebase

RUN ln -s /opt/knowledgebase/knowledgebase.sh /etc/init.d/knowledgebase

RUN update-rc.d knowledgebase defaults

RUN service knowledgebase start

EXPOSE 15566
