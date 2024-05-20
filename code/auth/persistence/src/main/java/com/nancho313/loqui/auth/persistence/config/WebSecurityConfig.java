package com.nancho313.loqui.auth.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSecurityConfig {
  
  private static final List<String> ALL_ALLOWED_VALUE = List.of("*");
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    http.cors(Customizer.withDefaults())
            .csrf((csrf) -> csrf.ignoringRequestMatchers("/**"))
            .authorizeHttpRequests(authorize -> authorize.requestMatchers("/signin", "/signup").permitAll());
    return http.build();
  }
  
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedMethods(ALL_ALLOWED_VALUE);
    configuration.setAllowedOrigins(ALL_ALLOWED_VALUE);
    configuration.setAllowedHeaders(ALL_ALLOWED_VALUE);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  
  @Bean
  public PasswordEncoder loquiPasswordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
  
}
