package com.techlooper.converter;

import org.dozer.DozerConverter;

import java.util.Map;
import java.util.Set;

/**
 * Created by phuonghqh on 12/24/14.
 */
public class ProfileNameConverter extends DozerConverter<Map, Set> {

  public ProfileNameConverter() {
    super(Map.class, Set.class);
  }

  public Set convertTo(Map source, Set destination) {
    return source.keySet();
  }

  public Map convertFrom(Set source, Map destination) {
    return destination;
  }
}
