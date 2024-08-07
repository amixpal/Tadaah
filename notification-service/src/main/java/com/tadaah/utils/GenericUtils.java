package com.tadaah.utils;


import org.springframework.beans.BeanUtils;

public class GenericUtils {
  public static <T> T mergeObjects(T target, Object source) {
    BeanUtils.copyProperties(source, target);
    return target;
  }
}
