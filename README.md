# KomMonitor DataManagement REST API

This projects implements a REST API that persists and manages all relevant data within the KomMonitor project.

**Table of Content**
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:0 orderedList:0 -->

- [KomMonitor DataManagement REST API](#kommonitor-datamanagement-rest-api)
	- [Background Information Concerning KomMonitor Spatial Data Infrastructure](#background-information-concerning-kommonitor-spatial-data-infrastructure)
		- [Architecture Overview](#architecture-overview)
	- [Overview](#overview)
	- [Installation / Building Information](#installation-building-information)
		- [Configuration](#configuration)
			- [application.properties - Configure Database Access and Deployment Details of other Services](#applicationproperties-configure-database-access-and-deployment-details-of-other-services)
			- [JAR / WAR File](#jar-war-file)
				- [JAR](#jar)
				- [WAR](#war)
		- [Docker](#docker)
	- [User Guide](#user-guide)
	- [Contribution - Developer Information](#contribution-developer-information)
		- [How to Contribute](#how-to-contribute)
		- [Branching](#branching)
		- [License and Third Party Lib POM Plugins](#license-and-third-party-lib-pom-plugins)
	- [Contact](#contact)
	- [Credits and Contributing Organizations](#credits-and-contributing-organizations)

<!-- /TOC -->

## Background Information Concerning KomMonitor Spatial Data Infrastructure
This software is part of a spatial data infrastructure called [KomMonitor](http://kommonitor.de), which is a shortcut for German "Kommunales Monitoring" (Municipal monitoring). Funded by the <i>German Federal Ministry of Education and Research</i> municipal actors from the cities Essen and Mülheim cooperate with scientists from the Ruhr University Bochum and the Bochum University of Applied Sciences in order to create a monitoring infrastructure to support planning processes within local governments. I.e., by monitoring certain planning aspects from various topics like demography, social, environment, habitation and other, whose spatio-temporal variation and development can be measured in the form of indicators, [KomMonitor](http://kommonitor.de) may act as a <i>Spatial Decision Support System</i>. Amongst others, the following goals and key aspects are focused:
1. cross-sectional data from several topics
2. variable spatial layers (i.e. administrative layers of a city)
3. transparency with regard to indicators and how they are computed
4. cross-sectional interactive analysis and exploration
5. a complete spatial data infrastructure consisting of data management, geodata processing and indicator computation as well as data display and exploration in webclient

The project is funded from Feb 2017 - Feb 2020. The resulting software components, are published as Open-Source software to continue maintenance and development beyond the funding period.

### Architecture Overview
![Conceptual KomMonitor Architecture and Idea](./misc/KomMonitor-Architecture.png "Conceptual KomMonitor Architecture and Idea")

As a spatial decision support system, the main idea behind KomMonitor is to take (geo-)data from local authorities, import them to the <i>Data Management</i> component, process them (i.e. compute indicators based on georesurces and maybe other indicators via the <i>Processing Engine</i>; or compute waypath routing and reachability isochrones via <i>Reachability Service</i>) and finally publish, display and analyze them within a <i>Web-Client</i>.     

## Overview  
The <b>KomMonitor Data Management API</b> is a REST API encapsulating data management CRUD operations for various resources, such as spatial units, other georesources, indicators, topics, users and user groups, roles and custom indicator computation scripts. These resources are managed in a PostGIS database and serve as the basis for other components of the [KomMonitor](http://kommonitor.de) spatial data infrastructure.

For each resource dedicated REST operations are specified using [Swagger/OpenAPI v2](https://swagger.io). The corresponding ```swagger.json``` containing the REST API specification is located at ```src/main/resources/swagger.json```. To inspect the REST API you may use the online [Swagger Editor](https://editor.swagger.io/) or, having access to a running instance of the <b>KomMonitor Data Management REST API</b> simply navigate to ```<pathToDeyployedInstance>/swagger-ui.html```, e.g. ```localhost:8085/swagger-ui.html```.

The service is implemented as a Java Spring Boot REST service. In addition [Maven](https://maven.apache.org/) is used as dependency and build management tool.

## Installation / Building Information
Being a Maven project, installation and building of the service is as simple as calling ```mvn clean install``` or ```mvn clean package```. Even Docker images can be acquired with ease, as described below. However, depending on your environment configuration aspects have to be adjusted first.

### Configuration
Configuration distinguishes between two main categories.
1. Configuring database access and deployment details of other components
2. Configuring target build format (single executable <b>JAR</b> file or classic <b>WAR</b> container).

#### application.properties - Configure Database Access and Deployment Details of other Services
The central configuration file is located at ```src/main/resources/application.properties```. Several important aspects must match your target environment when deploying the service. These are:

- name and port:
```java
server.port=8085
spring.application.name=kommonitor-data-access-api
```
- database connection (to PostGIS database):
```java
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL9Dialect
spring.datasource.driverClassName=org.postgresql.Driver
# adjust following parameters to your target environment
spring.datasource.url= jdbc:postgresql://localhost:5432/kommonitor_midterm
spring.datasource.username=postgres
spring.datasource.password=postgres
database.host=localhost
database.port=5432
database.name=kommonitor_midterm
```
- GeoServer connection (optional - it can be disabled):
```java
# properties for geoserver connection
# true enables geoserver publishment; false will disable it
ogc.enable_service_publication = false;
# connection details to your running GeoServer incl. credentials
rest.url=http://localhost:8080/geoserver
rest.user=admin
rest.password=geoserver
# target workspace and datastore names for respective features
workspace=kommonitor
datastore_spatialunits=kommonitor_spatialunits
datastore_georesources=kommonitor_georesources
datastore_indicators=kommonitor_indicators
defaultEPSG=EPSG:4326
```

After adjusting the configuration to your target environment, you may continue to build the service as described next.

#### JAR / WAR File
##### JAR
As a Spring Boot application the typical build format is a single runnable <b>JAR</b>, which is also set as project default. Simply run the command `mvn clean install` or `mvn clean package` to create the runnable JAR file within the <b>target</b> folder. To run the JAR locally, simply call `start java -Dfile.encoding=utf-8 -jar kommonitor-data-management-api-1.0.0.jar` from the target directory of the project, which will serve the application at `localhost:<PORT>` (i.e. `localhost:8085` per default). In a browser call  ``localhost:<PORT>/swagger-ui.html`` to inspect the REST API.

##### WAR
However, you might need to create a WAR archive in some scenarios. To create a WAR archive, following the [Baeldung Guide](https://www.baeldung.com/spring-boot-war-tomcat-deploy), you must adjust the associated settings in `pom.xml` file:

1. Look for the `<packaging>JAR</packaging>` setting and change it to `<packaging>WAR</packaging>`
2. Look for the follwing commented out dependency
```java
<!-- <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency> -->
```
and enable it by removing the comment characters
```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```
3. The entry point of the service located at `src/main/java/de/hsbo/kommonitor/datamanagement/Runner.java` already implements the required <i>SpringBootServletInitializer</i> interface. <u>Hence you must not change anything here</u>.
```java
package de.hsbo.kommonitor.datamanagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan("de.hsbo.kommonitor")
@EntityScan( basePackages = {"de.hsbo.kommonitor"} )
public class Runner extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }
}
```

After these adjustments you can run the command `mvn clean install` or `mvn clean package`, which will create the corresponding <b>WAR</b> file in the folder <b>target</b>. Deploy it somewhere, e.g. in Tomcat, to launch the app. Assuming the WAR file is named `kommonitor-data-management-api-1.0.0.WAR` and Tomcat is started locally on port 8080, you may reach the service via `localhost:8080/kommonitor-data-management-api-1.0.0`. In a browser call  ``localhost:8080/kommonitor-data-management-api-1.0.0/swagger-ui.html`` to inspect the REST API.

### Docker
The <b>KomMonitor Data Management REST API</b> can also be build and deployed as Docker image. The project contains the associated `Dockerfile` and an exemplar `docker-compose.yml` on project root level. The `Dockerfile` already expects a pre-built <b>JAR</b> file of the service. So, before building the docker image, you must build the runnable <b>JAR</b> via maven.

When building the docker image (i.e. `docker build -t data-management-api:latest .`), the profile `docker` is used. In contrast to the default profile, the `docker` profile consumes the associated `src/main/resources/application-docker.properties`, which makes use of environment variables to declare relevant settings (as described in the [Configuration](#configuration) section above). For instance check out the exemplar [docker-compose.yml](./docker-compose.yml) file. It specifies two services, `kommonitor-db` as required PostGIS database container and the actual `kommonitor-data-management-api` container. The latter depends on the database container and contains an `environment` section to define the required settings (connection details to other services etc.).

A more advanced setup including a Geoserver as docker container is also given at [docker-compose_managementAndGeoserver.yml](./docker-compose_managementAndGeoserver.yml).

## User Guide
TODO

### Registration Order of Resources

When integration data and resources into KomMonitor you the follwing order of requests is recommended:

1. Register/Manage relevant **roles** via REST `POST` and `PUT` operations on endpoint `/roles`
2. Register/Manage relevant **users** via REST `POST` and `PUT` operations on endpoint `/user`
3. Register/Manage relevant **topics** via REST `POST` and `PUT` operations on endpoint `/topics`
4. Register/Manage relevant **georesources** via REST `POST`, `PATCH` and `PUT` operations on endpoint `/georesources` - georesources might be used to compute indicators or are simply used for display in web client
5. Register/Manage relevant **spatial units** via REST `POST`, `PATCH` and `PUT` operations on endpoint `/spatial-units` - this step must be performed before registration of indicators, as a spatial join of indicator time series data to associated spatial unit features must be performed within the database.
6. Register/Manage relevant **indicators** via REST `POST`, `PATCH` and `PUT` operations on endpoint `/indicators` and take care of associated topics, spatial units, georesources, etc.
7. Register/Manage relevant **process-scripts** via REST `POST` and `PUT` operations on endpoint `/process-scripts` - required for all indicators that shall be computed by KomMonitor **Processing Engine**.

### Data Schema Information for Spatial Resources
#### Spatial Resources (Spatial Units, Georesources)
Concerning spatial resources (i.e. **spatial units**, **georesources**) KomMonitor expects a predefined attribution for certain property information. Within *POST* and *PUT* requests to the resource endpoints `/spatial-units` and `/georesources`, GeoJSON FeatuerCollection strings are submitted containing the relevant features for the respective **spatial unit** or **georesource**. As GeoJSON features certain property information about each feature is delivered within the respective `feature.properties` object, such as name, id an other properties. KomMonitor requires highly important information to be encoded in specific properties with dedicated property names. To be precise, each spatial feature of the aforementioned resource types should have the following information:

|    Feature Information   |   Property Name required by KomMonitor     |    Mandatory?    |
| :-------------: |:-------------:| :-----:|
| unique IDENTIFIER of the spatial feature | **ID** | yes |
| unique NAME of the spatial feature (required for display in web client) | **NAME** | yes |
| PERIOD OF VALIDITY (a time period to define a lifespan for the respective element. Within this lifespan the feature is marked as valid. E.g. this is used to query features for a dedicated timestamp, only returning the valid features) | **validStartDate** and **validEndDate**, each following the pattern *YYYY-MM-DD* | if not provided as GeoJSON properties for each individual feature, a generic period of validity can be specified within parameter **periodOfValidity**.**startDate** and **periodOfValidity**.**endDate**, again following the pattern *YYYY-MM-DD*|
| ARISON FROM (an *ID* reference to a former spatial feature that over time evolved into the respective feature - i.e. if a spatial district A evolves into two new smaller districts *B* and *C*, then *B* and *C* might carry the information that the have arisen from *A*) | **arisonFrom** | no |

When integrating data into KomMonitor those property settings must be respected. All other components of the **KomMonitor Spatial Data Infrastructure** make use of those spatial feature properties and, thus rely on the existence of those properties with those exact property names.

#### Indicators
For **indicators**, only the time series information can be submitted within *POST* and *PUT* requests against REST endpoint `/indicators` (in request parameter `indicatorValues`), not the underlying spatial features. However, the **unique identifier** of the associated spatial feature must be submitted (in request property `indicatorValues.spatialReferenceKey`) in order to allow the spatial join of indicator timeseries to spatial features within the KomMonitor database. Hereby the unique identifier must match the identifier of the associated spatial feature that was registered before. The name of the associated spatial unit dataset is submitted in request parameter `applicableSpatialUnit`. Any timestamp of a submitted time series entry must follow the pattern **YYYY-MM-DD**.

### Add Indicators with and without Timeseries data

Depending on the `creationType` of an indicator, the actual time series data is either submitted via POST and PUT requests or it can be computed by the KomMonitor **Processing Engine**. During registration of an indicator via `POST` operation on REST endpoint `/indicators`, the request parameter `creationType` distinguishes between `"creationType": "INSERTION"` or `"creationType": "COMPUTATION"`. If `"creationType": "INSERTION"` is chosen, then the request parameter `indicatorValues` must contain an array of time series values according to the following schema:

```
"indicatorValues": [
    {
      "spatialReferenceKey": "spatialReferenceKey",
      "valueMapping": [
        {
          "indicatorValue": 0.8008282,
          "timestamp": "2000-01-23"
        },
        {
          "indicatorValue": 0.8008282,
          "timestamp": "2000-01-23"
        }
      ]
    },
    {
      "spatialReferenceKey": "spatialReferenceKey",
      "valueMapping": [
        {
          "indicatorValue": 0.8008282,
          "timestamp": "2000-01-23"
        },
        {
          "indicatorValue": 0.8008282,
          "timestamp": "2000-01-23"
        }
      ]
    }
  ]
```

If instead `"creationType": "COMPUTATION"` is chosen, then the request parameter `indicatorValues` can remain empty, as the indicator values are computed later with the help of the KomMonitor **Processing Engine** and an associated **process script** that must be registered within KomMonitor via the REST endpoint `/process-scripts`.

## Contribution - Developer Information
This section contains information for developers.

### How to Contribute
The technical lead of the whole [KomMonitor](http://kommonitor.de) spatial data infrastructure currently lies at the Bochum University of Applied Sciences, Department of Geodesy. We invite you to participate in the project and in the software development process. If you are interested, please contact any of the persons listed in the [Contact section](#contact):

### Branching
The `master` branch contains latest stable releases. The `develop` branch is the main development branch that will be merged into the `master` branch from time to time. Any other branch focuses certain bug fixes or feature requests.

### License and Third Party Lib POM Plugins
According to [52°North Best Practices for Java License Management](https://wiki.52north.org/Documentation/BestPracticeLicenseManagementInSoftwareProjects) the POM contains following plugins

 - license-maven-plugin by mycila (used to check and generate license headers for source code files)
    - ```mvn license:check```
    - ```mvn license:format```
 - maven-license-plugin by codehaus (used to generate an overview of Third Party dependencies as THIRD-PARTY.txt)
    - ```mvn clean generate-resources``` (creates file under target/generated-resources/license/THIRD-PARTY.txt)
 - maven-notice-plugin (used to create and maintain a NOTICE file containing a short list of third party dependencies)   
    - ```mvn notice:check``` (checks whether NOTICE file exists and is up-to-date)
    - ```mvn notice:generate``` (generates NOTICE file based on POM dependencies)

Note that project build may fail if any of the configured prerequisits according to these plugins is not fulfilled.

## Contact
|    Name   |   Organization    |    Mail    |
| :-------------: |:-------------:| :-----:|
| Christian Danowski-Buhren | Bochum University of Applied Sciences | christian.danowski-buhren@hs-bochum.de |
| Andreas Wytzisk  | Bochum University of Applied Sciences | Andreas-Wytzisk@hs-bochum.de |
| Ulrike Klein | Bochum University of Applied Sciences | christian.danowski-buhren@hs-bochum.de | Ulrike.Klein@hs-bochum.de |

## Credits and Contributing Organizations
- Department of Geodesy, Bochum University of Applied Sciences
- Department for Cadastre and Geoinformation, Essen
- Department for Geodata Management, Surveying, Cadastre and Housing Promotion, Mülheim an der Ruhr
- Department of Geography, Ruhr University of Bochum
