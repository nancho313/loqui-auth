package com.nancho313.loqui.auth.test.command;

import com.nancho313.loqui.auth.model.command.EmptyCommandResponse;
import com.nancho313.loqui.auth.model.command.signup.command.SignUpCommand;
import com.nancho313.loqui.auth.model.command.signup.handler.SignUpCommandHandler;
import com.nancho313.loqui.auth.model.exception.InvalidCommandDataException;
import com.nancho313.loqui.auth.persistence.service.IdGenerator;
import com.nancho313.loqui.auth.persistence.service.UserAuthenticator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SignUpCommandHandlerTest {
  
  private SignUpCommandHandler sut;
  
  private UserAuthenticator userAuthenticatorMock;
  
  @BeforeEach
  void setup() {
    
    var validator = Validation.buildDefaultValidatorFactory().getValidator();
    userAuthenticatorMock = mock(UserAuthenticator.class);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    sut = new SignUpCommandHandler(validator, userAuthenticatorMock, idGenerator);
  }
  
  @Test
  void signUpOk() {
    
    // Arrange
    var username = "foo";
    var password = "123";
    var email = "foo@hotmail.com";
    var command = new SignUpCommand(username, password, email);
    
    // Act
    var response = sut.handle(command);
    
    // Assert
    assertThat(response).isNotNull().isInstanceOf(EmptyCommandResponse.class);
    verify(userAuthenticatorMock).storeBasicCredentials(anyString(), eq(username), eq(password), eq(email));
  }

  @Test
  void signUpThrowsExceptionDueUsernameIsAlreadyUsed() {

    // Arrange
    var username = "foo";
    var password = "123";
    var email = "foo@hotmail.com";
    var command = new SignUpCommand(username, password, email);
    when(userAuthenticatorMock.existsByUsername("foo")).thenReturn(Boolean.TRUE);

    // Act & Assert
    var exception = assertThrows(IllegalArgumentException.class, () -> sut.handle(command));
    assertThat(exception.getMessage()).isEqualTo("The username foo is already used.");
  }

  @Test
  void signUpThrowsExceptionDueEmailIsAlreadyUsed() {

    // Arrange
    var username = "foo";
    var password = "123";
    var email = "foo@hotmail.com";
    var command = new SignUpCommand(username, password, email);
    when(userAuthenticatorMock.existsByEmail("foo@hotmail.com")).thenReturn(Boolean.TRUE);

    // Act & Assert
    var exception = assertThrows(IllegalArgumentException.class, () -> sut.handle(command));
    assertThat(exception.getMessage()).isEqualTo("The email foo@hotmail.com is already used.");
  }
  
  @MethodSource("getInvalidData")
  @ParameterizedTest
  void signUpWithInvalidDataThrowsException(String username, String password, String email, String expectedErrorMessage) {
    
    // Arrange
    var command = new SignUpCommand(username, password, email);
    
    // Act & Assert
    var exception = assertThrows(InvalidCommandDataException.class, () -> sut.handle(command));
    assertThat(exception.getMessage()).contains(expectedErrorMessage);
    
  }
  
  public static Stream<Arguments> getInvalidData() {
    
    var username = "foo";
    var password = "123";
    var email = "foo@hotmail.com";
    
    var expectedErrorMessage1 = "The username cannot be empty.";
    var expectedErrorMessage2 = "The password cannot be empty.";
    var expectedErrorMessage3 = "The email cannot be empty.";
    
    return Stream.of(
            Arguments.of(null, password, email, expectedErrorMessage1),
            Arguments.of("", password, email, expectedErrorMessage1),
            Arguments.of("  ", password, email, expectedErrorMessage1),
            Arguments.of(username, null, email, expectedErrorMessage2),
            Arguments.of(username, "", email, expectedErrorMessage2),
            Arguments.of(username, "  ", email, expectedErrorMessage2),
            Arguments.of(username, password, null, expectedErrorMessage3),
            Arguments.of(username, password, "", expectedErrorMessage3),
            Arguments.of(username, password, "  ", expectedErrorMessage3)
    );
  }
}
