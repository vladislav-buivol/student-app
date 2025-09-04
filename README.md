## Quick Start

1. Create a hosted Maven repository in nexus, build and deploy the SOAP client

    ```sh
    mvn -f students-soap-client-dependency/pom.xml clean package
    mvn deploy:deploy-file -DgroupId=com.example -DartifactId=students-soap-client \
    -Dversion=1.0.0 -Dpackaging=jar \
    -Dfile=target/students-soap-client-1.0.0.jar \
    -DrepositoryId=nexus -Durl=http://localhost:8081/repository/microservices-soap-service-r/
    ```

   or deploy locally

    ```sh
    mvn install:install-file -DgroupId="com.example" -DartifactId=students-soap-client -Dversion="1.0.0" -Dpackaging=jar -Dfile="${PATH_TO_DEPENDENCY}\students-soap-client-dependency\target\students-soap-client-1.0.0.jar"
    ```
2. Run docker-compose.yml
    ```
    docker compose up -d
    ```
## Available requests
    http://localhost:8083/students/all
    http://localhost:8083/students/find/${recordBook}
