package com.nancho313.loqui.auth.test.controller;

import com.nancho313.loqui.auth.api.controller.SignUpController;
import com.nancho313.loqui.auth.api.dto.SignUpApiRequest;
import com.nancho313.loqui.auth.model.command.EmptyCommandResponse;
import com.nancho313.loqui.auth.model.command.signup.command.SignUpCommand;
import com.nancho313.loqui.auth.test.util.CommandHandlerTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpControllerTest {

  private SignUpController sut;

  private CommandHandlerTestUtil<SignUpCommand, EmptyCommandResponse> commandHandler;

  @BeforeEach
  void setup() {

    commandHandler = new CommandHandlerTestUtil<>();
    sut = new SignUpController(commandHandler);
  }

  @Test
  void signUpUserOk() {

    // Arrange
    var request = new SignUpApiRequest("foo", "123456", "foo@foo.com");
    var response = new EmptyCommandResponse();
    commandHandler.initResponse(response);

    // Act
    var result = sut.signUpUser(request);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
  }
}
