package com.tadaah.security;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SecurityConfig is a configuration class that implements WebMvcConfigurer to customize
 * the CORS (Cross-Origin Resource Sharing) settings for the application.
 *
 * This configuration allows the application to accept cross-origin requests from any domain
 * and supports common HTTP methods (GET, POST, PUT, DELETE, OPTIONS).
 * All headers are permitted, and credentials (such as cookies or HTTP authentication) are allowed
 * to be included in the requests.
 */
public class SecurityConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")  // Allow CORS for all paths
        .allowedOrigins("*")  // Allow all origins
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow all methods
        .allowedHeaders("*")  // Allow all headers
        .allowCredentials(true);  // Allow credentials
  }
}