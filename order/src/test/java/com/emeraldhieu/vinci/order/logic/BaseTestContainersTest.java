package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.logic.config.KafkaTestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Import({
    KafkaTestConfiguration.class,
})
public class BaseTestContainersTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.3-alpine"))
            .withReuse(true);

    private static Network network = Network.newNetwork();

    // Cluster ID is created by "kafka-storage random-uuid"
    private static String clusterId = "qYoMEZXcS_SKP2PzAl8-WA";

    @Container
    @ServiceConnection
    private static KafkaContainer kafka =
        new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
            .withNetwork(network)
            .withKraft()
            .withClusterId(clusterId)
            .withReuse(true);

    @Container
    private static GenericContainer schemaRegistry =
        new GenericContainer(DockerImageName.parse("confluentinc/cp-schema-registry:7.4.0"))
            .withNetwork(network)
            .withExposedPorts(8081) // Exposed port is used to get a mapped port. Otherwise, the error "Container doesn't expose any ports" occurs.
            .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry") // To be resolved by Docker
            .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081") // Seems optional
            .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                "PLAINTEXT://" + kafka.getNetworkAliases().get(0) + ":9092") // A list of Kafka brokers to connect to
            .dependsOn(kafka)
            .withReuse(true);

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.properties.schema.registry.url",
            () -> "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getFirstMappedPort());
    }
}
