package com.techlooper.converter;

import org.dozer.DozerConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 5/20/15.
 */
public class ListCSVStringConverter extends DozerConverter<Object, List> {

  public ListCSVStringConverter() {
    super(Object.class, List.class);
  }

  public List convertTo(Object source, List destination) {
    return Arrays.asList(source);
  }

  public Object convertFrom(List source, Object destination) {
    return source.stream().map(Object::toString).collect(Collectors.joining(",")).toString();
  }
}