package com.tadaah;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableCaching
@EnableMongoAuditing
public class DocumentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentServiceApplication.class, args);
  }

}
