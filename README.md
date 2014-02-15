akka-cluster-service-locator
============================

A very simple and quick prototype to model distrinuted services fremawork.
Frmework features:
  - service registry: watches the cluster for new nodes, and discovers available services
  - service provider(s): when added to cluster dynamically register their services with locator
  - service client(s): locate services they need usind service locator, then send requests directly to located services
  
  
# Setting up development environment

## Clone
First use [git](http://git-scm.com/) to clone this repo:

    git clone {TODO: add stash URL}
    cd akka-cluster-service-locator

## Build
UMF is built with [Maven](http://maven.apache.org/).

    mvn clean install

The above will build the prototype and run all unit tests.

## Run service locator (open new Terminal tab)
    mvn exec:java -DmainClass=com.prystupa.ServiceRegistryApp

## Run Bridge services (open new Terminal tab)
    cd quickfixj-bridge
    mvn exec:java

## Run Venue services (open new Terminal tab)
    cd quickfixj-venue
    mvn exec:java

## Explore
* Monitor the logs output to the console by each applications
* Try bringing registry down and then back
* Try bringing service provider down and then back
* Try calling client when service provider is down
