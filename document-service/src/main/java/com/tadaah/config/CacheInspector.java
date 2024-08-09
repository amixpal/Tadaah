package com.tadaah.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

@Component
public class CacheInspector {

  @Autowired
  private CacheManager cacheManager;

  public void logCacheContent(String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);

    if (cache != null) {
      ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) cache.getNativeCache();
      if (cacheMap.isEmpty()) {
        System.out.println("Cache " + cacheName + " is empty.");
      } else {
        System.out.println("Contents of cache " + cacheName + ":");
        cacheMap.forEach((key, value) -> {
          System.out.println("Key: " + key + ", Value: " + value);
        });
      }
    } else {
      System.out.println("Cache " + cacheName + " does not exist.");
    }
  }

  public boolean isCachePresent(String cacheName, Object key) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      Cache.ValueWrapper valueWrapper = cache.get(key);
      if (valueWrapper != null) {
        System.out.println("Cache " + cacheName + " contains key: " + key + " with value: " + valueWrapper.get());
        return true;
      } else {
        System.out.println("Cache " + cacheName + " does not contain key: " + key);
        return false;
      }
    } else {
      System.out.println("Cache " + cacheName + " does not exist.");
      return false;
    }
  }

  public int getCacheSize(String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) cache.getNativeCache();
      return cacheMap.size();
    } else {
      System.out.println("Cache " + cacheName + " does not exist.");
      return 0;
    }
  }
}

