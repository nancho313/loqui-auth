package com.nancho313.loqui.auth.persistence.service;

public interface UserAuthenticator {
  
  void storeBasicCredentials(String id, String username, String password, String email);
  
  boolean existsByUsername(String username);
  
  boolean existsByEmail(String email);
}
