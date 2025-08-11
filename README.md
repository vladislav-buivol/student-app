# Microservices Demo (Service-R, Service-S)

This repository contains a small microservices setup with two Spring Boot services:
- Service-R (default port 8080)
- Service-S (default port 8081)

External dependencies used by the services:
- RabbitMQ (with management UI)
- PostgreSQL

You can run everything with Docker Compose, or run each service individually with `docker run`.

## Prerequisites
- Docker and Docker Compose installed
- (Optional) Maven and JDK 21 if you want to run locally without Docker

## 1) Configuration via .env
Docker Compose is configured to read a `.env` file from the project root. Create a new file named `.env` next to `docker-compose.yml` with the following content (you can adjust values as needed):

```
# RabbitMQ
RABBITMQ_DEFAULT_USER=guest
RABBITMQ_DEFAULT_PASS=guest
RABBITMQ_AMQP_PORT=5672
RABBITMQ_MANAGEMENT_PORT=15672

# Postgres
POSTGRES_DB=students
POSTGRES_USER=students
POSTGRES_PASSWORD=students
PG_PORT=5432

# Services
SERVICE_R_PORT=8080
SERVICE_S_PORT=8081
```

Notes:
- RabbitMQ management UI will be available on http://localhost:15672 (user/pass as above).
- Postgres will be available on localhost:5432.
- You can change ports and credentials if needed.

## 2) Run everything with Docker Compose
From the project root:

Build images and start all services (in the background):

- Linux/macOS:
```
docker compose up -d --build
```

- Windows PowerShell:
```
docker compose up -d --build
```

Check logs:
```
docker compose logs -f
```

Stop and remove containers, network, but keep volumes:
```
docker compose down
```

This starts the following containers on a bridge network `microservices_ms_bridge_network`:
- rabbitmq (ports 5672, 15672)
- postgres (port 5432)
- service-r (port 8080)
- service-s (port 8081)

### Test Service-R quickly
```
curl http://localhost:8080/students
```
You should receive:
```
Received
```

## 3) Run each service separately with docker run
You can also run containers manually without Compose. The steps below create the same topology.

### 3.1 Create a shared network
Create a bridge network so containers can resolve each other by name:
```
docker network create microservices_ms_bridge_network
```

Note: If you’re mixing with `docker compose`, it will create a network named `<folder>_microservices_ms_bridge_network` by default. If you want to join that network instead, replace `microservices_ms_bridge_network` accordingly.

### 3.2 Start infrastructure
RabbitMQ:

- Linux/macOS (line continuations with backslashes):
```
docker run \
  -d \
  --name rabbitmq \
  --network microservices_ms_bridge_network \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management
```

- Windows PowerShell (use backtick for line continuation or run as a single line):
```
docker run `
  -d `
  --name rabbitmq `
  --network microservices_ms_bridge_network `
  -p 5672:5672 `
  -p 15672:15672 `
  -e RABBITMQ_DEFAULT_USER=guest `
  -e RABBITMQ_DEFAULT_PASS=guest `
  rabbitmq:3-management
```

PostgreSQL:

- Linux/macOS:
```
docker run \
  -d \
  --name postgres \
  --network microservices_ms_bridge_network \
  -p 5432:5432 \
  -e POSTGRES_DB=students \
  -e POSTGRES_USER=students \
  -e POSTGRES_PASSWORD=students \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:15
```

- Windows PowerShell:
```
docker run `
  -d `
  --name postgres `
  --network microservices_ms_bridge_network `
  -p 5432:5432 `
  -e POSTGRES_DB=students `
  -e POSTGRES_USER=students `
  -e POSTGRES_PASSWORD=students `
  -v postgres_data:/var/lib/postgresql/data `
  postgres:15
```

### 3.3 Build service images
From the project root:
```
docker build -t microservices-service-r ./Service-R
docker build -t microservices-service-s ./Service-S
```

### 3.4 Run Service-R only
Service-R needs RabbitMQ and exposes port 8080 by default (overridable via SERVER_PORT). Example (matching the style from the issue description):

- Linux/macOS:
```
docker run \
  --name service-r-test \
  --network microservices_ms_bridge_network \
  -p 8083:8083 \
  -e SERVER_PORT=8083 \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  microservices-service-r
```

- Windows PowerShell:
```
docker run `
  --name service-r-test `
  --network microservices_ms_bridge_network `
  -p 8083:8083 `
  -e SERVER_PORT=8083 `
  -e SPRING_RABBITMQ_HOST=rabbitmq `
  microservices-service-r
```

You can then test:
```
curl http://localhost:8083/students
```

### 3.5 Run Service-S only
Service-S needs both RabbitMQ and PostgreSQL. Default port is 8081, but we’ll expose it on 8082 here for variety.

- Linux/macOS:
```
docker run \
  --name service-s-test \
  --network microservices_ms_bridge_network \
  -p 8082:8082 \
  -e SERVER_PORT=8082 \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/students \
  -e SPRING_DATASOURCE_USERNAME=students \
  -e SPRING_DATASOURCE_PASSWORD=students \
  microservices-service-s
```

- Windows PowerShell:
```
docker run `
  --name service-s-test `
  --network microservices_ms_bridge_network `
  -p 8082:8082 `
  -e SERVER_PORT=8082 `
  -e SPRING_RABBITMQ_HOST=rabbitmq `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/students `
  -e SPRING_DATASOURCE_USERNAME=students `
  -e SPRING_DATASOURCE_PASSWORD=students `
  microservices-service-s
```

## 4) Useful commands
- List containers: `docker ps`
- Show logs: `docker logs -f <container>`
- Stop and remove a container: `docker rm -f <container>`
- Remove the network: `docker network rm microservices_ms_bridge_network`
- Remove volumes used above (if you created them): `docker volume rm postgres_data`

## 5) Notes
- Service-R Dockerfile exposes 8080; Service-S exposes 8081. You can override with `SERVER_PORT` at runtime as shown above.
- In Docker Compose, service names `rabbitmq` and `postgres` act as DNS hostnames, which is why `SPRING_RABBITMQ_HOST=rabbitmq` and `jdbc:postgresql://postgres:5432/...` work.
- If you change ports in `.env`, adjust the `-p` flags and datasource URL in manual `docker run` examples accordingly.


## 6) Service-S SOAP API: StudentSoapEndpoint

This project exposes a SOAP endpoint from Service-S that returns student data. The endpoint is implemented in:
- Service-S/src/main/java/app/students/Service_S/endpoint/StudentSoapEndpoint.java
- It uses the XML schema at Service-S/src/main/resources/students.xsd

Key runtime URLs (default ports from application.properties):
- WSDL: http://localhost:8081/ws/students.wsdl
- SOAP endpoint address (soap:address location): http://localhost:8081/ws
- Target namespace (xmlns:stu): http://example.com/students

How the flow works
- Spring-WS Dispatcher: SoapConfiguration registers MessageDispatcherServlet at `/ws/*` and enables `transformWsdlLocations`.
- WSDL exposure: `DefaultWsdl11Definition` bean named "students" serves the WSDL at `/ws/students.wsdl` using `students.xsd` and targetNamespace `http://example.com/students`.
- Marshalling: JAXB classes are generated from `students.xsd` into the package `com.example.students` (e.g., `GetStudentRequest`, `GetStudentResponse`, `Student`).
- Endpoint mapping: `StudentSoapEndpoint` is annotated with `@Endpoint`. The method handling requests is mapped by `@PayloadRoot(namespace = "http://example.com/students", localPart = "getStudentRequest")`.
- Request handling: Incoming SOAP messages with Body root `<stu:getStudentRequest>` are unmarshalled into `GetStudentRequest` and routed to `getStudent(...)`. The method returns `GetStudentResponse` which Spring-WS marshals back to SOAP.

Endpoint mapping details
- Code snippet:
```java
@Endpoint
public class StudentSoapEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/students";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentRequest")
    @ResponsePayload
    public GetStudentResponse getStudent(@RequestPayload GetStudentRequest request) { /* ... */ }
}
```
- Mapping rule: `@PayloadRoot` localPart must match the name of the root element in the XSD (`getStudentRequest`). The namespace must match the `targetNamespace` in the XSD.
- Service URL: The servlet mapping `/ws/*` means you POST SOAP messages to `http://localhost:8081/ws` (not to the `.wsdl` URL).

XSD overview (students.xsd)
- targetNamespace: `http://example.com/students` (prefix `tns`)
- Defined elements:
  - `getStudentRequest`: contains one required string field `recordBook`
  - `getStudentResponse`: contains `firstName`, `lastName`, `faculty`, `recordBook` (all strings)
  - `getAllStudentsRequest`: empty request (`xs:anyType`)
  - `getAllStudentsResponse`: contains an unbounded list of `tns:student`
- Complex types:
  - `tns:student`: `firstName`, `lastName`, `faculty`, `recordBook`
- Generated JAXB classes (via jaxb2-maven-plugin):
  - `com.example.students.GetStudentRequest`
  - `com.example.students.GetStudentResponse`
  - `com.example.students.Student`
  - `com.example.students.ObjectFactory`

Try it: Example SOAP request/response
1) Send a request to the service URL (`http://localhost:8081/ws`). Content-Type must be `text/xml` or `application/soap+xml`.

- Request body:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stu="http://example.com/students">
  <soapenv:Header/>
  <soapenv:Body>
    <stu:getStudentRequest>
      <stu:recordBook>12345</stu:recordBook>
    </stu:getStudentRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

Requested concise example
- POST URL: http://localhost:8081/ws
- Header: Content-Type: text/xml
- Body:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:stu="http://example.com/students">
  <soapenv:Header/>
  <soapenv:Body>
    <stu:getStudentRequest>
      <stu:recordBook>RB-001</stu:recordBook>
    </stu:getStudentRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

Note: The WSDL is available at http://localhost:8081/ws/students.wsdl. Post SOAP requests to the service URL (/ws), not to the .wsdl URL.

- Expected response body (example):

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stu="http://example.com/students">
  <soapenv:Header/>
  <soapenv:Body>
    <stu:getStudentResponse>
      <stu:firstName>Иван</stu:firstName>
      <stu:lastName>Иванов</stu:lastName>
      <stu:faculty>ФКТИ</stu:faculty>
      <stu:recordBook>12345</stu:recordBook>
    </stu:getStudentResponse>
  </soapenv:Body>
</soapenv:Envelope>
```

Quick invocation examples
- PowerShell (Windows):

```powershell
$body = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stu="http://example.com/students">
  <soapenv:Header/>
  <soapenv:Body>
    <stu:getStudentRequest>
      <stu:recordBook>12345</stu:recordBook>
    </stu:getStudentRequest>
  </soapenv:Body>
</soapenv:Envelope>
"@
Invoke-WebRequest -Uri "http://localhost:8081/ws" -Method Post -ContentType "text/xml; charset=UTF-8" -Body $body
```

- curl (any OS):

```sh
curl -X POST "http://localhost:8081/ws" \
     -H "Content-Type: text/xml; charset=UTF-8" \
     --data-binary @- <<'XML'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:stu="http://example.com/students">
  <soapenv:Header/>
  <soapenv:Body>
    <stu:getStudentRequest>
      <stu:recordBook>12345</stu:recordBook>
    </stu:getStudentRequest>
  </soapenv:Body>
</soapenv:Envelope>
XML
```

Notes and troubleshooting
- SOAPAction header: Not required for this endpoint (Spring-WS routing is based on the payload root element and namespace).
- WSDL address in different environments: `transformWsdlLocations(true)` ensures the `soap:address` in the WSDL reflects the host/port you used to fetch the WSDL.
- If you get 404: Ensure you POST to `/ws` and not to `/ws/students.wsdl`.
- If marshalling errors occur: Verify the XML uses the correct namespace (`http://example.com/students`) and element names exactly as defined in the XSD.

Internal usage in this project
- Service-S includes a `StudentRequestListener` that demonstrates programmatic SOAP invocation using Spring's `WebServiceTemplate`. It constructs `GetStudentRequest` and sends it to `http://localhost:8081/ws`, receiving `GetStudentResponse`. This pattern can be reused by other services.


## 7) Publish students-soap-client to Nexus (artifact repository)

This project includes a small client module (`students-soap-client`) that contains the JAXB classes generated from the Service-S XSD. To publish this client JAR to a Nexus repository so it can be reused by other services, follow the steps below.

1) Copy generated XSD classes into students-soap-client
- Generate the JAXB classes in Service-S (they are typically generated during build):
  - After building Service-S, look under:
    - Service-S/target/generated-sources/jaxb/com/example/students
- Copy the Java classes from that folder into the client module at:
  - students-soap-client/src/main/java/com/example/students
- If those files already exist in the client module, overwrite or update them accordingly.

2) Build the client JAR
- From the project root (or inside the module folder), run:
```
# From project root
mvn -f students-soap-client/pom.xml clean package
```
- This should produce:
  - students-soap-client/target/students-soap-client-1.0.0.jar

3) Configure Maven settings for Nexus credentials
- Edit (or create) your Maven settings.xml, usually located at:
  - Windows: %USERPROFILE%\.m2\settings.xml
  - Linux/macOS: ~/.m2/settings.xml
- Add the following server configuration (adjust username/password to your Nexus):
```
<settings>
  <servers>
    <server>
      <id>nexus</id>
      <username>admin</username>
      <password>password</password>
    </server>
  </servers>
</settings>
```

4) Deploy the JAR to Nexus using Maven deploy-file
- Open a terminal and navigate to the client module folder:
```
cd students-soap-client
```
- Execute the deploy command (Windows PowerShell example provided in the issue):
```
mvn deploy:deploy-file -DgroupId="com.example" -DartifactId=students-soap-client -Dversion="1.0.0" -Dpackaging=jar -Dfile="C:\projects\Microservices\students-soap-client\target\students-soap-client-1.0.0.jar" -DrepositoryId=nexus -Durl="http://172.28.17.185:8081/repository/microservices-soap-service-r/"
```

Notes
- Ensure the -Dfile path points to the JAR you just built.
- repositoryId must match the <id> in your settings.xml server entry ("nexus" in the example).
- Update -Dversion and the JAR filename if you use a different version in your pom.xml.
- Replace the -Durl value with your Nexus repository URL if it differs from the example.
- If your Nexus requires HTTPS or a different repository (hosted vs. snapshot), adjust accordingly.
