package com.tc.execution.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tc.oc.common.models.notification.DataDTO;
import org.dozer.CustomConverter;

public class ObjectToStringConverter implements CustomConverter {

  @Override
  public String convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
  Class<?> sourceClass) {
    if(sourceFieldValue == null) {
      return null;
    }
    if (sourceClass.equals(DataDTO.class)) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.writeValueAsString(sourceFieldValue);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
  
