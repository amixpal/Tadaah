package com.tadaah.controllers;

import com.tadaah.models.Dto.request.UserDto;
import com.tadaah.models.Dto.request.UserFilterRequestDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.models.Users;
import com.tadaah.services.UserService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  @Operation(
      summary = "Create User",
      description = "Creates a new user with the provided details.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "User created successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Users.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"userName\": \"john_doe\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"createdDate\": null, \"lastModifiedDate\": \"2024-08-09T18:06:44.679263\" }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input provided",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"success\": false, \"data\": null, \"error\": { \"status\": \"BAD_REQUEST\", \"message\": \"Invalid input provided\", \"errors\": [\"First name is required\"] } }"
                  )
              )
          )
      }
  )
  public ResponseDto<Users> createUser(
      @Parameter(description = "The user details to be created", required = true)
      @RequestBody @Valid UserDto userDto) {
    log.info("createUser API called with parameters: {}", userDto);
    Users createdUser = userService.createUser(userDto);
    log.info("User created successfully: {}", createdUser);
    return ResponseDto.success(createdUser);
  }




  @DeleteMapping("/{username}")
  @Operation(
      summary = "Delete User",
      description = "Deletes a user with the specified username.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "User deleted successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": \"User deleted successfully\", \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "User not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"success\": false, \"data\": null, \"error\": { \"status\": \"NOT_FOUND\", \"message\": \"User not found\", \"errors\": [\"User with username not found\"] } }"
                  )
              )
          )
      }
  )
  public ResponseDto<String> deleteUser(
      @Parameter(description = "The username of the user to be deleted", required = true)
      @PathVariable String username) {
    log.info("deleteUser API called with username: {}", username);
    userService.deleteUser(username);
    log.info("User deleted successfully with username: {}", username);
    return ResponseDto.success("User deleted successfully");
  }





  @PostMapping("/filter")
  @Operation(
      summary = "Get All Users",
      description = "Retrieves a paginated list of users based on optional filters.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved users",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = PaginatedResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"totalElements\": 0, \"totalPages\": 0, \"size\": 10, \"content\": [], \"first\": true, \"last\": true, \"empty\": true }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid filter criteria",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"success\": false, \"data\": null, \"error\": { \"status\": \"BAD_REQUEST\", \"message\": \"Invalid filter criteria\", \"errors\": [\"Username filter cannot be empty\"] } }"
                  )
              )
          )
      }
  )
  public ResponseDto<PaginatedResponseDto<Users>> getAllUsers(
      @Parameter(description = "Filter criteria for retrieving users", required = true)
      @RequestBody UserFilterRequestDto filterDto) {
    log.info("getAllUsers API called with filters - username: {}", filterDto.getUserName());
    Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
    PaginatedResponseDto<Users> users = userService.getAllUsers(filterDto.getUserName(), pageable);
    log.info("Fetched {} users", users.getTotalElements());
    return ResponseDto.success(users);
  }
}
