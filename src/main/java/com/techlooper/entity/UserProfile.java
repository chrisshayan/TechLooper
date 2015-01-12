package com.techlooper.entity;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by phuonghqh on 12/25/14.
 */
public abstract class UserProfile {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserProfile.class);

  private static List<String> ENTITY_ID_FIELDS = Arrays.asList("email", "emailAddress", "accountEmail", "id");

  private AccessGrant accessGrant;

  public String entityId() {
    final String[] entityId = new String[1];
    Object instance = this instanceof SimpleUserProfile ? ((SimpleUserProfile)this).getActual() : this;
    ENTITY_ID_FIELDS.stream().filter((field) -> {
      try {
        entityId[0] = BeanUtils.getSimpleProperty(instance, field);
      }
      catch (IllegalAccessException e) {
        LOGGER.debug("Field {} is not accessible", field);
      }
      catch (InvocationTargetException e) {
        LOGGER.debug("Field {} is not invokable", field);
      }
      catch (NoSuchMethodException e) {
        LOGGER.debug("Field {} is not gettable", field);
      }
      return Optional.ofNullable(entityId[0]).isPresent();
    }).findFirst();
    return entityId[0];
  }

  public AccessGrant getAccessGrant() {
    return accessGrant;
  }

  public void setAccessGrant(AccessGrant accessGrant) {
    this.accessGrant = accessGrant;
  }
}
