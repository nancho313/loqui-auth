package com.nancho313.loqui.auth.persistence.service;

import com.nancho313.loqui.auth.persistence.client.mongodb.dao.AuthUserMongodbDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class LoginResolverImpl implements LoginResolver{
  
  private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "The given credentials are not valid.";
  
  private final AuthUserMongodbDAO authUserDao;
  
  private final PasswordEncoder loquiPasswordEncoder;
  
  private final String jwtKey;
  
  public LoginResolverImpl(AuthUserMongodbDAO authUserDao, PasswordEncoder loquiPasswordEncoder, @Value("${loqui.auth.jwt.key}") String jwtKey) {
    this.authUserDao = authUserDao;
    this.loquiPasswordEncoder = loquiPasswordEncoder;
    this.jwtKey = jwtKey;
  }
  
  public String login(String username, String password) {
    
    var optionalAuthUser = authUserDao.findByUsername(username);
    
    if (optionalAuthUser.isPresent()) {
      
      var authUser = optionalAuthUser.get();
      validatePassword(password, authUser.password());
      var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
      return Jwts.builder()
              .subject(authUser.id())
              .signWith(key)
              .issuedAt(getIssuedAtDate())
              .expiration(getExpirationDate())
              .claim("lqu", authUser.username())
              .compact();
    } else {
      throw new BadCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
    }
  }
  
  private void validatePassword(String rawPassword, String encodedPassword) {
    
    if (!loquiPasswordEncoder.matches(rawPassword, encodedPassword)) {
      
      throw new BadCredentialsException(INVALID_CREDENTIALS_ERROR_MESSAGE);
    }
  }
  
  private Date getIssuedAtDate() {
    return new Date(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
  }
  
  private Date getExpirationDate() {
    
    return new Date(LocalDateTime.now().plusDays(5).toInstant(ZoneOffset.UTC).toEpochMilli());
  }
}
