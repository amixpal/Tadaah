package com.tadaah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
public class DocumentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentServiceApplication.class, args);
  }

}
