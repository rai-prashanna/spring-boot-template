package com.tc.execution.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.CustomConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class StringAndListConverter implements CustomConverter {

  @Override
  public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
      Class<?> sourceClass) {

    if(sourceFieldValue == null) {
      return null;
    }
    if (sourceClass.equals(String.class)) {
      return new ArrayList<>(
          Arrays.asList(((String) sourceFieldValue).replace("\"", "").replace("[", "").replace("]", "").replace(" ", "").split(",")));
    }
    if (sourceClass.equals(ArrayList.class)) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.writeValueAsString(sourceFieldValue);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return new ArrayList<>();
  }
}
