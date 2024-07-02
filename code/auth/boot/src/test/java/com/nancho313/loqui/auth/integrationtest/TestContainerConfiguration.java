package com.nancho313.loqui.auth.integrationtest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@TestConfiguration
public class TestContainerConfiguration {

  @Container
  private static final MongoDBContainer mongoDBContainer;

  @Container
  private static final KafkaContainer kafkaContainer;

  static {

    mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).withExposedPorts(27017);

    mongoDBContainer.setPortBindings(List.of("27017:27017"));

    kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withExposedPorts(9092, 9093).withEmbeddedZookeeper()
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    kafkaContainer.setPortBindings(List.of("9092:9092", "9093:9093"));
  }

  public static class KafkaServerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
      kafkaContainer.start();
      TestPropertyValues.of("spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers()).applyTo(applicationContext.getEnvironment());

    }
  }
}
