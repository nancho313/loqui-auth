package com.nancho313.loqui.auth.model.command.signin.handler;

import com.nancho313.loqui.auth.model.command.CommandHandler;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommand;
import com.nancho313.loqui.auth.model.command.signin.command.SignInCommandResponse;
import com.nancho313.loqui.auth.persistence.service.LoginResolver;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class SignInCommandHandler extends CommandHandler<SignInCommand, SignInCommandResponse> {
  
  private final LoginResolver loginResolver;
  
  public SignInCommandHandler(Validator validator, LoginResolver loginResolver) {
    super(validator);
    this.loginResolver = loginResolver;
  }
  
  @Override
  protected SignInCommandResponse handleCommand(SignInCommand command) {
    
    return new SignInCommandResponse(loginResolver.login(command.username(), command.password()));
  }
}