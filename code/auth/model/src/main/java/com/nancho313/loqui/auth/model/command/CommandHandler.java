package com.nancho313.loqui.auth.model.command;

import com.nancho313.loqui.auth.model.exception.InvalidCommandDataException;
import com.nancho313.loqui.auth.model.exception.InvalidResponseDataException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

public abstract class CommandHandler<C extends Command, R extends CommandResponse> {
  
  private final Validator validator;
  
  public CommandHandler(Validator validator) {
    this.validator = validator;
  }
  
  public R handle(C command) {
    
    validateCommand(command);
    var response = handleCommand(command);
    validateResponse(response);
    return response;
  }
  
  protected abstract R handleCommand(C command);
  
  private void validateCommand(C data) {
    
    var errors = validateData(data);
    if (!errors.isEmpty()) {
      
      throw new InvalidCommandDataException(errors);
    }
  }
  
  private void validateResponse(R data) {
    
    var errors = validateData(data);
    if (!errors.isEmpty()) {
      
      throw new InvalidResponseDataException(errors);
    }
  }
  
  private <Y> List<String> validateData(Y data) {
    
    Set<ConstraintViolation<Y>> violations = validator.validate(data);
    return violations.stream().map(ConstraintViolation::getMessage).toList();
  }
}
