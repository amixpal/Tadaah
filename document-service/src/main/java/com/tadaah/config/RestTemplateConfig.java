package com.tadaah.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

  @Value("${notification.service.url:#{null}}")
  private String notificationServiceUrl;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public String notificationServiceUrl() {
    if (notificationServiceUrl == null || notificationServiceUrl.isEmpty()) {
      logger.error("Notification service URL is not configured!");
      throw new IllegalStateException("Notification service URL must be configured");
    }
    logger.info("Notification service URL: {}", notificationServiceUrl);
    return notificationServiceUrl;
  }
}
