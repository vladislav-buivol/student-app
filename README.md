# Quick Start 
### 1. Configure your environment

Before starting the services, update the `.env` file with your environment-specific values.

- Replace the example host IPs, ports, and credentials with the ones that correspond to your setup.
- Make sure the `NEXUS_PASSWORD` matches the password in your Nexus Docker volume.

### 2. Start services
Run ``` docker compose up ``` to start services

## Pre-requirement

### Set up nexus
1. Start nexus:

```
 docker compose up nexus
```  

2. Get volume location:

```
docker inspect nexus
```

```
"Mounts": [
        {
            "Type": "volume",
            "Name": "microservices_nexus-data",
            "Source": "/var/lib/docker/volumes/microservices_nexus-data/_data",
            "Destination": "/nexus-data",
            "Driver": "local",
            "Mode": "rw",
            "RW": true,
            "Propagation": ""
        }
    ]
``` 

3. Get password and set it in .env file for NEXUS_PASSWORD
```
 sudo cat /var/lib/docker/volumes/microservices_nexus-data/_data/admin.password
```
4. Create a hosted Maven repository ``` microservices-soap-service-r ``` in Nexus (type: maven2 hosted), or skip Nexus and install locally.
5. Set setting.xml in .m2 folder
```xml
<settings>
    <servers>
        <server>
            <id>nexus</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>

    <mirrors>
        <mirror>
            <id>nexus</id>
            <mirrorOf>*,!central</mirrorOf>
            <url>http://${NEXUS_HOST}/repository/microservices-soap-service-r/</url>
        </mirror>
    </mirrors>
</settings>

```

### Deploy the JAR to Nexus using Maven deploy-file
1. Create jar. Run in $projectRoot ```mvn -f students-soap-client-dependency/pom.xml clean package```
2. Open a terminal and navigate to the ```students-soap-client-dependency``` module folder:
3. Execute the deploy command (Windows PowerShell example provided in the issue):
```
    cd students-soap-client-dependency
```
```
    mvn deploy:deploy-file -DgroupId="com.example" -DartifactId=students-soap-client -Dversion="1.0.0" -Dpackaging=jar -Dfile=".\target\students-soap-client-1.0.0.jar" -DrepositoryId=nexus -Durl="http://%NEXUS_HOST%/repository/<your-hosted-repo>/"
```
or deploy locally
``` 
  mvn install:install-file -DgroupId="com.example" -DartifactId=students-soap-client -Dversion="1.0.0" -Dpackaging=jar -Dfile=".\target\students-soap-client-1.0.0.jar""
```

## Available requests
    http://${SERVICE_R_HOST}:${SERVICE_R_PORT}/students/all
    http://${SERVICE_R_HOST}:${SERVICE_R_PORT}/students/find/${recordBook}
