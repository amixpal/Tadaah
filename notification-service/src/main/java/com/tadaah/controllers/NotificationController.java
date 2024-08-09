package com.tadaah.controllers;

import com.tadaah.config.RabbitMQProducerService;
import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.models.Dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping("/v1/api/notifications")
@Tag(name = "Notification Management", description = "Operations related to sending and managing notifications")
public class NotificationController {

  @Autowired
  private RabbitMQProducerService rabbitMQProducerService;

  /**
   * Sends a new notification.
   *
   * @param notificationDto The details of the notification to send.
   * @return A response indicating that the notification was received and will be processed.
   */
  @PostMapping
  @Operation(
      summary = "Send Notification",
      description = "Receives notification details and sends them to RabbitMQ for processing.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Notification received and queued successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(name = "Success Example", value = "{ \"success\": true, \"data\": \"Notification received and will be sent successfully\", \"error\": null }")
              )
          ),
          @ApiResponse(responseCode = "400", description = "Invalid input provided")
      }
  )
  public Mono<ResponseDto<String>> sendNotification(
      @Parameter(description = "The notification details to be sent", required = true)
      @Valid @RequestBody NotificationDto notificationDto) {
    log.info("createNotification API called with payload: {}", notificationDto);

    // Create a Mono that will execute the message sending operation when subscribed to
    Mono<Void> sendMessageMono = Mono.defer(() -> {
      try {
        rabbitMQProducerService.sendMessage(notificationDto);
        log.info("Message sent to RabbitMQ successfully");
      } catch (Exception e) {
        log.error("Failed to send message to RabbitMQ", e);
      }
      return Mono.empty();
    });

    // Subscribe to the sendMessageMono on a bounded elastic scheduler
    sendMessageMono = sendMessageMono.subscribeOn(Schedulers.boundedElastic());

    // Return a Mono that represents the response to the client
    return sendMessageMono.then(
        Mono.just(ResponseDto.success("Notification received and will be sent successfully")));
  }
}
