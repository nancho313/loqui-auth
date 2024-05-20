package com.nancho313.loqui.auth.test.persistence.service;

import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import com.nancho313.loqui.auth.persistence.client.mongodb.document.AuthUserDocument;
import com.nancho313.loqui.auth.persistence.service.LoginResolver;
import com.nancho313.loqui.auth.persistence.service.LoginResolverImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginResolverTest {
  
  private static final String JWT_KEY = "Zm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkgZm9vX2p3dF9rZXkg"; //foo_jwt_key
  
  private static final String DEFAULT_EMAIL = "email@hotmail.com";
  
  private LoginResolver sut;
  
  private AuthUserMongodbDAO authUserMongodbDAOStub;
  
  private PasswordEncoder passwordEncoder;
  
  @BeforeEach
  void setup() {
    
    authUserMongodbDAOStub = mock(AuthUserMongodbDAO.class);
    passwordEncoder = new BCryptPasswordEncoder(10);
    sut = new LoginResolverImpl(authUserMongodbDAOStub, passwordEncoder, JWT_KEY);
  }
  
  @Test
  void loginOk() {
    
    // Arrange
    var username = "foo";
    var password = "123";
    when(authUserMongodbDAOStub.findByUsername(username)).thenReturn(Optional.of(buildUser(username, password)));
    
    // Act
    var result = sut.login(username, password);
    
    // Assert
    assertThat(result).isNotNull();
  }
  
  @Test
  void loginThrowsBadCredentialsWhenPasswordIsNotTheSame() {
    
    // Arrange
    var username = "foo";
    var password = "123";
    when(authUserMongodbDAOStub.findByUsername(username)).thenReturn(Optional.of(buildUser(username, "123456")));
    
    // Act & Assert
    assertThrows(BadCredentialsException.class, () -> sut.login(username, password));
  }
  
  @Test
  void loginThrowsBadCredentialsWhenUserDoesNotExist() {
    
    // Arrange
    var username = "foo";
    var password = "123";
    when(authUserMongodbDAOStub.findByUsername(username)).thenReturn(Optional.empty());
    
    // Act & Assert
    assertThrows(BadCredentialsException.class, () -> sut.login(username, password));
  }
  
  private AuthUserDocument buildUser(String username, String password) {
    
    return new AuthUserDocument(new ObjectId().toHexString(), username, passwordEncoder.encode(password),
            DEFAULT_EMAIL, LocalDateTime.now());
  }
}
