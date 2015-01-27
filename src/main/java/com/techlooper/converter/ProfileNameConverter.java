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

  public Set convertTo(Map profiles, Set profileNames) {
    return profiles.keySet();
  }

  public Map convertFrom(Set profileNames, Map profiles) {
    Set names = profiles.keySet();
    profileNames.stream().filter(name -> !names.contains(name)).forEach(profileName -> profiles.put(profileName, null));
    return profiles;
  }
}
