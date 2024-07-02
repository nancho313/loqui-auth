package com.nancho313.loqui.auth.integrationtest.api.controller;

import com.nancho313.loqui.auth.integrationtest.BaseIntegrationTest;
import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.service.IdGenerator;
import com.nancho313.loqui.auth.persistence.service.UserAuthenticator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class SignInControllerIT extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuthUserMongodbDAO authUserMongodbDAO;

  @Autowired
  private UserAuthenticator userAuthenticator;

  @Autowired
  private IdGenerator idGenerator;

  @AfterEach
  void teardown() {

    authUserMongodbDAO.deleteAll();
  }

  @Test
  void signInOk() throws Exception {

    // Arrange
    userAuthenticator.storeBasicCredentials(idGenerator.generateId(), "foo", "123456", "foo@foo.com");
    var uri = URI.create("/signin");
    var message = """
            {
              "username": "foo",
              "password": 123456
            }""";

    // Act & Assert
    mockMvc.perform(post(uri).content(message).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jwt").isNotEmpty());
  }

  @Test
  void signInFails() throws Exception {

    // Arrange
    var uri = URI.create("/signin");
    var message = """
            {
              "username": "foo",
              "password": 123456
            }""";

    // Act & Assert
    mockMvc.perform(post(uri).content(message).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("The given credentials are not valid."))
            .andExpect(jsonPath("$.date").isNotEmpty());
  }
}