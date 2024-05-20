package com.nancho313.loqui.auth.test.persistence.util;

import com.nancho313.loqui.auth.persistence.service.LoginResolver;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashSet;
import java.util.Set;

public class LoginResolverSpy implements LoginResolver {
  
  private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "The given credentials are not valid.";
  
  private static String DEFAULT_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
  
  private final Set<TestCredentials> storedCredentials;
  
  private String jwtToReturn;
  
  public LoginResolverSpy() {
    
    storedCredentials = new HashSet<>();
    jwtToReturn = DEFAULT_JWT;
  }
  
  public String login(String username, String password) {
    
    var credentials = new TestCredentials(username, password);
    
    if (storedCredentials.contains(credentials)) {
      
      return jwtToReturn;
    } else {
      throw new BadCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
    }
  }
  
  public void addCredential(String username, String password) {
    
    storedCredentials.add(new TestCredentials(username, password));
  }
  
  public void setJwtToReturn(String jwtToReturn) {
    this.jwtToReturn = jwtToReturn;
  }
  
  private record TestCredentials(String username, String password) {
  
  }
}
