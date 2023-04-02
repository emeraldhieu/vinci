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

Module `grpc-interface` contains [protobuf3](https://protobuf.dev/programming-guides/proto3/)
files to define services, request, and response messages then use [protobuf-maven-plugin](https://github.com/xolstice/protobuf-maven-plugin)
to generate Java service stubs.

gRPC server `shipping` implements those stubs to follow the contracts defined in protobuf files.

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
  },
  {
    "id": "850829b025704efe995d29661a3a1220",
    "products":
    [
      "apple",
      "orange",
      "avocado"
    ],
    "createdBy": 3,
    "createdAt": "2022-11-25T00:00:00",
    "updatedBy": 4,
    "updatedAt": "2022-11-26T00:00:00"
  },
  {
    "id": "c611d780541541f69c1e1e80b966527a",
    "products":
    [
      "pizza",
      "burger",
      "pasta"
    ],
    "createdBy": 1,
    "createdAt": "2022-11-23T00:00:00",
    "updatedBy": 2,
    "updatedAt": "2022-11-24T00:00:00"
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

## Quickstart

At project directory, run this command to set up external services.

```sh
docker compose up -d
```

In IntelliJ, start `OrderApp`, `PaymentApp`, and `ShippingApp`.

---

## Deploy microservices to Kubernetes

[Minikube](https://minikube.sigs.k8s.io/) is used for development only.

Assuming you've had minikube [installed and started](https://minikube.sigs.k8s.io/docs/start/).

### Follow these steps

Since minikube actually creates a VM and run containers on it, you have to mount the host machine's directory to the VM's directory

```
minikube mount <yourLocalPathTo>/vinci/postgres-scripts:/home/docker/postgres-scripts
```

Open another terminal, create k8s resources
```
kubectl apply -f deployment.yaml
```

Listen on port 5432, forward data to a pod selected by the service
```
kubectl port-forward svc/postgres 5432
```

Connect to postgres from the host machine. Enter password "postgres".
```
psql -h 127.0.0.1 -d postgres -U postgres -W
```

List databases. If you see databases `order`, `payment`, and `shipping`, the setup is working.
```
postgres=# \l
```

## Troubleshooting

### 1) postgres-scripts: file exists

After applying `deployment.yml`, check if Postgres is running correctly
```
kubectl describe po <postgresPodId>
```

If you see this error
```
Error: failed to start container "postgres": Error response from daemon: error while creating mount source path '/home/docker/postgres-scripts': mkdir /home/docker/postgres-scripts: file exists
```

the steps to fix are:

+ Stop the terminal mounting the `postgres-scripts`
+ Delete the configuration `kubectl delete -f deployment.yaml`
+ Apply the configuration `kubectl apply -f deployment.yaml`

## TODOs

+ Implement OAuth2
+ Improve database modeling
+ Improve field validation
+ Update README.md for other endpoints
