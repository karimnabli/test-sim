Test Data Capture Simulator: test-data-capture-sim
==================================================

## Introduction

This project is composed into 2 parts: api-mock and web-mock
api-mock aims to simulate the calls to different api endpoints
web-mock aims to deploy a static website in order to used to generate data capture events
## Prerequisites

Java 8 or more

maven 3.5 or more

## Api mock
### Getting started
api-mock is designed to serve 2 endpoints which are: 

    /myget

    /mypost
    

### Structure
```
    api-mock
    ├── pom.xml
    ├── src
        └── main
            └── webapp
                └── WEB-INF
                    ├── mock
                    │   ├── files
                    │   │   ├── get.json
                    │   │   └── post.json
                    │   └── mappings
                    │       ├── get.json
                    │       └── post.json
                    └── web.xml
```
The mappings files contain the endpoint definitions
The __files contain the stub response. these response are static. for dynamic response this project need to be extended.   

### Build and run locally
```shell
$ mvn clean install
$ mvn jetty:run
```
You can check if all working fine, go to (http://localhost:8885/api/config)[http://localhost:8885/api/config]

## Web mock
### Getting started

web-mock is designed to host static page

### Structure
The project structure look like the following: 

```
    web-mock
  ├── pom.xml
  └── src
       └── main
            └── webapp
                   ├── index.html
                   ├── pages
                   │     ├── test-events.js
                   │     └── testpage.html
                   └── web.xml

```

## Api and Web mock deployment
### Method 1
```shell
$ mvn clean install
```
once the api-mock-[x.y].war and web-mock-[x.y].war are generated run the following 

```shell
$ java -jar jetty-runner-9.4.9.v20180320.jar --port 8885 --path / web-mock-0.1.war --path /mock api-mock-0.1.war
```

the static pages should be deployed under "/" and the api mock is deployed under "/mock" path. 

### Method 2 
in the level of test-datacapture-sim run 
```shell
$ mvn clean install -Passembly
```
in data-capture-sim/target there is appassembler generated output 

```
	appassembler/
	├── bin
	│   ├── start.bat
	│   ├── start.sh
	│   ├── stop.bat
	│   └── stop.sh
	├── conf
	│   ├── api-mock-1.0.war
	│   ├── application.properties
	│   └── web-mock-1.0.war
	└── repo
	    ├── apache-el-8.5.24.2.jar
	    ├── apache-jsp-8.5.24.2.jar
	    ├── apache-jsp-9.4.9.v20180320.jar
	    ├── apache-jstl-9.4.9.v20180320.jar
	    ├── asm-6.0.jar
	    ├── asm-commons-6.0.jar
	    ├── asm-tree-6.0.jar
	    ├── data-capture-sim-0.1.jar
	    ├── ecj-3.12.3.jar
	    ├── javax.annotation-api-1.2.jar
	    ├── javax.servlet-api-3.1.0.jar
	    ├── jetty-annotations-9.4.9.v20180320.jar
	    ├── jetty-client-9.4.9.v20180320.jar
	    ├── jetty-http-9.4.9.v20180320.jar
	    ├── jetty-io-9.4.9.v20180320.jar
	    ├── jetty-jaas-9.4.9.v20180320.jar
	    ├── jetty-jmx-9.4.9.v20180320.jar
	    ├── jetty-jndi-9.4.9.v20180320.jar
	    ├── jetty-plus-9.4.9.v20180320.jar
	    ├── jetty-runner-9.4.9.v20180320.jar
	    ├── jetty-schemas-3.1.jar
	    ├── jetty-security-9.4.9.v20180320.jar
	    ├── jetty-server-9.4.9.v20180320.jar
	    ├── jetty-servlet-9.4.9.v20180320.jar
	    ├── jetty-util-9.4.9.v20180320.jar
	    ├── jetty-webapp-9.4.9.v20180320.jar
	    ├── jetty-xml-9.4.9.v20180320.jar
	    ├── taglibs-standard-impl-1.2.5.jar
	    ├── taglibs-standard-spec-1.2.5.jar
	    ├── websocket-api-9.4.9.v20180320.jar
	    ├── websocket-client-9.4.9.v20180320.jar
	    ├── websocket-common-9.4.9.v20180320.jar
	    ├── websocket-server-9.4.9.v20180320.jar
	    └── websocket-servlet-9.4.9.v20180320.jar
```
this assembler project contain start.sh and stop.sh scripts. to start the web and api mock all you need to do is to execute start.sh and to stop it execute stop.sh
 