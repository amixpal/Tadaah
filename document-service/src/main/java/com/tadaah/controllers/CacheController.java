package com.tadaah.controllers;

import com.tadaah.config.CacheInspector;
import com.tadaah.models.Dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/cache")
@Tag(name = "Cache Management", description = "Operations related to cache inspection and management")
public class CacheController {

  @Autowired
  private CacheInspector cacheInspector;

  @GetMapping("/inspect/{cacheName}")
  @Operation(
      summary = "Inspect Cache",
      description = "Retrieves all entries from the specified cache. Returns a message if the cache is empty.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved cache content",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"amix.pal\": { \"name\": \"amix.palDrriving License2kk3\", \"documentType\": \"FINANCIAL_DOCUMENT\", \"userName\": \"amix.pal\", \"fileUrl\": \"newursasssal.com\", \"expiryDate\": \"2024-10-24\", \"verified\": true, \"id\": \"66b60442a3ac456c2c0d8362\" }, \"66b60442a3ac456c2c0d8362\": { \"name\": \"amix.palDrriving License2kk3\", \"documentType\": \"FINANCIAL_DOCUMENT\", \"userName\": \"amix.pal\", \"fileUrl\": \"newursasssal.com\", \"expiryDate\": \"2024-10-24\", \"verified\": true, \"id\": \"66b60442a3ac456c2c0d8362\" }, \"amix.palDrriving License2kk3\": { \"name\": \"amix.palDrriving License2kk3\", \"documentType\": \"FINANCIAL_DOCUMENT\", \"userName\": \"amix.pal\", \"fileUrl\": \"newursasssal.com\", \"expiryDate\": \"2024-10-24\", \"verified\": true, \"id\": \"66b60442a3ac456c2c0d8362\" } }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Cache not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"success\": false, \"data\": null, \"error\": { \"status\": \"NOT_FOUND\", \"message\": \"Cache not found\", \"errors\": [\"The specified cache does not exist\"] } }"
                  )
              )
          )
      }
  )
  public ResponseDto<Map<Object, Object>> inspectCache(
      @Parameter(description = "The name of the cache to inspect", required = true)
      @PathVariable String cacheName) {
    log.info("inspectCache API called for cacheName: {}", cacheName);
    Map<Object, Object> cacheContent = cacheInspector.getCacheContent(cacheName);

    if (cacheContent.isEmpty()) {
      log.info("Cache {} is empty.", cacheName);
    } else {
      log.info("Cache {} contains {} entries.", cacheName, cacheContent.size());
    }
    return ResponseDto.success(cacheContent);
  }

  @GetMapping("/contains/{cacheName}")
  @Operation(
      summary = "Check Cache for Key",
      description = "Checks if the specified key is present in the specified cache.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully checked cache for key",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Key Present Example",
                      value = "{ \"success\": true, \"data\": \"Key 66b5fd0a99b9461b8992bdc1 is present in cache documentsCache\", \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Cache or key not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Key Not Present Example",
                      value = "{ \"success\": true, \"data\": \"Key 66b5fd0a99b9461b8992bdc1 is not present in cache documentsCache\", \"error\": null }"
                  )
              )
          )
      }
  )
  public ResponseDto<String> checkCacheForKey(
      @Parameter(description = "The name of the cache to check", required = true)
      @PathVariable String cacheName,
      @Parameter(description = "The key to check for presence in the cache", required = true)
      @RequestParam String key) {
    log.info("checkCacheForKey API called for cacheName: {}, key: {}", cacheName, key);
    boolean isPresent = cacheInspector.isCachePresent(cacheName, key);
    String message = isPresent ? "Key " + key + " is present in cache " + cacheName
        : "Key " + key + " is not present in cache " + cacheName;
    log.info(message);
    return ResponseDto.success(message);
  }

  @GetMapping("/size/{cacheName}")
  @Operation(
      summary = "Get Cache Size",
      description = "Retrieves the total number of entries in the specified cache.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved cache size",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": \"Cache documentsCache contains 3 entries.\", \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Cache not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"success\": false, \"data\": null, \"error\": { \"status\": \"NOT_FOUND\", \"message\": \"Cache not found\", \"errors\": [\"The specified cache does not exist\"] } }"
                  )
              )
          )
      }
  )
  public ResponseDto<String> getCacheSize(
      @Parameter(description = "The name of the cache to inspect", required = true)
      @PathVariable String cacheName) {
    log.info("getCacheSize API called for cacheName: {}", cacheName);
    int size = cacheInspector.getCacheSize(cacheName);
    String message = "Cache " + cacheName + " contains " + size + " entries.";
    log.info(message);
    return ResponseDto.success(message);
  }
}
