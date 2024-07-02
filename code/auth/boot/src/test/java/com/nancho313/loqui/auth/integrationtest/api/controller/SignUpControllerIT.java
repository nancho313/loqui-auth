package com.nancho313.loqui.auth.integrationtest.api.controller;

import com.nancho313.loqui.auth.integrationtest.BaseIntegrationTest;
import com.nancho313.loqui.auth.integrationtest.util.KafkaMessageCaptor;
import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.service.IdGenerator;
import com.nancho313.loqui.auth.persistence.service.UserAuthenticator;
import com.nancho313.loqui.events.CreatedUserEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@AutoConfigureMockMvc(addFilters = false)
class SignUpControllerIT extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuthUserMongodbDAO authUserMongodbDAO;

  @Autowired
  private UserAuthenticator userAuthenticator;

  @Autowired
  private IdGenerator idGenerator;

  @Autowired
  private KafkaMessageCaptor<CreatedUserEvent> messageCaptor;

  @AfterEach
  void teardown() {

    authUserMongodbDAO.deleteAll();
    messageCaptor.cleanMessages();
  }

  @Test
  void signUpUserOk() throws Exception {

    // Arrange
    var uri = URI.create("/signup");
    var message = """
            {
              "username": "foo",
              "email": "foo@foo.com",
              "password": "1234567"
            }""";

    // Act & Assert
    mockMvc.perform(post(uri).content(message).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());

    var opStoredUser = authUserMongodbDAO.findByUsername("foo");
    assertThat(opStoredUser).isPresent();
    var storedUser = opStoredUser.get();
    assertThat(storedUser.email()).isEqualTo("foo@foo.com");
    assertThat(storedUser.username()).isEqualTo("foo");
    assertThat(storedUser.password()).isNotEqualTo("1234567").isNotBlank();
    assertThat(storedUser.creationDate()).isCloseTo(LocalDateTime.now(), within(300, ChronoUnit.MILLIS));

    await().atMost(5, TimeUnit.SECONDS).until(() -> !messageCaptor.getCapturedMessages().isEmpty());

    var capturedMessages = messageCaptor.getCapturedMessages();
    assertThat(capturedMessages).isNotNull().hasSize(1);
    var capturedMessage = capturedMessages.getFirst().getPayload();
    assertThat(capturedMessage.getUserId()).hasToString(storedUser.id());
    assertThat(capturedMessage.getEmail()).hasToString(storedUser.email());
    assertThat(capturedMessage.getUsername()).hasToString(storedUser.username());
  }

  @Test
  void signUpUserFailsDueUsernameIsAlreadyUsed() throws Exception {

    // Arrange
    var uri = URI.create("/signup");
    var message = """
            {
              "username": "foo",
              "email": "foo1@foo.com",
              "password": "1234567"
            }""";
    userAuthenticator.storeBasicCredentials(idGenerator.generateId(), "foo", "123654", "foo2@foo.com");

    // Act & Assert
    mockMvc.perform(post(uri).content(message).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("The username foo is already used."));
  }

  @Test
  void signUpUserFailsDueEmailIsAlreadyUsed() throws Exception {

    // Arrange
    var uri = URI.create("/signup");
    var message = """
            {
              "username": "foo",
              "email": "foo@foo.com",
              "password": "1234567"
            }""";
    userAuthenticator.storeBasicCredentials(idGenerator.generateId(), "other_guy", "123654", "foo@foo.com");

    // Act & Assert
    mockMvc.perform(post(uri).content(message).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("The email foo@foo.com is already used."));
  }
}
