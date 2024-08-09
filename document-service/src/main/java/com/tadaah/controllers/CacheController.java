package com.tadaah.controllers;

import com.tadaah.config.CacheInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

  @Autowired
  private CacheInspector cacheInspector;

  // Endpoint to log all cache contents by cache name
  @GetMapping("/inspect/{cacheName}")
  public String inspectCache(@PathVariable String cacheName) {
    cacheInspector.logCacheContent(cacheName);
    return "Check logs for cache content details of cache: " + cacheName;
  }

  // Endpoint to check if a specific key is present in the cache
  @GetMapping("/contains/{cacheName}")
  public String checkCacheForKey(@PathVariable String cacheName, @RequestParam String key) {
    boolean isPresent = cacheInspector.isCachePresent(cacheName, key);
    return isPresent ? "Key " + key + " is present in cache " + cacheName
        : "Key " + key + " is not present in cache " + cacheName;
  }

  // Endpoint to check the total number of entries in the cache
  @GetMapping("/size/{cacheName}")
  public String getCacheSize(@PathVariable String cacheName) {
    int size = cacheInspector.getCacheSize(cacheName);
    return "Cache " + cacheName + " contains " + size + " entries.";
  }
}
