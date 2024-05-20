package com.nancho313.loqui.auth.persistence.client.mongodb.dao;

import com.nancho313.loqui.auth.persistence.client.mongodb.document.AuthUserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthUserMongodbDAO extends MongoRepository<AuthUserDocument, String> {
  
  boolean existsByUsername(String username);
  
  boolean existsByEmail(String email);
  
  Optional<AuthUserDocument> findByUsername(String username);
}
