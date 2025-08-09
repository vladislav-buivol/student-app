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

This starts the following containers on a bridge network `ms_bridge_network`:
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
docker network create ms_bridge_network
```

Note: If you’re mixing with `docker compose`, it will create a network named `<folder>_ms_bridge_network` by default. If you want to join that network instead, replace `ms_bridge_network` accordingly.

### 3.2 Start infrastructure
RabbitMQ:

- Linux/macOS (line continuations with backslashes):
```
docker run \
  -d \
  --name rabbitmq \
  --network ms_bridge_network \
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
  --network ms_bridge_network `
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
  --network ms_bridge_network \
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
  --network ms_bridge_network `
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
  --network ms_bridge_network \
  -p 8083:8083 \
  -e SERVER_PORT=8083 \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  microservices-service-r
```

- Windows PowerShell:
```
docker run `
  --name service-r-test `
  --network ms_bridge_network `
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
  --network ms_bridge_network \
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
  --network ms_bridge_network `
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
- Remove the network: `docker network rm ms_bridge_network`
- Remove volumes used above (if you created them): `docker volume rm postgres_data`

## 5) Notes
- Service-R Dockerfile exposes 8080; Service-S exposes 8081. You can override with `SERVER_PORT` at runtime as shown above.
- In Docker Compose, service names `rabbitmq` and `postgres` act as DNS hostnames, which is why `SPRING_RABBITMQ_HOST=rabbitmq` and `jdbc:postgresql://postgres:5432/...` work.
- If you change ports in `.env`, adjust the `-p` flags and datasource URL in manual `docker run` examples accordingly.
