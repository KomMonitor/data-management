# KomMonitor DataManagement REST API

This projects implements a REST API that persists and manages all relevant data within the KomMonitor stack.

**Table of Content**
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:0 orderedList:0 -->

- [KomMonitor DataManagement REST API](#kommonitor-datamanagement-rest-api)
	- [Quick Links And Further Information on KomMonitor](#quick-links-and-further-information-on-kommonitor)
	- [Overview](#overview)
  - [Dependencies to other KomMonitor Components](#dependencies-to-other-kommonitor-components)
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

## Quick Links And Further Information on KomMonitor
   - [DockerHub repositories of KomMonitor Stack](https://hub.docker.com/orgs/kommonitor/repositories)
   - [Github Repositories of KomMonitor Stack](https://github.com/KomMonitor)
   - [Github Wiki for KomMonitor Guidance and central Documentation](https://github.com/KomMonitor/KomMonitor-Docs/wiki)
   - [Technical Guidance](https://github.com/KomMonitor/KomMonitor-Docs/wiki/Technische-Dokumentation) and [Deployment Information](https://github.com/KomMonitor/KomMonitor-Docs/wiki/Setup-Guide) for complete KomMonitor stack on Github Wiki
   - [KomMonitor Website](https://kommonitor.de/)  

## Overview  
The <b>KomMonitor Data Management API</b> is a REST API encapsulating data management CRUD operations for various resources, such as spatial units, other georesources, indicators, topics, roles and custom indicator computation scripts. These resources are managed in a PostGIS database and serve as the basis for other components of the [KomMonitor](http://kommonitor.de) spatial data infrastructure.

For each resource dedicated REST operations are specified using [Swagger/OpenAPI v2](https://swagger.io). To inspect the REST API you may use swagger-ui interface to a running instance of the <b>KomMonitor Data Management REST API</b>, i.e. navigate to ```<pathToDeyployedInstance>/swagger-ui.html```, e.g. ```localhost:8085/swagger-ui.html```.

The service is implemented as a Java Spring Boot REST service. In addition [Maven](https://maven.apache.org/) is used as dependency and build management tool.

## Dependencies to other KomMonitor Components
KomMonitor Data Management requires 
   - a **PostGIS database**, where all KomMonitor-relevant data is managed. The database can be a docker container or an external database server reachable via URL.
   - an optional and configurable connection to a running **Keycloak** server, if role-based data access is activated via configuration of KomMonitor stack
   - an optional and configurable connection to a running **Geoserver** instance, if spatial data shall be published as interoperable geoservices WMS and WFS (currently not fully implemented and tested)

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
spring.datasource.url= jdbc:postgresql://localhost:5432/kommonitor
spring.datasource.username=postgres
spring.datasource.password=postgres
database.host=localhost
database.port=5432
database.name=kommonitor
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

- encrypted data transfer
```java
encryption.enabled=false
encryption.symmetric.aes.password=password
encryption.symmetric.aes.iv.length_byte=16
```

- in a Keycloak-active scenario, data retrieval is protected by a role-based access mechanism using software Keycloak. It can be enabled/disabled and onfigured as follows:
```java
# Do not use real values in production environment
kommonitor.swagger-ui.security.client-id=kommonitor-data-management-api
kommonitor.swagger-ui.security.secret=secret
#kommonitor.datamanagement-api.swagger-ui.base-path=/data-management-api

kommonitor.roles.admin=administrator

keycloak.enabled=false
keycloak.realm=kommonitor
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.resource=kommonitor-data-management-api
keycloak.bearer-only=true
keycloak.autodetect-bearer-only=false
keycloak.ssl-required=external
keycloak.public-client=false
keycloak.cors=true
keycloak.cors-allowed-methods=*
keycloak.credentials.secret=secret
```

After adjusting the configuration to your target environment, you may continue to build the service as described next.

#### JAR / WAR File
##### JAR
As a Spring Boot application the typical build format is a single runnable <b>JAR</b>, which is also set as project default. Simply run the command `mvn clean install` or `mvn clean package` to create the runnable JAR file within the <b>target</b> folder. To run the JAR locally, simply call `start java -Dfile.encoding=utf-8 -jar <jar-file-name>.jar` from the target directory of the project, which will serve the application at `localhost:<PORT>` (i.e. `localhost:8085` per default). In a browser call  ``localhost:<PORT>/swagger-ui.html`` to inspect the REST API.

##### WAR
However, you might need to create a WAR archive in some scenarios. To create a WAR archive, following the [Baeldung Guide](https://www.baeldung.com/spring-boot-war-tomcat-deploy), you must adjust the associated settings in `pom.xml` file:

**(there is a branch called [deployment/war](https://github.com/KomMonitor/data-management/tree/deployment/war) that has the follwing steps prepared for WAR deployment - so simply use that and adjust parameters to your local environment)**

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
4. if role-based data retrieval is activated using Keycloak, then more steps are required. The way, how the Keycloak setup is defined when deploying a Spring Boot runnable JAR greatly differs from a WAR deployment. In a WAR deployment, additional files must be specified/configured according to official [Keycloak adapter documentation](https://www.keycloak.org/docs/latest/securing_apps/#overview). The branch `deployment/war` contains a *Tomcat* example according to [Keycloak Doc for Tomcat runtime environment](https://www.keycloak.org/docs/latest/securing_apps/#_tomcat_adapter) 
   1. `src/main/webapp/WEB-INF/keycloak.json` contains Keycloak connection parameters (i.e. [inspect branch deployment/WAR](https://github.com/KomMonitor/data-management/blob/deployment/war/src/main/webapp/WEB-INF/keycloak.json))
   2. `src/main/webapp/WEB-INF/web.xml` contains specification of proteced resources (i.e. [inspect branch deployment/WAR](https://github.com/KomMonitor/data-management/blob/deployment/war/src/main/webapp/WEB-INF/web.xml))
   3. `src/main/webapp/META-INF/context.xml` should contain crucial information about the target runtime container (e.g. tomcat) 
   4. In addition `pom.xml` requires additional changes, as the Keycloak-libraries must be set to `<scope>provided</scope>` as the runtime server container includes the libraries 
   5. the deployment server (e.g. tomcat) must have addtional libraries added to its lib folder, i.e. for tomcat check [https://www.keycloak.org/docs/latest/securing_apps/#_tomcat_adapter](https://www.keycloak.org/docs/latest/securing_apps/#_tomcat_adapter) 


After these adjustments you can run the command `mvn clean install` or `mvn clean package`, which will create the corresponding <b>WAR</b> file in the folder <b>target</b>. Deploy it somewhere, e.g. in Tomcat, to launch the app. Assuming the WAR file is named `kommonitor-data-management-api-1.0.0.WAR` and Tomcat is started locally on port 8080, you may reach the service via `localhost:8080/kommonitor-data-management-api-1.0.0`. In a browser call  ``localhost:8080/kommonitor-data-management-api-1.0.0/swagger-ui.html`` to inspect the REST API.

### Docker
The <b>KomMonitor Data Management REST API</b> can also be build and deployed as Docker image. The project contains the associated `Dockerfile` and an exemplar `docker-compose.yml` on project root level. 

When building the docker image (i.e. `docker build -t kommonitor/data-management:latest .`), the profile `docker` is used. In contrast to the default profile, the `docker` profile consumes the associated `src/main/resources/application-docker.properties`, which makes use of environment variables to declare relevant settings (as described in the [Configuration](#configuration) section above). For instance check out the exemplar [docker-compose.yml](./docker-compose.yml) file. It specifies two services, `kommonitor-db` as required PostGIS database container and the actual `kommonitor-data-management` container. The latter depends on the database container and contains an `environment` section to define the required settings (connection details to other services etc.).

## Exemplar docker-compose File with explanatory comments

Only contains subset of whole KomMonitor stack to focus on the config parameters of this component

```yml

version: '2.1'

networks:
  kommonitor:
    driver: bridge
services:

    # database container; must use PostGIS database
    # database is not required to run in docker - will be configured in Data Management component
    kommonitor-db:
      image: mdillon/postgis
      container_name: kommonitor-db
      #restart: unless-stopped
      ports:
        - 5432:5432
      environment:
        - POSTGRES_USER=kommonitor      # database user (will be created on startup if not exists) - same settings in data management service
        - POSTGRES_PASSWORD=kommonitor  # database password (will be created on startup if not exists) - same settings in data management service 
        - POSTGRES_DB=kommonitor_data   # database name (will be created on startup if not exists) - same settings in data management service
      volumes:
        - postgres_data:/var/lib/postgresql/data   # persist database data on disk (crucial for compose down calls to let data survive)
      networks:
        - kommonitor

    # Data Management component encapsulating the database access and management as REST service
    kommonitor-data-management:
      image: kommonitor/data-management
      container_name: kommonitor-data-management
      #restart: unless-stopped
      depends_on:
        - kommonitor-db    # only if database runs as docker container as well
      ports:
        - "8085:8085"
      networks:
        - kommonitor
      links:
        - kommonitor-db
      environment:
       - SERVER_PORT=8085                                             # Server port; default is 8085
       - DATABASE_HOST=kommonitor-db      # host of database (i.e. docker name when db runs in docker in same network; else URL to database server)
       - DATABASE_USER=kommonitor         # database user (username with acess to database, should be owner of the database)
       - DATABASE_PASSWORD=kommonitor     # database user password
       - DATABASE_NAME=kommonitor_data    # database name
       - DATABASE_PORT=5432               # database port
       - logging.level.de.hsbo.kommonitor=ERROR    # adjust logging level [e.g. "INFO", "WARN", "ERROR"] - ERROR logs only errors 
       - ENABLE_OGC_PUBLISHMENT=false                               # enable/disable Geoserver-based publishment of spatial data as Web service - currently Geoserver-connection should be disabled
       - DB_SCHEMA_SPATIALUNITS=public                              # only relevant with GeoServer connection
       - DB_SCHEMA_GEORESOURCES=public                              # only relevant with GeoServer connection
       - DB_SCHEMA_INDICATORS=public                                # only relevant with GeoServer connection
       - GEOSERVER_HOST=https://kommonitor.fbg-hsbo.de/geoserver    # Geoserver host URL - only relevant with GeoServer connection
       - GEOSERVER_PORT=80                                          # Geoserver port - only relevant with GeoServer connection
       - GEOSERVER_USER=admin                                       # Geoserver username - only relevant with GeoServer connection
       - GEOSERVER_PASSWORD=password                              # Geoserver user password - only relevant with GeoServer connection
       - GEOSERVER_TARGET_WORKSPACE=kommonitor                      # Geoserver workspace name (will be created if not exists) - only relevant with GeoServer connection
       - GEOSERVER_DATASTORE_SPATIALUNITS=kommonitor_spatialunits   # Geoserver datastore name for spatial units (will be created if not exists) - only relevant with GeoServer connection
       - GEOSERVER_DATASTORE_GEORESOURCES=kommonitor_georesources   # Geoserver datastore name for georesources (will be created if not exists) - only relevant with GeoServer connection
       - GEOSERVER_DATASTORE_INDICATORS=kommonitor_indicators       # Geoserver datastore name for indicators (will be created if not exists) - only relevant with GeoServer connection
       - GEOSERVER_DEFAULT_EPSG=EPSG:4326                           # Geoserver default EPSG code (EPSG:4326 as KomMonitor uses this internally as well) - only relevant with GeoServer connection
       - encryption.enabled=false                      # enable/disable encrypted data transfer from Data Management service (requested data will be encrypted)
       - encryption.symmetric.aes.password=password    # shared secret for data encryption - must be set equally within all supporting components
       - encryption.symmetric.aes.iv.length_byte=16    # length of random initialization vector for encryption algorithm - must be set equally within all supporting components
       - KOMMONITOR_DATAMANAGEMENTAPI_SWAGGERUI_BASEPATH=      #depending on DNS Routing and Reverse Proxy setup a base path can be set here to access swagger-ui interface (e.g. set '/data-management' if https://kommonitor-url.de/data-management works as entry point for localhost:8085)   
       - keycloak.enabled=false                                               # enable/disable role-based data access using Keycloak (true requires working Keycloak Setup and enforces that all other components must be configured to enable Keycloak as well)
       - kommonitor.swagger-ui.security.client-id=kommonitor-data-management  # client/resource id of data management component in Keycloak realm
       - kommonitor.swagger-ui.security.secret=secret                         # WARNING: DO NOT SET IN PRODUCTION!!! Keycloak secret of this component within Credentials tab of respective Keycloak client; secret for swagger-ui to authorize swagger-ui requests in a Keycloak-active scenario (mostly this should not be set, as users with access to swagger-ui (e.g. 'http://localhost:8085/swagger-ui.html') could then authorize without own user account and perform CRUD requests)
       - keycloak.realm=kommonitor                                            # Keycloak realm name
       - keycloak.auth-server-url=https://keycloak.fbg-hsbo.de/auth           # Keycloak URL ending with '/auth/'
       - keycloak.resource=kommonitor-data-management                         # client/resource id of data management component in Keycloak realm
       - keycloak.bearer-only=true                                            # sets authentication workflow to use/accept Bearer token sent within Authorization header
       - keycloak.autodetect-bearer-only=false                                # normally do not change this value        
       - keycloak.sslquired=external                                          # Keycloak SSL setting; ["external", "none"]; default "external"
       - keycloak.public-client=false                                         # Keycloak setting that component is not public (its REST endpoints are partially Keycloak-protected and require Authentication)
       - keycloak.cors=true                                                   # enable CORS
       - keycloak.cors-allowed-methods=*                                      # enable all HTTP operations for CORS
       - keycloak.credentials.secret=secret                                   # Keycloak secret of this component within Credentials tab of respective Keycloak client; must be set here  
       - SERVER_MAXHTTPHEADERSIZE=48000                                       
       - server.max-http-header-size=48000
       - kommonitor.roles.admin=administrator                                 # name of the Keycloak role that is used as "administrator" role within KomMonitor granting rights to inspect all data and perform all actions. This name of this role is configurable, but must be set to the equal value within management and importer component as well as within Keycloak 



# ... other KomMonitor components omitted here



volumes:
 postgres_data:


```

## User Guide

### Registration Order of Resources

When integration data and resources into KomMonitor you the follwing order of requests is recommended:

1. Register/Manage relevant **roles** via REST `POST` and `PUT` operations on endpoint `/roles` (only relevant in Keycloak scenario)
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
| ARISEN FROM (an *ID* reference to a former spatial feature that over time evolved into the respective feature - i.e. if a spatial district A evolves into two new smaller districts *B* and *C*, then *B* and *C* might carry the information that the have arisen from *A*) | **arisenFrom** | no |

When integrating data into KomMonitor those property settings must be respected. All other components of the **KomMonitor Spatial Data Infrastructure** make use of those spatial feature properties and, thus rely on the existence of those properties with those exact property names.

#### Indicators
For **indicators**, only the time series information can be submitted within *POST* and *PUT* requests against REST endpoint `/indicators` (in request parameter `indicatorValues`), not the underlying spatial features. However, the **unique identifier** of the associated spatial feature must be submitted (in request property `indicatorValues.spatialReferenceKey`) in order to allow the spatial join of indicator timeseries to spatial features within the KomMonitor database. Hereby the unique identifier must match the identifier of the associated spatial feature that was registered before. The name of the associated spatial unit dataset is submitted in request parameter `applicableSpatialUnit`. Any timestamp of a submitted time series entry must follow the pattern **YYYY-MM-DD**.

### Add Indicators with and without Timeseries data

Depending on the `creationType` of an indicator, the actual time series data is either submitted via POST and PUT requests or it can be computed by the KomMonitor **Processing Engine**. During registration of an indicator via `POST` operation on REST endpoint `/indicators`, the request parameter `creationType` distinguishes between `"creationType": "INSERTION"` or `"creationType": "COMPUTATION"`. If `"creationType": "INSERTION"` is chosen, then the request parameter `indicatorValues` must contain an array of time series values according to the following schema:

```json
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

## Credits and Contributing Organizations
- Department of Geodesy, Bochum University of Applied Sciences
- Department for Cadastre and Geoinformation, Essen
- Department for Geodata Management, Surveying, Cadastre and Housing Promotion, Mülheim an der Ruhr
- Department of Geography, Ruhr University of Bochum
- 52°North GmbH, Münster
- Kreis Recklinghausen
