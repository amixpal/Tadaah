package com.tadaah.config;

import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

/**
 * Utility class to inspect and interact with cache content.
 * Provides methods to log the contents of a cache, check the presence of a key, and retrieve the size of a cache.
 */
@Slf4j
@Component
public class CacheInspector {
  
  @Autowired
  private CacheManager cacheManager;

  /**
   * Logs the contents of a specified cache.
   * If the cache does not exist or is empty, appropriate messages are logged.
   *
   * @param cacheName The name of the cache to inspect.
   */
  public Map<Object, Object> getCacheContent(String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) cache.getNativeCache();
      return cacheMap;
    } else {
      return Collections.emptyMap();
    }
  }

  /**
   * Checks if a specific key is present in a cache and logs the result.
   *
   * @param cacheName The name of the cache to check.
   * @param key The key to check for presence in the cache.
   * @return true if the key is present in the cache, false otherwise.
   */
  @SuppressWarnings("unchecked")
  public boolean isCachePresent(String cacheName, Object key) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      Object nativeCache = cache.getNativeCache();
      if (nativeCache instanceof ConcurrentMap) {
        ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) nativeCache;
        if (cacheMap.containsKey(key)) {
          log.info("Cache {} contains key: {}", cacheName, key);
          return true;
        } else {
          log.info("Cache {} does not contain key: {}", cacheName, key);
          return false;
        }
      } else {
        log.warn("Cache {} is not a ConcurrentMap. Unable to check key presence.", cacheName);
        return false;
      }
    } else {
      log.warn("Cache {} does not exist.", cacheName);
      return false;
    }
  }

  /**
   * Retrieves the size of a specified cache.
   * Logs a message if the cache does not exist or is not a ConcurrentMap.
   *
   * @param cacheName The name of the cache to inspect.
   * @return The number of entries in the cache, or 0 if the cache does not exist or is not a ConcurrentMap.
   */
  @SuppressWarnings("unchecked")
  public int getCacheSize(String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      Object nativeCache = cache.getNativeCache();
      if (nativeCache instanceof ConcurrentMap) {
        ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) nativeCache;
        int size = cacheMap.size();
        log.info("Cache {} contains {} entries.", cacheName, size);
        return size;
      } else {
        log.warn("Cache {} is not a ConcurrentMap. Unable to determine size.", cacheName);
        return 0;
      }
    } else {
      log.warn("Cache {} does not exist.", cacheName);
      return 0;
    }
  }
}
