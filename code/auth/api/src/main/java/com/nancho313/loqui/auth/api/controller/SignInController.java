package com.nancho313.loqui.auth.api.controller;

import com.nancho313.loqui.auth.api.dto.SignInRequest;
import com.nancho313.loqui.auth.api.dto.SignInResponse;
import com.nancho313.loqui.auth.model.command.CommandHandler;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommand;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SignInController {
  
  private final CommandHandler<SignInCommand, SignInCommandResponse> commandHandler;
  
  @PostMapping
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
    
    var response = commandHandler.handle(new SignInCommand(signInRequest.username(), signInRequest.password()));
    return ResponseEntity.ok(new SignInResponse(response.jwt()));
  }
}