package com.techlooper.converter;

import org.dozer.DozerConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 5/20/15.
 */
public class List2CSVStringConverter extends DozerConverter<String, List> {

  public List2CSVStringConverter() {
    super(String.class, List.class);
  }

  public List convertTo(String source, List destination) {
    return null;
  }

  public String convertFrom(List source, String destination) {
    return source.stream().map(Object::toString).collect(Collectors.joining(",")).toString();
  }
}