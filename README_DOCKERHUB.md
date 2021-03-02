# KomMonitor DataManagement REST API

This projects implements a REST API that persists and manages all relevant data within the KomMonitor stack.

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
       - GEOSERVER_PASSWORD=sK4nc$bDSm                              # Geoserver user password - only relevant with GeoServer connection
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
