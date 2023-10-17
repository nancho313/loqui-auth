package com.nancho313.loqui.auth.model.command.signin.command;

import com.nancho313.loqui.auth.model.command.CommandResponse;

public record SignInCommandResponse (String jwt) implements CommandResponse {
}
