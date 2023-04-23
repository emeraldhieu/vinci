# Vinci

Vinci is a microservice shopping webapp. The name is inspired by my favourite artist - Leonardo da Vinci. 🧑🏻‍🎨

## API-first approach

Vinci chooses [API First approach](https://swagger.io/resources/articles/adopting-an-api-first-approach/) using [Open API 3.0](https://swagger.io/specification/) and [Open API Maven Generator](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin) to boost API development and allow foreseeing how the product looks like. The generated code can be overriden via [Mustache templates](https://mustache.github.io/mustache.5.html) such as [data transfer object](https://github.com/emeraldhieu/vinci/blob/master/order/src/main/resources/templates/pojo.mustache).

## Message queue

Whenever an order is placed, a corresponding payment record is asynchronouly created for the user to purchase later on. Behind the scene, order service produces Kafka messages and payment service consumes them.

## Schema registry

As Order's Kafka messages tend to evolve by development's needs, [Confluent Avro](https://docs.confluent.io/2.0.0/schema-registry/docs/intro.html) is used to version schemas of Kafka messages. Schemas are stored in Kafka's log files and indices are stored in Avro's in-memory storage. For example, OrderMessage's schema:
```
{
    "type": "record",
    "name": "OrderMessage",
    "namespace": "com.emeraldhieu.vinci.payment",
    "fields":
    [
        {
            "name": "orderId",
            "type": "string"
        }
    ]
}
```

## Database schema change management

[Liquibase](https://docs.liquibase.com/tools-integrations/springboot/springboot.html) supports revisioning, deploying and rolling back database changes. On top of that, it allows [initializing data from CSV](https://docs.liquibase.com/change-types/load-data.html) for demonstrative purpose.

[Flyway](https://flywaydb.org/documentation/usage/plugins/springboot) is similar. It's used to define database structure, bootstrap initial data via SQL statements, and manage database migrations.

## gRPC

[gRPC is said to be faster and more secured than traditional REST API](https://stackoverflow.com/questions/44877606/is-grpchttp-2-faster-than-rest-with-http-2#44937371) because it transmits binary data instead of JSON.

Module `grpc-interface` contains [protobuf3](https://protobuf.dev/programming-guides/proto3/) files to define services, request, and response messages then use [protobuf-maven-plugin](https://github.com/xolstice/protobuf-maven-plugin) to generate Java service stubs. The gRPC server `shipping` implements those stubs to follow the contracts defined in protobuf files.

You can test the gRPC using [grpcurl](https://github.com/fullstorydev/grpcurl).
```shell
grpcurl --plaintext -d '{"id": "d707ada36e6644ddaec63a52e7a40d56"}' localhost:50013 com.emeraldhieu.vinci.shipping.grpc.ShippingService/GetShipping
```

## GraphQL

[GraphQL](https://graphql.org/) is a query language that allows users to retrieve only necessary data in their own way in a single request. [GraphQL for Spring](https://docs.spring.io/spring-graphql/docs/current/reference/html) uses annotations to map handler methods to queries and fields in a GraphQL schema.

Vinci incorporates gRPC into GraphQL. For example, GraphQL `ShippingDetailController#shipping` calls gRPC server to retrieve a `Shipping` in gRPC manner.

This is a simple GraphQL to test.
```shell
curl --location 'http://localhost:50003/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"{\r\n    shippingDetails(offset: 0, limit: 10, sortOrders: []) {\r\n        id\r\n        amount\r\n        shipping {\r\n            id\r\n            status\r\n        }\r\n    }\r\n}","variables":{}}'
```

Response
```json
{
    "data": {
        "shippingDetails": [
            {
                "id": "ae61181973fd4896a99ecb4089005197",
                "amount": 2.0,
                "shipping": {
                    "id": "d707ada36e6644ddaec63a52e7a40d56",
                    "status": "IN_PROGRESS"
                }
            }
        ]
    }
}
```

## Problem Details RFC-7807

Vinci uses [Spring 6's Problem Details](https://docs.spring.io/spring-framework/docs/6.0.0-RC1/reference/html/web.html#mvc-ann-rest-exceptions) to keep error responses consistent across microservices.

```
{
    "type": "http://localhost:50001/vinci/types",
    "title": "Unprocessable Entity",
    "status": 422,
    "detail": "Invalid sort order",
    "instance": "/orders"
}
```

## Java beans mappings

Like Lombok, [Mapstruct](https://github.com/mapstruct/mapstruct) is a code generator library that supports mapping between entities and DTOs without writing boilerplate code. A significant benefit is that mappers don't need unit tests because there's no code to test!

## Quickstart

#### Spin up the stack

At project directory, run this command to set up external services.

```sh
docker compose up -d
```

In IntelliJ, start `OrderApp`, `PaymentApp`, and `ShippingApp`.

#### Verify an API

```shell
curl --location 'http://localhost:50001/orders?sortOrders=updatedAt%2Cdesc%7CcreatedBy%2Casc'
```

If it returns 200 with a JSON response, the app "order" is working. Check [Order API](#order-api) for other endpoints.

## Deploy microservices to Kubernetes

### Use K3d

[K3d](https://k3d.io/) is a lightweight Kubernetes distribution. This guide assumes you've had k3d [installed](https://k3d.io/v5.4.4/#installation).

#### 1) Setup k3d cluster

Install a local (registry)[https://hub.docker.com/_/registry]
```shell
k3d registry create registry42 --port 5050
```

At project directory, run this
```shell
k3d cluster create cluster42 -p "9900:80@loadbalancer" --registry-use k3d-registry42:5050 --registry-config registries.yaml -v <yourLocalPathTo>/vinci/postgres-scripts:/home/docker/postgres-scripts
```

What it does

+ Create a k8s cluster
+ Use an existing docker registry
+ Map host machine's port 9900 to [k3d's loadbalancer](https://k3d.io/v5.3.0/design/defaults/#k3d-loadbalancer)'s port 80
+ Mount the host machine's directory to the k8s cluster's directory

#### 2) Dockerize apps

2.1) At directory `order`, build the app
```shell
mvn clean package
```

2.2) Create docker image `order`
```shell
docker build -t localhost:5050/order:1.0-SNAPSHOT .
```

2.3) Push image to the registry
```
docker push localhost:5050/order:1.0-SNAPSHOT
```

Do all the steps again for `payment` and `shipping`.

#### 3) Apply configuration

Open another terminal, create k8s resources
```shell
kubectl apply -f deployment.yaml
```

#### 4) Verify Postgres

Listen on port 5432, forward data to a pod selected by the service "postgres"
```shell
kubectl port-forward svc/postgres 5432
```

Connect to postgres from the host machine. Enter password "postgres".
```shell
psql -h 127.0.0.1 -d postgres -U postgres -W
```

List databases. If you see databases `order`, `payment`, and `shipping`, the setup is working.
```shell
postgres=# \l
```

#### 5) Verify service "order"

Since K3d Load balancer has routed requests to app services by K8s ingress rules, you won't need port-forwarding. Mind that the context paths `/order`, `/payment`, and `/shipping` are mandatory.

Create an order on your host machine
```shell
curl --location 'http://localhost:8080/order/orders' \
--header 'Content-Type: application/json' \
--data '{
    "products": [
        "coke",
        "juice",
        "cider"
    ]
}'
```

If it returns a JSON response with an ID, it's working.

#### 6) Verify Schema Registry

Listen on port 8081, forward data to a pod selected by the service "schema-registry"
```shell
kubectl port-forward svc/schema-registry 8081
```

Ask for the very first schema of "order"
```shell
curl http://localhost:8081/schemas/ids/1
```

Response
```json
{
    "schema": "{\"type\":\"record\",\"name\":\"OrderMessage\",\"namespace\":\"com.emeraldhieu.vinci.order\",\"fields\":[{\"name\":\"orderId\",\"type\":\"string\"}]}"
}
```

## Order API

### 1) List orders

```
GET /orders
```

#### Request parameters (optional)

| Parameters    | Description  | Format                                     |
|---------------|--------------|--------------------------------------------|
| `sortOrders`  | Sort orders  | column1,direction&#124;column2,direction   |

Some examples of `sortOrders`:
+ `createdAt,desc`
+ `updatedAt,desc|createdBy,asc`

#### Example

##### 1) List orders

```sh
curl --location --request GET 'http://localhost:50001/orders?sortOrders=updatedAt,desc|createdBy,asc'
```

##### Response

```json
[
  {
    "id": "0a5eb04756f54776ac7752d3c8fae45b",
    "products":
    [
      "car",
      "bike",
      "house"
    ],
    "createdBy": 5,
    "createdAt": "2022-11-27T00:00:00",
    "updatedBy": 6,
    "updatedAt": "2022-11-28T00:00:00"
  }
]
```

### 2) Create an order

```
POST /orders
```

#### Request body

Required parameters

| Parameters | Type | Description      |
|------------|------|------------------|
| `products` | List | List of products |

#### Example

##### Create an order

```sh
curl --location --request POST 'http://localhost:50001/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "products": [
        "coke",
        "juice",
        "cider"
    ]
}'
```

##### Response

```json
{
  "id": "9383630cc2844b08a568fe50fb0c0e90",
  "products":
  [
    "coke",
    "juice",
    "cider"
  ],
  "createdBy": 1,
  "createdAt": "2022-12-20T17:17:12.918707",
  "updatedBy": 1,
  "updatedAt": "2022-12-20T17:17:12.918707"
}
```

### 3) Get an order

```
GET /orders/<id>
```

#### Path parameters

| Parameters | Description | Type       |
| ---------- |-------------| ---------- |
| `id`       | Order ID    | String     |

#### Example

##### Get an order

```sh
curl --location 'http://localhost:50001/orders/0a5eb04756f54776ac7752d3c8fae45b'
```

##### Response

```json
{
  "id": "0a5eb04756f54776ac7752d3c8fae45b",
  "products": [
    "car",
    "bike",
    "house"
  ],
  "createdBy": 5,
  "createdAt": "2022-11-27T00:00:00",
  "updatedBy": 6,
  "updatedAt": "2022-11-28T00:00:00"
}
```

### 4) Delete an order

```
DELETE /orders/<id>
```

#### Path parameters

| Parameters | Description | Type       |
| ---------- |-------------| ---------- |
| `id`       | Order ID    | String     |

#### Example

##### Delete an order

```sh
curl --location 'http://localhost:50001/orders/0a5eb04756f54776ac7752d3c8fae45b'
```

Response status is 204 with no content

## TODOs

+ ~~Deploy to K8s~~
+ ~~Scale app services by ingress and loadbalancer~~
+ Implement OAuth2
+ Improve database modeling
+ Improve field validation
+ Update README.md for other endpoints
