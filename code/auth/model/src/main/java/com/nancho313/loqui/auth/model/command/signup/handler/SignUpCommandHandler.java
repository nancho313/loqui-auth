package com.nancho313.loqui.auth.model.command.signup.handler;

import com.nancho313.loqui.auth.model.command.CommandHandler;
import com.nancho313.loqui.auth.model.command.EmptyCommandResponse;
import com.nancho313.loqui.auth.model.command.signup.command.SignUpCommand;
import com.nancho313.loqui.auth.persistence.service.IdGenerator;
import com.nancho313.loqui.auth.persistence.service.UserAuthenticator;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpCommandHandler extends CommandHandler<SignUpCommand, EmptyCommandResponse> {
  
  private final UserAuthenticator userAuthenticator;
  
  private final IdGenerator idGenerator;
  
  public SignUpCommandHandler(Validator validator, UserAuthenticator userAuthenticator, IdGenerator idGenerator) {
    super(validator);
    this.userAuthenticator = userAuthenticator;
    this.idGenerator = idGenerator;
  }
  
  @Override
  protected EmptyCommandResponse handleCommand(SignUpCommand command) {
    
    if (userAuthenticator.existsByUsername(command.username())) {
      
      throw new IllegalArgumentException("The username %s is already used.".formatted(command.username()));
    }
    
    if (userAuthenticator.existsByEmail(command.email())) {
      
      throw new IllegalArgumentException("The email %s is already used.".formatted(command.email()));
    }
    
    userAuthenticator.storeBasicCredentials(idGenerator.generateId(), command.username(), command.password(), command.email());
    
    return new EmptyCommandResponse();
  }
}
