package com.nancho313.loqui.auth.test.persistence.service;

import com.nancho313.loqui.auth.persistence.client.kafka.emitter.CreatedUserKafkaEmitter;
import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.client.mongodb.document.AuthUserDocument;
import com.nancho313.loqui.auth.persistence.service.UserAuthenticatorImpl;
import com.nancho313.loqui.events.CreatedUserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class UserAuthenticatorTest {

  private CreatedUserKafkaEmitter emitterMock;

  private AuthUserMongodbDAO authUserMongodbDAOMock;

  private UserAuthenticatorImpl sut;

  @BeforeEach
  void setup() {

    emitterMock = mock(CreatedUserKafkaEmitter.class);
    authUserMongodbDAOMock = mock(AuthUserMongodbDAO.class);
    var loquiPasswordEncoder = new BCryptPasswordEncoder();
    sut = new UserAuthenticatorImpl(emitterMock, authUserMongodbDAOMock, loquiPasswordEncoder);
  }

  @Test
  void storeBasicCredentialsOk() {

    // Arrange
    var id = UUID.randomUUID().toString();
    var username = "foo";
    var password = "123456";
    var email = "foo@foo.com";

    // Act
    sut.storeBasicCredentials(id, username, password, email);

    // Assert
    var argCaptorUser = ArgumentCaptor.forClass(AuthUserDocument.class);
    verify(authUserMongodbDAOMock).save(argCaptorUser.capture());

    var capturedDocument = argCaptorUser.getValue();
    assertThat(capturedDocument).isNotNull();
    assertThat(capturedDocument.id()).isEqualTo(id);
    assertThat(capturedDocument.username()).isEqualTo(username);
    assertThat(capturedDocument.password()).isNotBlank();
    assertThat(capturedDocument.creationDate()).isCloseTo(LocalDateTime.now(), within(500, ChronoUnit.MILLIS));

    var argCaptorCreatedUserEvent = ArgumentCaptor.forClass(CreatedUserEvent.class);
    verify(emitterMock).sendMessage(argCaptorCreatedUserEvent.capture(), anyList());
    var capturedEvent = argCaptorCreatedUserEvent.getValue();
    assertThat(capturedEvent.getUsername()).hasToString(username);
    assertThat(capturedEvent.getEmail()).hasToString(email);
    assertThat(capturedEvent.getUserId()).hasToString(id);
  }

  @ValueSource(booleans = {true, false})
  @ParameterizedTest
  void existsByUsernameOk(boolean exists) {

    // Arrange
    var username = "foo";
    when(authUserMongodbDAOMock.existsByUsername(username)).thenReturn(exists);

    // Act
    var result = sut.existsByUsername(username);

    // Assert
    assertThat(result).isEqualTo(exists);
  }

  @ValueSource(booleans = {true, false})
  @ParameterizedTest
  void existsByEmailOk(boolean exists) {

    // Arrange
    var email = "foo@foo.com";
    when(authUserMongodbDAOMock.existsByEmail(email)).thenReturn(exists);

    // Act
    var result = sut.existsByEmail(email);

    // Assert
    assertThat(result).isEqualTo(exists);
  }
}
