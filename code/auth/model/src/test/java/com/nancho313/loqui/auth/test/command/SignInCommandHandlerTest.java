package com.nancho313.loqui.auth.test.command;

import com.nancho313.loqui.auth.model.command.signin.command.SignInCommand;
import com.nancho313.loqui.auth.model.command.signin.handler.SignInCommandHandler;
import com.nancho313.loqui.auth.model.exception.InvalidCommandDataException;
import com.nancho313.loqui.auth.model.exception.InvalidResponseDataException;
import com.nancho313.loqui.auth.test.persistence.util.LoginResolverSpy;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignInCommandHandlerTest {
  
  private LoginResolverSpy loginResolverSpy;
  
  private SignInCommandHandler sut;
  
  @BeforeEach
  void setup() {
    
    loginResolverSpy = new LoginResolverSpy();
    var validator = Validation.buildDefaultValidatorFactory().getValidator();
    sut = new SignInCommandHandler(validator, loginResolverSpy);
  }
  
  @Test
  void signInOk() {
    
    // Arrange
    var username = "foo";
    var password = "123456";
    loginResolverSpy.addCredential(username, password);
    var command = new SignInCommand(username, password);
    
    // Act
    var result = sut.handle(command);
    
    // Assert
    assertThat(result.jwt()).isNotNull().isNotBlank();
  }
  
  @Test
  void signInThrowBadCredentials() {
    
    // Arrange
    var username = "foo";
    var password = "123456";
    var command = new SignInCommand(username, password);
    
    // Act & Assert
    assertThrows(BadCredentialsException.class, () -> sut.handle(command));
  }
  
  @NullSource
  @ValueSource(strings = {"",  "  ",})
  @ParameterizedTest
  void signInWasOkButJwtWasEmpty(String jwtToReturn) {
    
    // Arrange
    var username = "foo";
    var password = "123456";
    loginResolverSpy.addCredential(username, password);
    loginResolverSpy.setJwtToReturn(jwtToReturn);
    var command = new SignInCommand(username, password);
    
    // Act
    assertThrows(InvalidResponseDataException.class, () -> sut.handle(command));
  }
  
  @MethodSource("getInvalidData")
  @ParameterizedTest
  void signInWithInvalidDataThrowsException(String username, String password, String expectedErrorMessage) {
    
    // Arrange
    var command = new SignInCommand(username, password);
    
    // Act & Assert
    var exception = assertThrows(InvalidCommandDataException.class, () -> sut.handle(command));
    assertThat(exception.getMessage()).contains(expectedErrorMessage);
    
  }
  
  public static Stream<Arguments> getInvalidData() {
    
    var username = "foo";
    var password = "123456";
    
    var expectedErrorMessage1 = "The username cannot be empty.";
    var expectedErrorMessage2 = "The password cannot be empty.";
    
    return Stream.of(
            Arguments.of(null, password, expectedErrorMessage1),
            Arguments.of("", password, expectedErrorMessage1),
            Arguments.of("  ", password, expectedErrorMessage1),
            Arguments.of(username, null, expectedErrorMessage2),
            Arguments.of(username, "", expectedErrorMessage2),
            Arguments.of(username, "  ", expectedErrorMessage2)
    );
  }
}
