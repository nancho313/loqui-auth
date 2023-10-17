package com.nancho313.loqui.auth.model.command.signin.command;

import com.nancho313.loqui.auth.model.command.Command;

public record SignInCommand (String username, String password) implements Command {
}
