package com.nancho313.loqui.auth.api.config;

import com.nancho313.loqui.auth.model.exception.InvalidCommandDataException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler({InvalidCommandDataException.class, IllegalArgumentException.class})
  public ResponseEntity<ErrorDto> handleBadRequest(Exception e) {
    
    return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
  }
  
  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<ErrorDto> unauthorized(BadCredentialsException e) {
    
    return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new ErrorDto(e.getMessage()));
  }
  
  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorDto> defaultException(Exception e) {
    
    return ResponseEntity.internalServerError().body(new ErrorDto(e.getMessage()));
  }
  
  protected record ErrorDto(String message, LocalDateTime date) {
    
    ErrorDto(String message) {
      
      this(message, LocalDateTime.now());
    }
  }
}
