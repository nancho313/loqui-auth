package com.nancho313.loqui.auth.model.command.signin.command;

import com.nancho313.loqui.auth.model.command.Command;
import jakarta.validation.constraints.NotBlank;

public record SignInCommand(@NotBlank(message = "The username cannot be empty.") String username,
                            @NotBlank(message = "The password cannot be empty.") String password) implements Command {
}
