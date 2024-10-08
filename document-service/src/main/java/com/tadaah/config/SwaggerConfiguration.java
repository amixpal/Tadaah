package com.tadaah.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public OpenAPI publicApi() {
    return new OpenAPI()
        .info(new Info()
            .title("Tadaah API")
            .version("v1")
            .description("API documentation for document-service")
            .contact(new Contact()
                .name("Amit Pal")
                .email("some@gmail.com"))
        );
  }
}
