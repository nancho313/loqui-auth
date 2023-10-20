package com.nancho313.loqui.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.nancho313.loqui.auth")
public class AuthApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }
  
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
  
}
