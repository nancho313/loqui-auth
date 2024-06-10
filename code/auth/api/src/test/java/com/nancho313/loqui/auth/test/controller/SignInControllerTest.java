package com.nancho313.loqui.auth.test.controller;

import com.nancho313.loqui.auth.api.controller.SignInController;
import com.nancho313.loqui.auth.api.dto.SignInRequest;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommand;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommandResponse;
import com.nancho313.loqui.auth.test.util.CommandHandlerTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;

class SignInControllerTest {

  private SignInController sut;

  private CommandHandlerTestUtil<SignInCommand, SignInCommandResponse> commandHandler;

  @BeforeEach
  void setup() {

    commandHandler = new CommandHandlerTestUtil<>();
    sut = new SignInController(commandHandler);
  }

  @Test
  void signInOk() {

    // Arrange
    var request = new SignInRequest("foo", "123456");
    var response = new SignInCommandResponse("jwt_content");
    commandHandler.initResponse(response);

    // Act
    var result = sut.signIn(request);

    // Assert
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().jwt()).isEqualTo(response.jwt());
  }
}
