package com.nancho313.loqui.auth.model.command.signin.command;

import com.nancho313.loqui.auth.model.command.CommandResponse;
import jakarta.validation.constraints.NotBlank;

public record SignInCommandResponse(
        @NotBlank(message = "The token cannot be empty.") String jwt) implements CommandResponse {
}
