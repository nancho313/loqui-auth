package com.nancho313.loqui.auth.model.exception;

import java.util.List;

public class InvalidCommandDataException extends RuntimeException {

    private static final String INVALID_COMMAND_ERROR_MESSAGE = "The command has invalid values. Error -> %s";

    public InvalidCommandDataException(List<String> errors) {

        super(INVALID_COMMAND_ERROR_MESSAGE.formatted(errors));
    }
}
