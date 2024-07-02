package com.nancho313.loqui.auth.test.util;

import com.nancho313.loqui.auth.model.command.Command;
import com.nancho313.loqui.auth.model.command.CommandHandler;
import com.nancho313.loqui.auth.model.command.CommandResponse;
import jakarta.validation.Validation;
import lombok.Getter;

@Getter
public class CommandHandlerTestUtil<T extends Command, V extends CommandResponse> extends CommandHandler<T, V> {

  private T commandToProcess;

  private V responseToReturn;

  public CommandHandlerTestUtil() {
    super(Validation.buildDefaultValidatorFactory().getValidator());
  }

  public void initResponse(V responseToReturn) {
    this.responseToReturn = responseToReturn;
  }

  @Override
  protected V handleCommand(T command) {
    commandToProcess = command;
    return responseToReturn;
  }
}
