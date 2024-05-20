package com.nancho313.loqui.auth.test.persistence.service;

import com.nancho313.loqui.auth.persistence.service.IdGenerator;
import com.nancho313.loqui.auth.persistence.service.IdGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorTest {
  
  private IdGenerator sut;
  
  @BeforeEach
  void setup() {
    
    sut = new IdGeneratorImpl();
  }
  
  @Test
  void generateIdOk() {
    
    // Act
    var result = sut.generateId();
    
    // Assert
    assertThat(result).isNotBlank();
  }
}
