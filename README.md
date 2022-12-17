# Vinci

Vinci is a microservice shopping webapp. The name is inspired by my favourite artist - Leonardo da Vinci. 🧑🏻‍🎨

## API-first approach

Vinci chooses API First approach using [Open API 3.0](https://swagger.io/specification/) and [Open API Maven Generator](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin) to boost API development and allow foreseeing how the product looks like. There are also [some other advatanges](https://swagger.io/resources/articles/adopting-an-api-first-approach/).

## Message queue

Whenever an order is placed, a corresponding payment record is asynchronouly created for the user to purchase later on. Behind the scene, order service produces Kafka messages and payment service consumes them.

## Schema registry

As Order's Kafka messages tend to evolve by development's needs, [Confluent Avro](https://docs.confluent.io/2.0.0/schema-registry/docs/intro.html) is used to version schemas of Kafka messages. Schemas are stored in Kafka's log files and indices are stored in Avro's in-memory storage.

## Database schema change management

[Liquibase](https://docs.liquibase.com/home.html) supports revisioning, deploying and rolling back database changes. Generally, it makes it easier to work with database.

## Order API

### 1) List orders

```
GET /orders
```

#### Request parameters (optional)

| Parameters    | Description  | Format                                |
|---------------|--------------|-------------------------------------- |
| `sortOrders`  | Sort orders  | `column1,direction|column2,direction` |

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
        "id": 3,
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
        "id": 2,
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
        "id": 1,
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

| Parameters           | Type     | Description          |
| -------------------- | -------- | -------------------- |
| `products`           | List     | List of products     |

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
    "id": 4,
    "products":
    [
        "coke",
        "juice",
        "cider"
    ],
    "createdBy": 1,
    "createdAt": "2022-12-17T15:22:29.540621",
    "updatedBy": 1,
    "updatedAt": "2022-12-17T15:22:29.540621"
}
```
