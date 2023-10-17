package com.nancho313.loqui.auth.api.controller;


import com.nancho313.loqui.auth.api.dto.SignUpApiRequest;
import com.nancho313.loqui.auth.model.command.CommandHandler;
import com.nancho313.loqui.auth.model.command.EmptyCommandResponse;
import com.nancho313.loqui.auth.model.command.signup.command.SignUpCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/signup")
@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final CommandHandler<SignUpCommand, EmptyCommandResponse> commandHandler;

    @PostMapping
    public ResponseEntity<Void> signUpUser(@RequestBody SignUpApiRequest request) {

        commandHandler.handle(new SignUpCommand(request.username(), request.password(), request.email()));
        return ResponseEntity.ok(null);
    }

}
