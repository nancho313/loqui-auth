package com.nancho313.loqui.auth.integrationtest;

import com.nancho313.loqui.auth.AuthApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ImportTestcontainers(TestContainerConfiguration.class)
@ContextConfiguration(
        initializers = {TestContainerConfiguration.KafkaServerInitializer.class},
        classes = {AuthApplication.class, ITConfiguration.class})
public abstract class BaseIntegrationTest {
}
